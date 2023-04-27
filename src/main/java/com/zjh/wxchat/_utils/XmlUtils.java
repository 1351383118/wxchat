package com.zjh.wxchat._utils;

import com.alibaba.fastjson2.JSON;
import com.zjh.wxchat._bean._wx.GetWxMessage;
import com.zjh.wxchat._bean._wx.SendWxMessage;
import com.zjh.wxchat._enum.MessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

/**
 * 获取xml数据
 *
 * @author 张俊虎
 * @date 2023/04/20
 */
@Slf4j
public class XmlUtils {
    /**
     * 获取xml数据 字符串
     *
     * @param request 请求
     * @return {@link String}
     */
    public static String getXmlData(HttpServletRequest request){
        try {
            request.setCharacterEncoding("utf-8");
            ServletInputStream inputStream = request.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String content;
            while ((content = bufferedReader.readLine()) != null){
                stringBuilder.append(content);
            }
            bufferedReader.close();
            inputStream.close();
            return stringBuilder.toString();
        }catch (IOException exception){
            log.error(exception.getMessage());
        }
        return null;
    }

    /**
     * 将xml字符串解析为GetWxMessage对象
     *
     * @param xml xml
     * @return {@link GetWxMessage}
     */
    public static GetWxMessage xmlToGetMessage(String xml){
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            SAXReader saxReader = new SAXReader();
            Document read = saxReader.read(new StringReader(xml));
            Element rootElement = read.getRootElement();
            readEle(rootElement,hashMap);
            if (!ObjectUtils.isEmpty(hashMap)){
                switch (Objects.requireNonNull(MessageEnum.getMessageEnum(hashMap.get("msgType")))){
                    case TEXT:
                        return JSON.parseObject(JSON.toJSONString(hashMap), GetWxMessage.TextMessage.class);
                    case IMAGE:
                        return JSON.parseObject(JSON.toJSONString(hashMap), GetWxMessage.PictureMessage.class);
                    case VOICE:
                        return JSON.parseObject(JSON.toJSONString(hashMap), GetWxMessage.VoiceMessage.class);
                    case VIDEO:
                    case SHORTVIDEO:
                        return JSON.parseObject(JSON.toJSONString(hashMap), GetWxMessage.VideoMessage.class);
                    case LOCATION:
                        return JSON.parseObject(JSON.toJSONString(hashMap), GetWxMessage.GeographicalPositionInformation.class);
                    case LINK:
                        return JSON.parseObject(JSON.toJSONString(hashMap), GetWxMessage.LinksToNews.class);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        throw new RuntimeException("格式不支持");
    }

    /**
     * xml逐节点解析
     *
     * @param e       e
     * @param hashMap 散列映射
     */
    private static void readEle(Element e,HashMap<String, String> hashMap){
        //判断是否有复合内容
        if(e.hasMixedContent()){
            //输出该节点的名字，对他的子节点继续进行判断
            Iterator<Element> it = e.elementIterator();
            while (it.hasNext()) {
                Element arrrName = (Element) it.next();
                //递归
                readEle(arrrName,hashMap);
            }
        }else{
            hashMap.put(StringUtils.uncapitalize(e.getName()),e.getTextTrim());
            //如果没有复合内容，就可以输出了
        }
    }

    /**
     * 将sendWxMessage对象转为xml字符串
     *
     * @param sendWxMessage 发送wx信息
     * @return {@link String}
     */
    public static String getMessageToXml(SendWxMessage sendWxMessage){
        // 创建document
        Document document = DocumentHelper.createDocument();
        //创建根节点
        Element xml = document.addElement("xml");
        //添加公共属性
        xml.addElement("ToUserName").addCDATA(sendWxMessage.getToUserName());
        xml.addElement("FromUserName").addCDATA(sendWxMessage.getFromUserName());
        xml.addElement("CreateTime").addCDATA(sendWxMessage.getCreateTime().toString());
        xml.addElement("MsgType").addCDATA(sendWxMessage.getMsgType());
        switch (Objects.requireNonNull(MessageEnum.getMessageEnum(sendWxMessage.getMsgType()))){
            case TEXT:
                xml.addElement("Content").addCDATA(JSON.parseObject(JSON.toJSONString(sendWxMessage), SendWxMessage.TextMessage.class).getContent());
                break;
            case IMAGE:
                xml.addElement("Image").addElement("MediaId").addCDATA(JSON.parseObject(JSON.toJSONString(sendWxMessage), SendWxMessage.PictureMessage.class).getMediaId());
                break;
            case VOICE:
                xml.addElement("Voice").addElement("MediaId").addCDATA(JSON.parseObject(JSON.toJSONString(sendWxMessage), SendWxMessage.VoiceMessage.class).getMediaId());
                break;
            case VIDEO:
                Element video = xml.addElement("Video");
                video.addElement("MediaId").addCDATA(JSON.parseObject(JSON.toJSONString(sendWxMessage), SendWxMessage.VideoMessage.class).getMediaId());
                video.addElement("Title").addCDATA(JSON.parseObject(JSON.toJSONString(sendWxMessage), SendWxMessage.VideoMessage.class).getTitle());
                video.addElement("Description").addCDATA(JSON.parseObject(JSON.toJSONString(sendWxMessage), SendWxMessage.VideoMessage.class).getDescription());
                break;
            case MUSIC:
                Element music = xml.addElement("Music");
                music.addElement("Title").addCDATA(JSON.parseObject(JSON.toJSONString(sendWxMessage), SendWxMessage.MusicInformation.class).getTitle());
                music.addElement("Description").addCDATA(JSON.parseObject(JSON.toJSONString(sendWxMessage), SendWxMessage.MusicInformation.class).getDescription());
                music.addElement("MusicUrl").addCDATA(JSON.parseObject(JSON.toJSONString(sendWxMessage), SendWxMessage.MusicInformation.class).getHQMusicUrl());
                music.addElement("HQMusicUrl").addCDATA(JSON.parseObject(JSON.toJSONString(sendWxMessage), SendWxMessage.MusicInformation.class).getMusicURL());
                music.addElement("ThumbMediaId").addCDATA(JSON.parseObject(JSON.toJSONString(sendWxMessage), SendWxMessage.MusicInformation.class).getThumbMediaId());
                break;
            case NEWS:
                //todo 图文是多个，后续做
                break;
        }
        return xml.asXML();
    }
}
