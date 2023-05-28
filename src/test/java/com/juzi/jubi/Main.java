package com.juzi.jubi;

import com.juzi.jubi.utils.ChartUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        String contentA = "【【【【【\n" +
                "{\n" +
                "  \"xAxis\": {\n" +
                "    \"type\": \"category\",\n" +
                "    \"data\": [\"A\", \"B\", \"C\", \"D\", \"E\", \"F\", \"G\", \"H\", \"I\", \"J\"]\n" +
                "  },\n" +
                "  \"yAxis\": [\n" +
                "    {\n" +
                "      \"type\": \"value\",\n" +
                "      \"name\": \"销售额\",\n" +
                "      \"position\": \"left\",\n" +
                "      \"axisLabel\": {\n" +
                "        \"formatter\": \"{value} 元\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"value\",\n" +
                "      \"name\": \"利润\",\n" +
                "      \"position\": \"right\",\n" +
                "      \"offset\": 50,\n" +
                "      \"axisLabel\": {\n" +
                "        \"formatter\": \"{value} 元\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"series\": [\n" +
                "    {\n" +
                "      \"name\": \"销售额\",\n" +
                "      \"data\": [100, 200, 150, 120, 180, 90, 250, 300, 170, 200],\n" +
                "      \"type\": \"bar\",\n" +
                "      \"barCategoryGap\": \"50%\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"利润\",\n" +
                "      \"data\": [20, 30, 25, 10, 35, 15, 50, 70, 30, 40],\n" +
                "      \"type\": \"line\",\n" +
                "      \"yAxisIndex\": 1\n" +
                "    }\n" +
                "  ]\n" +
                "}\n" +
                "【【【【【\n" +
                "从数据可以看出，销售额和利润之间确实存在相关性，即销售额越高的产品，相应的利润也越高，反之亦然。具体而言，可以看出有以下规律：\n" +
                "1. 销售额和利润之间的相关性比较明显，有较强的正相关关系。\n" +
                "2. 线性拟合出的趋势线斜率为0.45，表明每增加100元的销售额，平均获得45元的利润。\n" +
                "3. 从销售额和利润的变化趋势来看，销售额和利润都呈现出增长的趋势，但销售额的增长幅度大于利润的增长幅度。";

        String json = ChartUtils.getValidGenChart(contentA);
        System.out.println("json = " + json);

        String removeGenChartTitle = ChartUtils.removeGenChartTitle(json);
        System.out.println(removeGenChartTitle);
    }

}
