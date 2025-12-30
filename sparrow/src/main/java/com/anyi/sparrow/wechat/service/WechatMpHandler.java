package com.anyi.sparrow.wechat.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.user.service.UserAccountService;
import com.anyi.sparrow.assist.system.service.SysDictService;
import com.anyi.sparrow.base.security.Constants;
import com.anyi.sparrow.common.utils.HttpClientUtil;
import com.anyi.sparrow.wechat.config.WxMiniProperties;
import com.anyi.sparrow.wechat.enums.MpEvent;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author peng can
 * @date 2022/12/1 22:11
 * 公众号处理类
 */
@Component
@Slf4j
public class WechatMpHandler {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private WxMiniProperties wxMiniProperties;


    /**
     * 用户事件回调
     *
     * @param inMessage
     * @return
     */
    public void handler(WxMpXmlMessage inMessage) {
        MpEvent mpEvent = MpEvent.valueOf(inMessage.getEvent().toUpperCase(Locale.ROOT));
        if (mpEvent == MpEvent.UNSUBSCRIBE) {
            log.info("用户取消关注不处理，openid：{}", inMessage.getFromUser());
            return;
        }
        if (mpEvent == MpEvent.TEMPLATESENDJOBFINISH) {
            log.info("模板消息推送，不处理 - {}", JSONUtil.toJsonStr(inMessage));
            return;
        }
        userAccountService.saveUserMpOpenId(inMessage.getFromUser(), this.getUnionID(inMessage.getFromUser()));
        String eventKey = inMessage.getEventKey();
        if (StringUtils.isBlank(inMessage.getTicket()) || StringUtils.isBlank(eventKey)) {
            log.info("非扫码关注公众号，或者二维码参数为，不处理，eventKey：{}，ticket：{}", eventKey, inMessage.getTicket());
            return;
        }
        Long orderId = this.getOrderId(eventKey);
        if (Objects.isNull(orderId)) {
            log.info("二维码参数错误，不处理，eventKey：{}", eventKey);
           return;
        }
       /* EtcOrder etcOrder = etcOrderService.getById(orderId);
        if (Objects.isNull(etcOrder)) {
           return;
        }
        this.sendMiniProgramPage(etcOrder, inMessage.getFromUser(), orderId);
        try {
            contractAccountService.getAndRegisterBestSign(orderId);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }*/
    }

    /**
     * 推送小程序卡片消息
     */
   /* private void sendMiniProgramPage(EtcOrder etcOrder, String toUserOpenId, Long orderId) {
        UserAccount userAccount = userAccountService.selectByMpOpenId(toUserOpenId);

        if (!etcOrder.getInnerStatus().equals(InnerStatus.CAR_INFO.getCode()) &&
                !etcOrder.getInnerStatus().equals(InnerStatus.CONTRACT.getCode()) &&
                Objects.nonNull(etcOrder.getUserId()) && !etcOrder.getUserId().equals(userAccount.getId())) {
            return;
        }
        try {
            log.info("发送客服小程序卡片消息");
            wxMpService.getKefuService().sendKefuMessage(WxMpKefuMessage
                    .MINIPROGRAMPAGE()
                    .title(wxMiniProperties.getTitle())
                    .toUser(toUserOpenId)
                    .thumbMediaId(this.getMediaId())
                    .appId(wxMiniProperties.getAppId())
                    .pagePath(wxMiniProperties.getPagePath() + "?orderId=" + orderId).build());
        } catch (WxErrorException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }*/

    /**
     * 回复默认消息
     *
     * @param inMessage
     * @return
     */
    private WxMpXmlOutTextMessage defaultMessage(WxMpXmlMessage inMessage) {
        WxMpXmlOutTextMessage message = new WxMpXmlOutTextMessage();
        message.setContent("你好，欢迎关注安逸E出行！");
        message.setCreateTime(System.currentTimeMillis() / 1000);
        message.setToUserName(inMessage.getFromUser());
        message.setFromUserName(inMessage.getToUser());
        return message;
    }

    /**
     * 获取用户的unionId
     *
     * @param openId 用户的 openId
     * @return
     */
    public String getUnionID(String openId) {
        try {
            WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId);
            log.info("获取公众号用户信息返回：{}", JSON.toJSONString(wxMpUser));
            return wxMpUser.getUnionId();
        } catch (WxErrorException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new BusinessException(BizError.MP_USER_INFO);
        }
    }

    /**
     * 获取临时二维码 有效期1天
     *
     * @param param 二维码参数
     * @return
     */
    public String generateTicket(String param) {
        try {
            WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(param, 24 * 60 * 60);
//            return wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket(), Boolean.TRUE);
            return wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
        } catch (WxErrorException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new BusinessException(BizError.MP_QRCODE);
        }
    }

    /**
     * 获取临时素材id
     *
     * @return mediaId
     * @throws WxErrorException
     */
    public String getMediaId() throws WxErrorException {
        String mediaId = redisTemplate.opsForValue().get(Constants.MEDIA_ID_KEY);
        if (StringUtils.isBlank(mediaId)) {
            //获取素材库相关实现
            WxMpMaterialService materialService = wxMpService.getMaterialService();

            //下载图片
            String imageUrl = sysDictService.getByName("media_image_url");
            byte[] bytes = HttpClientUtil.sendGetByte(imageUrl);
            assert bytes != null;
            //上传临时素材
            WxMediaUploadResult wxMediaUploadResult = materialService.mediaUpload(WxConsts.KefuMsgType.IMAGE, ".jpg", new ByteArrayInputStream(bytes));
            //获取素材ID
            mediaId = wxMediaUploadResult.getMediaId();
            redisTemplate.opsForValue().set(Constants.MEDIA_ID_KEY, mediaId, 2, TimeUnit.DAYS);
        }

        return mediaId;
    }

    private Long getOrderId(String eventKey) {
        Long orderId = null;
        try {
            orderId = Long.parseLong(eventKey.replaceAll("qrscene_", ""));
        } catch (NumberFormatException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return orderId;
    }
}