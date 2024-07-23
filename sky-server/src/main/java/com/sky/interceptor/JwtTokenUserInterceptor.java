package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2、校验令牌
        // 尝试验证JWT令牌的有效性
        try {
            // 记录日志，显示正在验证的JWT令牌
            log.info("jwt校验:{}", token);
            // 使用预定义的管理员密钥解析JWT令牌
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            // 从声明中提取员工ID
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            // 记录日志，显示当前员工ID
            log.info("当前用户id：", userId);
            BaseContext.setCurrentId(userId);
            // 如果令牌验证成功，返回true
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            // 如果令牌验证失败，设置响应状态为401（未授权）并返回false
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }

    }
}
