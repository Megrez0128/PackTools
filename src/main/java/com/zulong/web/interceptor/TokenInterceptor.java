package com.zulong.web.interceptor;

import com.zulong.web.entity.User;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.zulong.web.utils.TokenUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.zulong.web.config.ConstantConfig.TOKEN_VERIFY_FAILED;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        // 如果是 /auth/user/login 请求，则放行
        if ("/auth/user/login".equals(path)) {
            return true;
        }
//        if ("/auth/user/gettoken".equals(path)) {
//            return true;
//        }
        if("/saml/login".equals(path)) {
            return true;
        }
        if("/saml/auth".equals(path)) {
            return true;
        }
        //跨域请求会首先发一个option请求，直接返回正常状态并通过拦截器
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (token!=null){
//            LoggerManager.logger().info(String.format(
//                    "[com.zulong.web.interceptor.TokenInterceptor]TokenInterceptor.preHandle@token!=null|token=%s",token));
            String curr_user_id= TokenUtils.verify(token);
            if (curr_user_id != null){
//                LoggerManager.logger().info(String.format(
//                        "[com.zulong.web.interceptor.TokenInterceptor]TokenInterceptor.preHandle@通过拦截器|result=%s", true));
                if(curr_user_id.equals("admin")){
                    return true;
                }
                User curr_user = userService.getUserByUserId(curr_user_id);
                if(curr_user != null){
                    TokenUtils.setAdmin(curr_user.isAdmin());
                    return true;
                }

            }
        }
        response.setContentType("application/json; charset=utf-8");
        try {
            Map<String,Object> json=new HashMap<>();
            json.put("msg","token verify fail");
            json.put("code",TOKEN_VERIFY_FAILED);
            response.getWriter().append(buildJsonString(json));
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.interceptor.TokenInterceptor]TokenInterceptor.preHandle@认证失败，未通过拦截器|token=%s",token));
        } catch (Exception e) {
            return false;
        }
        /**
         * 还可以在此处检验用户存不存在等操作
         */
        return false;

    }
    // 拼接JSON字符串方法
    private static String buildJsonString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                sb.append("\"").append(entry.getValue()).append("\",");
            } else {
                sb.append(entry.getValue()).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String token = null;
//        String username = null;
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            token = authorizationHeader.substring(7);
//            username = jwtTokenUtil.getUsernameFromToken(token);
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
//
//            if (jwtTokenUtil.validateToken(token, userDetails)) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken
//                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
//        return true;
//    }
}
