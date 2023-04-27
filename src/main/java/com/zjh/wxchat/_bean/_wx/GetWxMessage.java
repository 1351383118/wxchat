package com.zjh.wxchat._bean._wx;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.*;

/**
 * 获取信息
 *
 * @author 张俊虎
 * @date 2023/04/13
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public class GetWxMessage {
    /**
     * 开发者微信号
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    @XmlElement(name = "FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    @XmlElement(name = "CreateTime")
    private Long createTime;
    /**
     * 消息类型
     */
    @XmlElement(name = "MsgType")
    private String msgType;
    /**
     * 消息id，64位整型
     */
    @XmlElement(name = "MsgId")
    private Integer msgId;
    /**
     * 消息的数据ID（消息如果来自文章时才有）
     */
    @XmlElement(name = "MsgDataId")
    private String msgDataId;
    /**
     * 多图文时第几篇文章，从1开始（消息如果来自文章时才有）
     */
    @XmlElement(name = "Idx")
    private String idx;

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
    static class media extends GetWxMessage{
        /**
         * 媒体id，可以调用获取临时素材接口拉取数据
         */
        @XmlElement(name = "MediaId")
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
    public static class TextMessage extends GetWxMessage{
        /**
         * 文本消息内容
         */
        @XmlElement(name = "Content")
        private String content;
    }

    /**
     * 图片信息
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
    public static class PictureMessage extends media{
        /**
         * 图片链接（由系统生成）
         */
        @XmlElement(name = "PicUrl")
        private String picUrl;
    }

    /**
     * 语音信息
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
    public static class VoiceMessage extends media{
        /**
         * 语音格式，如amr，speex等
         */
        @XmlElement(name = "Format")
        private String format;
    }

    /**
     * 视频信息/小视频消息
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
         * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据
         */
        @XmlElement(name = "ThumbMediaId")
        private String thumbMediaId;
    }

    /**
     * 地理位置信息
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
    public static class GeographicalPositionInformation extends GetWxMessage{
        /**
         * 地理位置纬度
         */
        @XmlElement(name = "Location_X")
        private String locationX;
        /**
         * 地理位置经度
         */
        @XmlElement(name = "Location_Y")
        private String locationY;
        /**
         * 地图缩放大小
         */
        @XmlElement(name = "Scale")
        private String scale;
        /**
         * 地理位置信息
         */
        @XmlElement(name = "Label")
        private String label;
    }

    /**
     * 链接消息
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
    public static class LinksToNews extends GetWxMessage{
        /**
         * 消息标题
         */
        @XmlElement(name = "Title")
        private String title;
        /**
         * 消息描述
         */
        @XmlElement(name = "Description")
        private String description;
        /**
         * 消息链接
         */
        @XmlElement(name = "Url")
        private String url;
    }
}
