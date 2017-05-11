package com.ugym.admin.common;

/**
 * @author zheng.xu
 * @since 2017-05-11
 */
public class Result {
    private int code;
    private String message;
    private Object Data;

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public static Result ok() {
        return new Result(200, "");
    }


    public static Result ok(Object data) {
        return new Result(200, data);
    }
    public Result(int code, Object data) {
        this.code = code;
        Data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }
}
