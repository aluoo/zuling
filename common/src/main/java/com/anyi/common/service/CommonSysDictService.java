package com.anyi.common.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.domain.entity.CommonSysDict;
import com.anyi.common.domain.mapper.CommonSysDictMapper;
import com.anyi.common.product.domain.constants.DictPropertiesConstants;
import com.anyi.common.product.domain.constants.TreeConstants;
import com.anyi.common.result.DictMapVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/2
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class CommonSysDictService extends ServiceImpl<CommonSysDictMapper, CommonSysDict> {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public Boolean getExchangeMode(String mobile) {
        boolean result = false;
        if (StrUtil.isBlank(mobile)) {
            return result;
        }
        String exchangeModeMobiles = getValueByName("exchange_mode_mobiles");
        if (StrUtil.isBlank(exchangeModeMobiles)) {
            return result;
        }
        List<String> split = StrUtil.split(exchangeModeMobiles, TreeConstants.SPLIT_CHAR);
        if (CollUtil.isEmpty(split)) {
            return result;
        }
        return split.contains(mobile);
    }

    public Boolean getTemporaryFuncAble() {
        int defaultValue = 0;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.TEMPORARY_FUNC_ABLE)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getTemporaryFuncAble.error: {}", e.getMessage());
        }
        return defaultValue == 1;
    }

    public List<String> getInsuranceOrderRefundReasons() {
        String value = getValueByName(DictPropertiesConstants.INSURANCE_ORDER_REFUND_REASON_DICT_KEY);
        return StrUtil.isNotBlank(value)
                ? StrUtil.split(value, TreeConstants.SPLIT_CHAR)
                : getDefaultInsuranceOrderRefundReasons();
    }

    private List<String> getDefaultInsuranceOrderRefundReasons() {
        List<String> list = new LinkedList<>();
        list.add("不想要了");
        list.add("价格贵");
        list.add("服务内容不合适");
        return list;
    }

    /**
     * 获取数保订单自动上传时间 单位天
     */
    public int getInsuranceOrderAutoUploadDay() {
        int defaultValue = 7;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.INSURANCE_ORDER_AUTO_UPLOAD_DAY)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getInsuranceOrderAutoUploadDay.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    /**
     * 获取数保订单报价过期时间设置，超时关闭报价功能
     */
    public int getInsuranceOrderExpiredMinutes() {
        int defaultValue = 60 * 2;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.INSURANCE_ORDER_EXPIRED_MINUTES)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getInsuranceOrderExpiredMinutes.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    public List<String> getIndexBanner() {
        String value = getValueByName("mb_index_banner");
        return StrUtil.isNotBlank(value)
                ? StrUtil.split(value, TreeConstants.SPLIT_CHAR)
                : null;
    }

    public BigDecimal getJdlLogisticsDiscountRate() {
        BigDecimal defaultValue = BigDecimal.ZERO;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.JDL_LOGISTICS_DISCOUNT_RATE)).orElse(defaultValue.toString());
        try {
            defaultValue = new BigDecimal(value);
        } catch (Exception e) {
            log.info("CommonSysDictService.getJdlLogisticsDiscountRate.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    public List<String> getRejectQuteReason() {
        String value = getValueByName(DictPropertiesConstants.REJECT_QUOTE_REASON_DICT_KEY);
        return StrUtil.isNotBlank(value)
                ? StrUtil.split(value, TreeConstants.SPLIT_CHAR)
                : getDefaultRemarkInfo();
    }

    private List<String> getDefaultRejectQuteReason() {
        List<String> list = new LinkedList<>();
        list.add("本公司近期暂不回收此类机型");
        list.add("照片遮挡，无法给出准确报价");
        list.add("功能选项描述与图片信息不符，无法给出准确报价");
        list.add("山寨机、租赁机等，不予回收");
        list.add("无面容、原彩、指纹等核心功能照片，无法给出准确报价");
        list.add("无验机报告（沙漏/爱思截图），无法给出准确报价");
        list.add("图片过于模糊，无法给出准确报价");
        list.add("外观照片缺失，无法给出准确报价");
        list.add("无白色【关于本机】界面，无法判断屏幕问题，无法给出准确报价");
        list.add("无设备主界面相关图片，无法判断旧机来源是否合法");
        return list;
    }

    public List<String> getRemarkInfo() {
        String value = getValueByName(DictPropertiesConstants.ORDER_REMARK_DICT_KEY);
        return StrUtil.isNotBlank(value)
                ? StrUtil.split(value, TreeConstants.SPLIT_CHAR)
                : getDefaultRemarkInfo();
    }

    private List<String> getDefaultRemarkInfo() {
        List<String> list = new LinkedList<>();
        list.add("机身外观有磕碰掉漆");
        list.add("大花-屏幕明显划痕");
        list.add("屏幕外爆-外屏碎");
        list.add("轻微老化透图");
        list.add("内爆-屏幕漏液");
        list.add("无限自动重启");
        list.add("充电无反应/不良");
        list.add("震动功能异常");
        list.add("侧边有破裂");
        list.add("机身弯曲");
        list.add("有拆机/维修历史");
        list.add("机身有进水/受潮等");
        list.add("侧边有划痕");
        list.add("侧键按压无反馈/失灵");
        list.add("中控件无反应/失灵");
        list.add("国行官换机/展示机");
        list.add("港澳台/其他地区无锁");
        list.add("后盖碎裂");
        list.add("无法接通电话");
        return list;
    }

    public String getDiInsuranceOrderPaymentDescription() {
        String value = "数保服务";
        value = Optional.ofNullable(getValueByName(DictPropertiesConstants.DI_INSURANCE_ORDER_PAYMENT_DESCRIPTION)).orElse(value);
        return value;
    }

    public String getRefundPaymentDescription() {
        String value = "购机款退还";
        value = Optional.ofNullable(getValueByName(DictPropertiesConstants.REFUND_PAYMENT_DESCRIPTION)).orElse(value);
        return value;
    }

    public Boolean getShippingOnlineSwitch() {
        int defaultValue = 0;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.SHIPPING_ONLINE_SWITCH)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getShippingOnlineSwitch.error: {}", e.getMessage());
        }
        return defaultValue == 1;
    }

    public int getAlipayReceivePaymentExpiredMinutes() {
        // 默认60分钟，设置为0时不过期
        int defaultValue = 60;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.ALIPAY_RECEIVE_PAYMENT_EXPIRED_MINUTES)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getAlipayReceivePaymentExpiredMinutes.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    public int getShippingOrderExpiredMinutes() {
        int defaultValue = 60 * 5;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.SHIPPING_ORDER_EXPIRED_MINUTES)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getShippingOrderExpiredMinutes.error: {}", e.getMessage());
        }
        return defaultValue;
    }


    /**
     * 获取订单过期时间设置，超时关闭订单
     */
    public int getProductOrderExpiredMinutes() {
        int defaultValue = 60 * 2;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.PRODUCT_ORDER_EXPIRED_MINUTES)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getProductOrderExpiredMinutes.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    /**
     * 获取订单报价过期时间设置，超时关闭报价功能
     */
    public int getQuoteExpiredMinutes() {
        int defaultValue = 5;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.PRODUCT_ORDER_QUOTE_EXPIRED_MINUTES)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getQuoteExpiredMinutes.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    public int getQuoteWarningThresholdPrice() {
        // 默认10000元
        int defaultValue = 1000000;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.QUOTE_WARNING_THRESHOLD_PRICE)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getQuoteWarningThresholdPrice.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    public int getShippingOverdueTimeThresholdPrice() {
        // 默认200元
        int defaultValue = 20000;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.SHIPPING_OVER_DUE_TIME_THRESHOLD_PRICE)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getShippingOverdueTimeThresholdPrice.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    public int getBadFixInsurancePrice() {
        // 默认1588元
        int defaultValue = 1588;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.BAD_FIX_INSURANCE_PRICE)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("BAD_FIX_INSURANCE_PRICE.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    public int getGoodFixInsurancePrice() {
        // 默认788元
        int defaultValue = 788;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.GOOD_FIX_INSURANCE_PRICE)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("GOOD_FIX_INSURANCE_PRICE.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    private String getValueByName(String name) {
        if (StrUtil.isBlank(name)) {
            return null;
        }
        String value = getFromCache(name);
        if (StrUtil.isBlank(value)) {
            value = getValueByNameFromStorage(name);
            setToCache(name, value);
        }
        return value;
    }

    public String getValueByNameFromStorage(String name) {
        return StrUtil.isBlank(name)
                ? null
                : Optional.ofNullable(this.lambdaQuery().eq(CommonSysDict::getName, name).one()).map(CommonSysDict::getValue).orElse(null);
    }

    public DictMapVO getNameMap(String name) {
        Map<String, String> resp = new HashMap<>();
        String value = getValueByName(name);
        if (StrUtil.isNotBlank(value)) {
            resp = JSONUtil.toBean(value, new TypeReference<Map<String, String>>() {
            }, true);
        }
        DictMapVO dictMapVO = new DictMapVO();
        dictMapVO.setResultMap(resp);
        return dictMapVO;
    }

    private String getFromCache(String name) {
        if (StrUtil.isBlank(name)) {
            return null;
        }
        String key = buildKey(name);
        return redisTemplate.opsForValue().get(key);
    }

    private void setToCache(String name, String value) {
        if (StrUtil.isBlank(name) || StrUtil.isBlank(value)) {
            return;
        }
        String key = buildKey(name);
        redisTemplate.opsForValue().set(key, value, 10, TimeUnit.DAYS);
    }

    public Boolean getRtaFuncAble() {
        int defaultValue = 0;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.RTA_FUNC_ABLE)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getTemporaryFuncAble.error: {}", e.getMessage());
        }
        return defaultValue == 1;
    }

    private String buildKey(String name) {
        return DictPropertiesConstants.DICT_NAME_CACHE_PREFIX + name;
    }
}