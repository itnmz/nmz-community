package com.itnmz.community.auth.interceptor;

import com.itnmz.community.auth.context.UserContext;
import com.itnmz.community.common.result.Result;
import com.itnmz.community.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * 登录校验拦截器。
 *
 * 校验通过后同时做两件事：
 *  1. 把 userId 写入 request 属性 —— Controller 用
 *     {@code @RequestAttribute(LoginInterceptor.ATTR_USER_ID) Long userId} 显式取；
 *  2. 写入 {@link UserContext} —— Service 层可以直接 UserContext.getUserId()，
 *     不用一层层传参。
 *
 * 两者都保留，是因为它们各有取舍：request 属性零清理负担但必须通过参数传递，
 * ThreadLocal 随处可读但必须有人负责清理（见 afterCompletion）。
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /** 校验通过后写入 request 的用户 ID 属性名 */
    public static final String ATTR_USER_ID = "loginUserId";

    private static final String AUTH_HEADER = "Authorization"; //授权


    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public LoginInterceptor(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        // CORS 预检请求不带 Authorization，必须放行，否则浏览器直接报跨域失败
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        /** 检查Token是否存在 */
        String token = jwtUtil.resolveToken(request.getHeader(AUTH_HEADER));
        if (token == null || token.isBlank()) {
            writeUnauthorized(response, "未登录");
            return false;
        }

        //
        try {
            Long userId = jwtUtil.getUserId(token);//从jwt中获取userId
            //挂载到本次HTTP请求对象(HttpServletRequest)的生命周期中(只能在拿到 request 对象的 Web 层使用)
            request.setAttribute(ATTR_USER_ID, userId);
            //用户ID写入当前线程上下文(跨线程会丢失)
            UserContext.setUserId(userId);
            return true;
        } catch (ExpiredJwtException e) {
            writeUnauthorized(response, "登录已过期，请重新登录");
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            // 签名不匹配、格式损坏、subject 不是数字，都归到这里
            writeUnauthorized(response, "登录状态无效");
            return false;
        }
    }

    /**
     * 请求结束（无论成功、抛异常）都会执行，是清理 ThreadLocal 的唯一可靠位置。
     * 注意：只有 preHandle 返回 true 时才会进入 afterCompletion，
     * 所以失败分支里没有 set 过 UserContext，也就不需要额外清理。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserContext.clear();
    }

    /**
     * 手动构造并返回 401 未授权 JSON 响应 —— 拦截器返回 false 时异常处理器不参与(无法像正常接口返回JSON格式)。
     */
    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        //设置状态码
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), Result.fail(401, message));
    }
}
