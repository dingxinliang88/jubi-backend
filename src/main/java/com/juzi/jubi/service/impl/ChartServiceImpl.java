package com.juzi.jubi.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.jubi.common.ErrorCode;
import com.juzi.jubi.exception.ThrowUtils;
import com.juzi.jubi.manager.AiManager;
import com.juzi.jubi.manager.RedisLimiterManager;
import com.juzi.jubi.mapper.ChartMapper;
import com.juzi.jubi.model.dto.chart.GenChartByAiRequest;
import com.juzi.jubi.model.entity.Chart;
import com.juzi.jubi.model.entity.User;
import com.juzi.jubi.model.vo.BiResponse;
import com.juzi.jubi.service.ChartService;
import com.juzi.jubi.service.UserService;
import com.juzi.jubi.utils.ChartUtils;
import com.juzi.jubi.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static com.juzi.jubi.constant.ChartConstant.*;

/**
 * @author codejuzi
 * @description 针对表【chart(图表信息表)】的数据库操作Service实现
 * @createDate 2023-05-01 20:27:39
 */
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
        implements ChartService {

    @Resource
    private UserService userService;

    @Resource
    private AiManager aiManager;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @SuppressWarnings("DuplicatedCode")
    @Override
    public BiResponse genChartByAi(MultipartFile multipartFile,
                                   GenChartByAiRequest genChartByAiRequest,
                                   HttpServletRequest request) {
        // validation
        validGenChartRequest(multipartFile, genChartByAiRequest);

        User loginUser = userService.getLoginUser(request);
        // limiter
        doLimiter(loginUser);

        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        StringBuilder userInput = new StringBuilder();
        // 构建用户输入
        userInput.append("分析需求：").append("\n");
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 压缩后的数据
        String csvData = ExcelUtils.xlsx2Csv(multipartFile);
        userInput.append(csvData).append("\n");
        // 对接AI，生成内容
        String genContent = aiManager.doGenByYuCongMing(userInput.toString());
        // 解析内容
        String[] contents = genContent.split(GEN_CONTENT_SPLITS);
        ThrowUtils.throwIf(contents.length < GEN_ITEM_NUM, ErrorCode.SYSTEM_ERROR, "生成错误");
        String preGenChart = contents[GEN_CHART_IDX].trim();
        String genResult = contents[GEN_RESULT_IDX].trim();
        String validGenChart = ChartUtils.getValidGenChart(preGenChart);

        // 插入数据
        Chart chart = new Chart();
        name = StringUtils.isBlank(name) ? ChartUtils.genDefaultChartName() : name;
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setGenChart(validGenChart);
        chart.setGenResult(genResult);
        chart.setUserId(loginUser.getId());
        this.save(chart);

        // 生成返回值
        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(validGenChart);
        biResponse.setGenResult(genResult);
        biResponse.setChartId(chart.getId());

        return biResponse;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public BiResponse genChartByAiAsync(MultipartFile multipartFile,
                                        GenChartByAiRequest genChartByAiRequest,
                                        HttpServletRequest request) {
        // validation
        validGenChartRequest(multipartFile, genChartByAiRequest);

        User loginUser = userService.getLoginUser(request);
        // limiter
        doLimiter(loginUser);

        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
        // 压缩后的数据
        String csvData = ExcelUtils.xlsx2Csv(multipartFile);

        // 先插入数据
        Chart chart = new Chart();
        name = StringUtils.isBlank(name) ? ChartUtils.genDefaultChartName() : name;
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setUserId(loginUser.getId());
        chart.setExecStatus(CONS_WAIT);
        boolean saveRes = this.save(chart);
        ThrowUtils.throwIf(!saveRes, ErrorCode.SYSTEM_ERROR, "图表保存失败");

        // 异步处理ai生成
        CompletableFuture.runAsync(() -> {
            Chart updateChart = new Chart();
            Long chartId = chart.getId();
            updateChart.setId(chartId);
            // 执行状态修改为Running
            updateChart.setExecStatus(CONS_RUNNING);
            boolean updateRes = this.updateById(updateChart);
            if (!updateRes) {
                handleChartUpdateError(chartId, "更新图表执行中状态失败");
                return;
            }

            // 构建用户输入
            StringBuilder userInput = new StringBuilder();
            userInput.append("分析需求：").append("\n");
            String userGoal = goal;
            if (StringUtils.isNotBlank(chartType)) {
                userGoal += "请使用" + chartType;
            }
            userInput.append(userGoal).append("\n");
            userInput.append("原始数据：").append("\n");
            // 压缩后的数据
            userInput.append(csvData).append("\n");
            // 对接AI，生成内容
            String genContent = aiManager.doGenByYuCongMing(userInput.toString());
            // 解析内容
            String[] contents = genContent.split(GEN_CONTENT_SPLITS);
            ThrowUtils.throwIf(contents.length < GEN_ITEM_NUM, ErrorCode.SYSTEM_ERROR, "生成错误");
            String preGenChart = contents[GEN_CHART_IDX].trim();
            String genResult = contents[GEN_RESULT_IDX].trim();
            String validGenChart = ChartUtils.getValidGenChart(preGenChart);

            // 更新图表信息
            Chart updateChartResult = new Chart();
            updateChartResult.setId(chartId);
            updateChartResult.setGenChart(validGenChart);
            updateChartResult.setGenResult(genResult);
            updateChartResult.setExecStatus(CONS_SUCCESS);

            updateRes = this.updateById(updateChartResult);
            if(!updateRes) {
                handleChartUpdateError(chartId, "更新图表成功状态失败");
            }

        }, threadPoolExecutor);
        // 生成返回值
        BiResponse biResponse = new BiResponse();
        biResponse.setChartId(chart.getId());
        return biResponse;

    }


    private void handleChartUpdateError(Long chartId, String execMsg) {
        Chart updateChart = new Chart();
        updateChart.setId(chartId);
        updateChart.setExecStatus(CONS_FAILED);
        updateChart.setExecMsg(execMsg);
        boolean updateRes = this.updateById(updateChart);
        if (!updateRes) {
            log.error("更新图表状态失败, chartId=" + chartId + ", execMsg=" + execMsg);
        }
    }


    private void doLimiter(User loginUser) {

        final String USER_METHOD_RATE_PREFIX = "genChartByAi_";
        // 每个用户一个限流器
        redisLimiterManager.doRateLimit(USER_METHOD_RATE_PREFIX + loginUser.getId());
    }


    private void validGenChartRequest(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        // validation
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标不能为空！");
        ThrowUtils.throwIf(StringUtils.isNoneBlank(name) && name.length() > 200,
                ErrorCode.PARAMS_ERROR, "图表名称过长");

        // 文件校验
        long fileSize = multipartFile.getSize();
        final long ONE_MB = 1024 * 1024L;
        ThrowUtils.throwIf(fileSize > ONE_MB, ErrorCode.PARAMS_ERROR, "文件过大");

        String originFileName = multipartFile.getOriginalFilename();
        String fileSuffix = FileUtil.getSuffix(originFileName);
        final List<String> VALID_FILE_SUFFIX_LIST = Arrays.asList("png", "jpg", "xlsx", "pptx", "docx");
        ThrowUtils.throwIf(!VALID_FILE_SUFFIX_LIST.contains(fileSuffix), ErrorCode.PARAMS_ERROR, "非法文件后缀");
    }
}




