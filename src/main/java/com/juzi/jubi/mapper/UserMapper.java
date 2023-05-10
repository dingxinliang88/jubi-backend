package com.juzi.jubi.mapper;

import com.juzi.jubi.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author codejuzi
* @description 针对表【user(用户)】的数据库操作Mapper
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




