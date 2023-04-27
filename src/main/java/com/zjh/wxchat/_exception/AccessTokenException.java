package com.zjh.wxchat._exception;

/**
 * 访问令牌异常
 *
 * @author 张俊虎
 * @date 2023/04/12
 */
public class AccessTokenException extends RuntimeException{
    /**
     * 访问令牌异常
     */
    public AccessTokenException(){
        super("获取访问令牌失败");
    }

    /**
     * 访问令牌异常
     *
     * @param message 异常信息
     */
    public AccessTokenException(String message){
        super("获取访问令牌失败:" + message);
    }
}
