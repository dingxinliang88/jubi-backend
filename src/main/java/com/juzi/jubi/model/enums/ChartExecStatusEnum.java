package com.juzi.jubi.model.enums;

import java.util.Objects;

/**
 * @author codejuzi
 */
public enum ChartExecStatusEnum {
    WAIT("等待中", 0),
    RUNNING("执行中", 1),
    SUCCESS("成功", 2),
    FAILED("失败", 3);

    private final Integer execStatus;

    private final String description;


    ChartExecStatusEnum(String description, Integer execStatus) {
        this.execStatus = execStatus;
        this.description = description;
    }

    public Integer getExecStatus() {
        return execStatus;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据状态获取枚举
     *
     * @param status 图表执行状态
     * @return 枚举
     */
    public ChartExecStatusEnum getEnumByStatus(Integer status) {
        if (Objects.isNull(status) || status < 0) {
            return null;
        }
        for (ChartExecStatusEnum execStatusEnum : ChartExecStatusEnum.values()) {
            if (execStatusEnum.getExecStatus().equals(status)) {
                return execStatusEnum;
            }
        }
        return null;
    }

}
