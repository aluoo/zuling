package com.anyi.sparrow.wechat.enums;

/**
 * @author peng can
 * @date 2022/12/1 22:20
 */
public enum MpEvent {
    /**
     * 关注
     */
    SUBSCRIBE,
    /**
     * 取消关注
     */
    UNSUBSCRIBE,
    /**
     * 用户扫码，已关注
     */
    SCAN,
    /**
     * 模板消息推送成功
     */
    TEMPLATESENDJOBFINISH,
}