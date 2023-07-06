package com.zulong.web.exception;

public class BaseException extends RuntimeException
{
    private int resultCode;

    public BaseException(int resultCode, String message)
    {
        super(message);
        this.resultCode = resultCode;
    }

    public int getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(int resultCode)
    {
        this.resultCode = resultCode;
    }
}
