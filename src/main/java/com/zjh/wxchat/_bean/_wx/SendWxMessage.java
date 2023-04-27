package com.zjh.wxchat._bean._wx;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 返回消息
 *
 * @author 张俊虎
 * @date 2023/04/13
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendWxMessage {
    /**
     * 接收方帐号（收到的OpenID）
     */
    private String toUserName;
    /**
     * 开发者微信号
     */
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    private Long createTime;
    /**
     * 消息类型
     */
    private String msgType;
    /**
     * 媒体
     *
     * @author 张俊虎
     * @date 2023/04/23
     */
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class media extends SendWxMessage{
        /**
         * 媒体id，可以调用获取临时素材接口拉取数据
         */
        private String mediaId;
    }

    /**
     * 文本消息
     *
     * @author 张俊虎
     * @date 2023/04/23
     */
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TextMessage extends SendWxMessage{
        /**
         * 文本消息内容
         */
        private String content;
    }
    /**
     * 图片信息
     *
     * @author 张俊虎
     * @date 2023/04/23
     */
    @EqualsAndHashCode(callSuper = true)
//    @AllArgsConstructor
//    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PictureMessage extends media{
    }

    /**
     * 语音信息
     *
     * @author 张俊虎
     * @date 2023/04/23
     */
    @EqualsAndHashCode(callSuper = true)
//    @AllArgsConstructor
//    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VoiceMessage extends media{
    }

    /**
     * 视频信息
     *
     * @author 张俊虎
     * @date 2023/04/23
     */
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VideoMessage extends media{
        /**
         * 视频消息的标题
         */
        private String title;
        /**
         * 视频消息的描述
         */
        private String description;
    }

    /**
     * 音乐信息
     *
     * @author 张俊虎
     * @date 2023/04/23
     */
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MusicInformation extends SendWxMessage{
        /**
         * 音乐标题
         */
        private String title;
        /**
         * 音乐描述
         */
        private String description;
        /**
         * 音乐链接
         */
        private String musicURL;
        /**
         * 高质量音乐链接，WIFI环境优先使用该链接播放音乐
         */
        private String hQMusicUrl;
        /**
         * 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
         */
        private String thumbMediaId;
    }

    /**
     * 图文消息
     *
     * @author 张俊虎
     * @date 2023/04/23
     */
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ByMessage extends SendWxMessage{
        /**
         * 图文消息个数；当用户发送文本、图片、语音、视频、图文、地理位置这六种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
         */
        private String articleCount;
        /**
         * 图文消息信息，注意，如果图文数超过限制，则将只发限制内的条数
         */
        private String articles;
        /**
         * 图文消息标题
         */
        private String title;
        /**
         * 图文消息描述
         */
        private String description;
        /**
         * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
         */
        private String picUrl;
        /**
         * 点击图文消息跳转链接
         */
        private String url;
    }
}
