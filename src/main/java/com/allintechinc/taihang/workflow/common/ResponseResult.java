package com.allintechinc.taihang.workflow.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * @param <T>
 * @author sundp
 */
@Data
@NoArgsConstructor
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = -6128470500971010812L;
    private Integer errorCode;
    private Integer result;
    private Boolean success = true;
    private String message;
    private Long timestamp = System.currentTimeMillis();
    private T body;

    public static final Integer SUCCESS = 0;
    public static final Integer ERROR = -1;


    public ResponseResult(T body) {
        this.result = 1;
        this.body = body;
    }

    public ResponseResult(Integer errorCode, String message) {
        this.result = 0;
        this.success = false;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static <T> ResponseResult<T> success(T body, String msg) {
        return new ResponseResult<T>().body(body).code(SUCCESS).message(msg);
    }

    public static <T> ResponseResult<T> success(T body) {
        return new ResponseResult<T>().body(body).code(SUCCESS).message("success");
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<T>().body(null).code(SUCCESS).message("success");
    }

    public static <T> ResponseResult<T> success(T body, Integer code, String msg) {
        return new ResponseResult<T>().body(body).code(code).message(msg);
    }

    public static <T> ResponseResult<T> error(T body, Integer code, String msg) {
        return new ResponseResult<T>().body(body).code(code).message(msg);
    }

    public static <T> ResponseResult<T> error(Integer code, String msg) {
        return new ResponseResult<T>().code(code).message(msg);
    }

    public static <T> ResponseResult<T> error(String msg) {
        return new ResponseResult<T>().code(ERROR).message(msg);
    }

    private ResponseResult<T> message(String msg) {
        this.message = msg;
        return this;
    }

    private ResponseResult<T> code(Integer code) {
        this.errorCode = code;
        this.success = Objects.equals(code, SUCCESS);
        return this;
    }

    private ResponseResult<T> body(T body) {
        this.body = body;
        return this;
    }

    public boolean ok() {
        this.success = true;
        return  Objects.equals(errorCode, SUCCESS);
    }
}