package com.juzi.jubi.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * BI图表生成返回值
 *
 * @author codejuzi
 */
@Data
public class BiResponse implements Serializable {

    private static final long serialVersionUID = -5462804010026550244L;

    private String genChart;

    private String genResult;

    private Long chartId;
}
