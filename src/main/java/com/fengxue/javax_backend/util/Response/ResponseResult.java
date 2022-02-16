package com.fengxue.javax_backend.util.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {

    public int code; //返回状态码

    private String msg; //返回描述信息

    private T data; //返回内容体


}
