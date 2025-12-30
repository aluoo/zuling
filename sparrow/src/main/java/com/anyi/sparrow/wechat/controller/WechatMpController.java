package com.anyi.sparrow.wechat.controller;

import com.anyi.sparrow.wechat.service.WechatMpHandler;
import com.anyi.common.domain.param.Response;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 公众号相关接口
 *
 * @author peng can
 * @date 2022/12/1 21:20
 */
@RestController
@RequestMapping("/wechat/mp")
@Slf4j
public class WechatMpController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WechatMpHandler wechatMpHandler;

    /***
     * 微信服务器触发get请求用于检测签名
     * @return
     */
    @GetMapping("/entry")
    public void checkSignature(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String echostr = request.getParameter("echostr");
        log.info(echostr);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(echostr);
        writer.flush();
        writer.close();
    }


    /**
     * 微信公众号回调
     */
    @PostMapping(value = "/entry")
    public void entryCallback(HttpServletRequest request) throws IOException {

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        String requestBody = this.getBody(request);
        log.info("接收微信请求：[signature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                signature, timestamp, nonce, requestBody);

        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        //String out = "";
        try {
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            this.wechatMpHandler.handler(inMessage);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        //log.info("组装回复信息：[{}]", out);
    }

    /***
     * 生成带参数临时二维码
     * @return
     */
    @GetMapping("/qrcode/url")
    public Response<String> qrcode(@RequestParam("param") String param) {
        String qrcodeUrl = wechatMpHandler.generateTicket(param);
        return Response.ok(qrcodeUrl);
    }


    private String getBody(HttpServletRequest request) throws IOException {
        InputStream is = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return sb.toString();
    }

}
