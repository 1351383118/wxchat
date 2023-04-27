package com.zjh.wxchat._controller;

import com.zjh.wxchat._ase.AesException;
import com.zjh.wxchat._ase.WXBizMsgCrypt;
import com.zjh.wxchat._bean._wx.GetWxMessage;
import com.zjh.wxchat._bean._wx.StaticInformation;
import com.zjh.wxchat._service.ChatService;
import com.zjh.wxchat._utils.WxUtils;
import com.zjh.wxchat._utils.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

/**
 * chat
 *
 * @author 张俊虎
 * @date 2023/04/11
 */
@RestController
@RequestMapping({"/wx/"})
public class ChatController {
    /**
     * 静态信息
     */
    @Resource
    private StaticInformation staticInformation;
    /**
     * 聊天服务
     */
    @Resource
    private ChatService chatService;
    /**
     * 线程池
     */
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * 日志
     */
    private Logger log = LoggerFactory.getLogger("chat-controller");
    /**
     * 消息id
     */
    private LinkedHashMap<Integer,String> msgIdHashMap = new LinkedHashMap<>();

    /**
     * 验证消息的确来自微信服务器
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return {@link String}
     */
    @GetMapping()
    @ResponseBody
    public String authGet(@RequestParam(name = "signature") String signature,
                          @RequestParam(name = "timestamp") String timestamp,
                          @RequestParam(name = "nonce") String nonce,
                          @RequestParam(name = "echostr") String echostr) {
        log.info("接收到验证参数:{},{},{},{}", signature, timestamp, nonce, echostr);
        return WxUtils.checkSignature(staticInformation.getToken(),timestamp,nonce,signature) ? echostr : "校验失败";
    }

    /**
     * 发送消息
     * todo 目前消息是加密模式
     * @param request  请求
     * @param response 响应
     */
    @PostMapping
    public void sendMessage(HttpServletRequest request, HttpServletResponse response){
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(staticInformation.getToken(), staticInformation.getEncodingAESKey(), staticInformation.getAppId());
            //从request获取数据
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String msgSignature = request.getParameter("msg_signature");
            String remoteAddr = request.getRemoteAddr();
            //解密、转化
            GetWxMessage getWxMessage = XmlUtils.xmlToGetMessage(pc.decryptMsg(msgSignature, timestamp, nonce, XmlUtils.getXmlData(request)));
            //重试的消息排重
            if (!ObjectUtils.isEmpty(getWxMessage.getMsgId())){
                if (ObjectUtils.isEmpty(msgIdHashMap.get(getWxMessage.getMsgId()))){
                    msgIdHashMap.put(getWxMessage.getMsgId(),remoteAddr);
                }else {
                    log.error("超时重发消息id:{}",getWxMessage.getMsgId());
                    return;
                }
            }
            //回复
            PrintWriter writer = response.getWriter();
            writer.write(pc.encryptMsg(XmlUtils.getMessageToXml(chatService.chat(getWxMessage)), timestamp, nonce));
            writer.close();
            if (!ObjectUtils.isEmpty(getWxMessage.getMsgId())){
                msgIdHashMap.remove(getWxMessage.getMsgId());
            }
        } catch (AesException | IOException exception) {
            log.error(exception.getMessage());
        }
    }
}
