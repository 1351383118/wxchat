package com.zjh.wxchat._bean._wx;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 静态信息
 *
 * @author 张俊虎
 * @date 2023/04/18
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class StaticInformation {
    /**
     * 令牌
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    /**
     * 第三方用户唯一凭证
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String appId;
    /**
     * 第三方用户唯一凭证密钥
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String appSecret;
    /**
     * 消息加解密密钥
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String encodingAESKey;
    /**
     * 获取url访问令牌
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String getAccessTokenUrl;
}
