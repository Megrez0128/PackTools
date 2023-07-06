package com.zulong.web.utils;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * ControllerAdvice会把basePackages下的Controller方法返回值拦截，统一处理返回值和异常
 * 错误情况和无参数成功的情况手动调用Result,
 * 带数据的成功返回直接返回数据即可
 */
@ControllerAdvice(basePackages = "com.zulong.web")
public class ResponseHandler implements ResponseBodyAdvice<Object>
{

    /**
     * 是否支持advice功能
     * ture=支持，false=不支持
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass)
    {
        return true;
    }

    /**
     * 处理response的具体业务方法
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            org.springframework.http.server.ServerHttpRequest request,
            org.springframework.http.server.ServerHttpResponse response)
    {

        if (body instanceof Result)
        {
            return body;
        }
        if (body instanceof String)
        {
            return JsonUtil.object2Json(Result.suc(body));
        }
        return Result.suc(body);
    }
}
