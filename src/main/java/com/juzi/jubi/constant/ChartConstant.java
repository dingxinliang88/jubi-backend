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

    /**
     * 生成图表的数据下标
     */
    int GEN_CHART_IDX = 1;

    /**
     * 生成图表的分析结果的下标
     */
    int GEN_RESULT_IDX = 2;

    /**
     * 提取生成的图表的Echarts配置的正则
     */
//    String GEN_CHART_REGEX = "\\{(?>[^{}]*(?:\\{[^{}]*}[^{}]*)*)}";
    String GEN_CHART_REGEX = "\\{.*\\}";

    /**
     * 删除图表的Echarts配置中的title部分
     */
    String REMOVE_GEN_CHART_TITLE_REGEX = "([{,])\\s*\"title\"\\s*:\\s*(\\{[^{}]*}|\"[^\"]*\")\\s*,?";

    /**
     * 图表默认名称的前缀
     */
    String DEFAULT_CHART_NAME_PREFIX = "Chart_";

    /**
     * 图表默认名称的后缀长度
     */
    int DEFAULT_CHART_NAME_SUFFIX_LEN = 10;

    // region chart exec status

    Integer CONS_WAIT = 0;
    Integer CONS_RUNNING = 1;
    Integer CONS_SUCCESS = 2;
    Integer CONS_FAILED = 3;


    // endregion
}
