package com.zjh.wxchat._bean._wx;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * wx访问令牌
 *
 * @author 张俊虎
 * @date 2023/04/11
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WxAccessToken implements Serializable {
    /**
     * 访问令牌
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String access_token;
    /**
     * 凭证有效时间，单位：秒
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long expires_in;
    /**
     * 错误码
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errcode;
    /**
     * 错误信息
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errmsg;
}
