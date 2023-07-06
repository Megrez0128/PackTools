package com.zulong.web.utils;

public enum ResultCode  {

    /**
     * 成功状态码
     */
    SUCCESS(0, "SUCCESS"),

    /**
     * 参数错误：10001-19999
     */
    PARAM_IS_INVALID(10001, "PARAM_IS_INVALID"),  //username token 分页索引等参数错误

    /**
     * 用户权限错误：20001-29999
     */
    USER_NOT_EXIST(20001, "USER_NOT_EXIST"), //用户不存在

    /**
     * 数据库操作： 40001-49999
     * */
    ;

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

}
