package com.juzi.jubi.constant;

/**
 * @author codejuzi
 */
public interface ChartConstant {

    /**
     * AI生成的内容分隔符
     */
    String GEN_CONTENT_SPLITS = "【【【【【";

    /**
     * AI 生成的内容的元素为3个
     */
    int GEN_ITEM_NUM = 3;

    int GEN_CHART_IDX = 1;

    int GEN_RESULT_IDX = 2;

    /**
     * 提取生成的图表的Echarts配置
     */
    String GEN_CHART_REGEX = "\\{(?>[^{}]*(?:\\{[^{}]*}[^{}]*)*)}";

    String DEFAULT_CHART_NAME_PREFIX = "Chart_";

    int DEFAULT_CHART_NAME_SUFFIX_LEN = 10;
}
