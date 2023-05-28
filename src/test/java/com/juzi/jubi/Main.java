package com.juzi.jubi;

import com.juzi.jubi.utils.ChartUtils;

/**
 * @author codejuzi
 */
public class Main {
    public static void main(String[] args) {
        String text = "阿巴巴啊吧啊吧你啊时间和第三方\n" +
                "options = {\n" +
                "  \"title\": {\n" +
                "    \"text\": \"网站用户增长情况\"\n" +
                "  },\n" +
                "  \"xAxis\": {\n" +
                "    \"type\": \"category\",\n" +
                "    \"data\": [\"1号\", \"2号\", \"3号\"]\n" +
                "  },\n" +
                "  \"yAxis\": {\n" +
                "    \"type\": \"value\"\n" +
                "  },\n" +
                "  \"series\": [\n" +
                "    {\n" +
                "      \"data\": [10, 20, 30],\n" +
                "      \"type\": \"line\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n" +
                "根据给定的原始数据，我们可以通过前端Echarts提供的line图表展示网站用户增长情况。从数据上看，用户增长情况比较稳定，每日用户增加10人，因此可以考虑提高用户转化率，从而实现更快的用户增长。";

        String validGenChart = ChartUtils.getValidGenChart(text);
        System.out.println(validGenChart);

        String removeGenChartTitle = ChartUtils.removeGenChartTitle(validGenChart);
        System.out.println(removeGenChartTitle);
    }
}
