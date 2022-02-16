package com.fengxue.javax_backend.util.Response;


public class Response {

    private static String SUCCESS="success";

    private static String FAIL="fail";

    //创建不同场景下的返回结果对象
    public static <T> ResponseResult<T> createOkResp()
    {
        return new ResponseResult<T>(StatusCode.SUCCESS.code,SUCCESS,null);
    }

    public static <T> ResponseResult<T> createOkResp(T data)
    {

        return new ResponseResult<T>(StatusCode.SUCCESS.code,SUCCESS,data);
    }

    public static <T> ResponseResult<T> createOkResp(String msg,T data)
    {
        return new ResponseResult<T>(StatusCode.SUCCESS.code,msg,data);
    }

    public static <T> ResponseResult<T> createOkResp(String msg)
    {

        return new ResponseResult<T>(StatusCode.SUCCESS.code,msg,null);
    }

    public static <T> ResponseResult<T> createFailResp()
    {

        return new ResponseResult<T>(StatusCode.SERVER_ERROR.code,FAIL,null);
    }

    public static <T> ResponseResult<T> createFailResp(String msg)
    {
        return new ResponseResult<T>(500,msg,null);
    }

    public static <T> ResponseResult<T> createFailResp(int code,String msg)
    {
        return new ResponseResult<T>(code,msg,null);
    }

    public static <T> ResponseResult<T> createResp(int code,String msg,T data)
    {
        return new ResponseResult<T>(code,msg,data);
    }



}
