package com.juzi.jubi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juzi.jubi.model.dto.user.UserQueryRequest;
import com.juzi.jubi.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juzi.jubi.model.vo.LoginUserVO;
import com.juzi.jubi.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author codejuzi
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-05-01 20:27:32
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request request
     * @return User info
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request request
     * @return User
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request request
     * @return User
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user user info
     * @return true - 是管理员
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request request
     * @return true - 登出成功
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @param user user info
     * @return 脱敏后的用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user user info
     * @return User VO （脱敏）
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList user list
     * @return user vo list
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 用户查询请求
     * @return 查询条件封装包
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}
