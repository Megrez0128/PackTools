package com.zulong.web.exception;

import com.zulong.web.utils.ResultCode;

public class ClientArgIllegalException extends com.zulong.web.exception.BaseException
{
    public ClientArgIllegalException(int resultCode, String message)
    {
        super(resultCode, message);
    }

    public ClientArgIllegalException(ResultCode code)
    {
        super(code.code(), code.message());
    }
}
