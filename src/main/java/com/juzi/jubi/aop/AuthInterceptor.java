package com.juzi.jubi.aop;

import com.juzi.jubi.annotation.AuthCheck;
import com.juzi.jubi.common.ErrorCode;
import com.juzi.jubi.exception.ThrowUtils;
import com.juzi.jubi.model.entity.User;
import com.juzi.jubi.model.enums.UserRoleEnum;
import com.juzi.jubi.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验 AOP
 *
 * @author codejuzi
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint 切点
     * @param authCheck 权限校验
     * @return 切面方法执行结果
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 必须有该权限才通过
        if (StringUtils.isNotBlank(mustRole)) {
            UserRoleEnum mustUserRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
            ThrowUtils.throwIf(mustUserRoleEnum == null, ErrorCode.NO_AUTH_ERROR);
            String userRole = loginUser.getUserRole();
            // 如果被封号，直接拒绝
            ThrowUtils.throwIf(UserRoleEnum.BAN.equals(mustUserRoleEnum), ErrorCode.NO_AUTH_ERROR);
            // 必须有管理员权限
            ThrowUtils.throwIf(UserRoleEnum.ADMIN.equals(mustUserRoleEnum) && !mustRole.equals(userRole),
                    ErrorCode.NO_AUTH_ERROR);
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

