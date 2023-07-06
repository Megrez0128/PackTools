package com.zulong.web.utils;

import com.zulong.web.exception.ClientArgIllegalException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler
{
    /**
     * 默认全局异常处理。
     * 
     * @param e
     *            the e
     * @return ResultData
     */
    @ExceptionHandler(Exception.class)
    public Result<String> exception(Exception e)
    {
        com.zulong.web.log.LoggerManager.logger().info("[RestHandler]Exception@" + e.getStackTrace()[0], e);
        return Result.fail(500, e.getMessage());
    }

    /**
     * 参数错误异常
     * 
     * @param e
     *            the e
     * @return ResultData
     */
    @ExceptionHandler(ClientArgIllegalException.class)
    public Result<String> ClientArgIllegalException(ClientArgIllegalException e)
    {
        com.zulong.web.log.LoggerManager.logger().info(String.format(
                "[RestHandler]ClientArgIllegal@" + e.getStackTrace()[0] + "|result_code=%d", e.getResultCode()));
        return Result.fail(e.getResultCode(), e.getMessage());
    }

}
