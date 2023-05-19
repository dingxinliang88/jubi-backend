package com.juzi.jubi.service;

import com.juzi.jubi.model.dto.chart.GenChartByAiRequest;
import com.juzi.jubi.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juzi.jubi.model.vo.BiResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author codejuzi
 * @description 针对表【chart(图表信息表)】的数据库操作Service
 * @createDate 2023-05-01 20:27:39
 */
public interface ChartService extends IService<Chart> {

    /**
     * AI生成图表
     *
     * @param multipartFile       用户上传的文件信息
     * @param genChartByAiRequest 用户的需求
     * @param request             http request
     * @return BiResponse 处理后的ai生成内容
     */
    BiResponse genChartByAi(MultipartFile multipartFile,
                            GenChartByAiRequest genChartByAiRequest,
                            HttpServletRequest request);
}
