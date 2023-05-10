package com.juzi.jubi.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel相关处理工具类
 *
 * @author codejuzi
 */
@Slf4j
public class ExcelUtils {

    /**
     * xlsx格式转csv格式
     *
     * @param multipartFile 文件
     * @return 预设 + csv文件内容（目标 + 数据）
     */
    public static String xlsx2Csv(MultipartFile multipartFile) {

        List<Map<Integer, String>> originDataList = null;
        try {
            originDataList = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理失败", e);
        }
        if (CollUtil.isEmpty(originDataList)) {
            return "";
        }
        // 转换成csv
        StringBuilder resBuilder = new StringBuilder();
        // 读取表头
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap<Integer, String>) originDataList.get(0);
        List<String> headerList = headerMap.values().stream()
                .filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
        resBuilder.append(StringUtils.join(headerList, ",")).append("\n");
        // 读取数据
        for (int i = 1; i < originDataList.size(); i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap<Integer, String>) originDataList.get(i);
            List<String> dataList = dataMap.values().stream()
                    .filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
            resBuilder.append(StringUtils.join(dataList, ",")).append("\n");
        }
        return resBuilder.toString();
    }
}
