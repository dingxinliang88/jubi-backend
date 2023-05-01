package com.juzi.jubi.constant;

/**
 * 用户常量
 *
 * @author codejuzi
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    // endregion

    // region 限制
    /**
     * 用户账号最短长度
     */
    int USER_ACCOUNT_MIN_LEN = 4;

    /**
     * 用户密码最短长度
     */
    int USER_PASSWORD_MIN_LEN = 8;
    //
}
