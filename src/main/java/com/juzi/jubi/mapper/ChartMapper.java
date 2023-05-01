package com.juzi.jubi.mapper;

import com.juzi.jubi.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author codejuzi
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2023-05-01 20:27:39
* @Entity com.juzi.jubi.model.entity.Chart
*/
@Mapper
public interface ChartMapper extends BaseMapper<Chart> {

}




