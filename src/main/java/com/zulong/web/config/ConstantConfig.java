package com.zulong.web.config;

public class ConstantConfig {

    /**
     * 接口返回值使用，可能出现多种情况
     */
    public static final int DELETE_SUCCESS = 100;
    public static final int DELETE_HAVE_INSTANCE = 200;
    public static final int DELETE_DATABASE_WRONG = 300;

    public static final int TOKEN_VERIFY_FAILED = 40600;

    public static final int RETURN_SUCCESS = 20000;
    public static final int RETURN_EXIST_INSTANCE = 30300;  // 在删除Flow时使用
    public static final int RETURN_DATABASE_WRONG = 30400;
    public static final int RETURN_PARAMS_WRONG = 40000;
    public static final int RETURN_NO_AUTHORITY = 40100;
    public static final int RETURN_NO_OBJECT = 40600;  // 修改用户/组时被修改的对象不存在
    public static final int RETURN_SERVER_WRONG = 50000;
    //用户不在组中

    //该组没有该meta的权限

    //该组没有该flow的权限

    public static final boolean DEFAULT_UPDATE_AUTHORITY = false;
    public static final boolean DEFAULT_DELETE_AUTHORITY = false;

    public static final int ADMINISTRATOR_GROUP_ID = 0;

    public static final String CONST_INIT_END_TIME = "CONST_INIT_END_TIME";
}
