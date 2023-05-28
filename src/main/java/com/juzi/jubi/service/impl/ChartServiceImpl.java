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

    @Override
    public BiResponse genChartByAi(MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();
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

        // limiter
        User loginUser = userService.getLoginUser(request);
        final String USER_METHOD_RATE_PREFIX = "genChartByAi_";
        // 每个用户一个限流器
        redisLimiterManager.doRateLimit(USER_METHOD_RATE_PREFIX + loginUser.getId());

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
//        String finalGenChart = ChartUtils.removeGenChartTitle(validGenChart);

        // 插入数据
        Chart chart = new Chart();
        name = StringUtils.isBlank(name) ? ChartUtils.genDefaultChartName() : name;
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
//        chart.setGenChart(finalGenChart);
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
}




