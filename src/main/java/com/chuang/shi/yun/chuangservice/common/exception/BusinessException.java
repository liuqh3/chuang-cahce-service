package com.chuang.shi.yun.chuangservice.common.exception;

import com.chuang.shi.yun.chuangservice.enums.RetCode;


/**
 * @author ：liuqh3
 * @description：业务异常
 * @modified By：
 * @version: 1.0.0
 */
public class BusinessException extends RuntimeException {
    // 默认返回错误代码
    private RetCode retCode = RetCode.EXCEPTION;

    /**
     * 构造方法
     */
    public BusinessException() {
        super(RetCode.EXCEPTION.getMsg());
    }

    /**
     * 构造方法
     *
     * @param message 提示信息
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * 构造方法
     *
     * @param message 提示信息
     * @param e       异常对象
     */
    public BusinessException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * 构造方法
     *
     * @param e 异常对象
     */
    public BusinessException(Throwable e) {
        super(RetCode.EXCEPTION.getMsg(), e);
    }

    /**
     * 构造方法
     *
     * @param retCode 返回代码
     */
    public BusinessException(RetCode retCode) {
        super(retCode.getMsg());
        this.retCode = retCode;
    }

    /**
     * 构造方法
     *
     * @param retCode 返回代码
     * @param message 提示信息
     */
    public BusinessException(RetCode retCode, String message) {
        super(message);
        this.retCode = retCode;
    }

    /**
     * 构造方法
     *
     * @param retCode 返回代码
     * @param e       异常对象
     */
    public BusinessException(RetCode retCode, Throwable e) {
        super(retCode.getMsg(), e);
        this.retCode = retCode;
    }

    /**
     * 构造方法
     *
     * @param retCode 返回代码
     * @param message 提示信息
     * @param e       异常对象
     */
    public BusinessException(RetCode retCode, String message, Throwable e) {
        super(message, e);
        this.retCode = retCode;
    }

    public RetCode getRetCode() {
        return retCode;
    }

    public void setRetCode(RetCode retCode) {
        this.retCode = retCode;
    }

}

