package com.fengxue.javax_backend.util.Response;


public enum StatusCode {

    //http定义好的状态码
    SUCCESS(200),

    SERVER_ERROR(500),

    URL_NOT_FOUND(404),

    //自定义  状态码
    NOT_ALLOWRD_REG(1001);

    public int code;

    StatusCode(int code)
    {
        this.code=code;
    }

}
