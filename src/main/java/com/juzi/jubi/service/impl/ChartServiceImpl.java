package com.juzi.jubi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.jubi.model.entity.Chart;
import com.juzi.jubi.service.ChartService;
import com.juzi.jubi.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author codejuzi
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2023-05-01 20:27:39
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




