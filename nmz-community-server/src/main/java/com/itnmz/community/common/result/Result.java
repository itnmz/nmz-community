package com.itnmz.community.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回体。
 *
 * code 沿用 HTTP 语义：200 成功、400 参数错误、401 未登录、403 无权限、500 服务端异常。
 * 前端只认 code，不要再靠 HTTP 状态码做分支判断。
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 业务状态码 */
    private Integer code;

    /** 提示信息，可直接展示给用户 */
    private String message;

    /** 业务数据，失败时为 null */
    private T data;

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    //1.成功不返回数据
    public static <T> Result<T> ok() {
        return new Result<>(200, "success", null);
    }

    //2.成功并返回数据
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    //500响应码，服务器错误
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }

    //相应其他错误码
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
