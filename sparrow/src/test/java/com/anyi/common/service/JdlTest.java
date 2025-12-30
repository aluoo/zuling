package com.anyi.common.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.sparrow.SparrowApplicationTest;
import com.lop.open.api.sdk.DefaultDomainApiClient;
import com.lop.open.api.sdk.LopException;
import com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.*;
import com.lop.open.api.sdk.plugin.LopPlugin;
import com.lop.open.api.sdk.plugin.factory.OAuth2PluginFactory;
import com.lop.open.api.sdk.request.ECAP.EcapV1OrdersCreateLopRequest;
import com.lop.open.api.sdk.response.ECAP.EcapV1OrdersCreateLopResponse;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collections;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/20
 * @Copyright
 * @Version 1.0
 */
@Slf4j
public class JdlTest extends SparrowApplicationTest {
    public static final String APP_KEY = "f73264e00bae453ba29c4ffd337c5606";
    public static final String APP_SECRET = "afd0f7618c974251a861ab49c64e167f";
    public static final String CUSTOMER_CODE = "020K2847752";
    public static final String ACCESS_TOKEN = "0c274c6abf394a7c8ae5c606b8bba202";

    public static void main(String[] args) {
        DefaultDomainApiClient client = new DefaultDomainApiClient("https://uat-api.jdl.com",500,15000);
        EcapV1OrdersCreateLopRequest request = new EcapV1OrdersCreateLopRequest();

        CommonCreateOrderRequest req = new CommonCreateOrderRequest();
        req.setPickupStartTime(DateUtil.parse("2024-03-25 20:30:00"));
        req.setPickupEndTime(DateUtil.parse("2024-03-25 21:00:00"));
        req.setOrderId("aycx-test1");
        req.setOrderOrigin(1);
        req.setCustomerCode(CUSTOMER_CODE);
        req.setSettleType(3);
        Contact sender = new Contact();
        sender.setName("测试发送");
        sender.setMobile("13112341234");
        sender.setFullAddress("河北省廊坊市广阳区万庄镇中心小学");
        Contact receiver = new Contact();
        receiver.setName("测试接收");
        receiver.setMobile("13112341234");
        receiver.setFullAddress("河北省廊坊市广阳区万庄镇中心小学");
        req.setSenderContact(sender);
        req.setReceiverContact(receiver);
        CommonProductInfo productInfo = new CommonProductInfo();
        productInfo.setProductCode("ed-m-0001");
        req.setProductsReq(productInfo);
        CommonCargoInfo commonCargoInfo = new CommonCargoInfo();
        commonCargoInfo.setName("手机");
        commonCargoInfo.setQuantity(1);
        commonCargoInfo.setWeight(new BigDecimal(1));
        commonCargoInfo.setVolume(new BigDecimal(1));
        req.setCargoes(Collections.singletonList(commonCargoInfo));
        request.setRequest(req);
        LopPlugin lopPlugin = OAuth2PluginFactory.produceLopPlugin(APP_KEY, APP_SECRET, ACCESS_TOKEN);
        request.addLopPlugin(lopPlugin);

        try {
            EcapV1OrdersCreateLopResponse response = client.execute(request);
            log.info(JSONUtil.toJsonStr(response));
            log.info(response.getMsg());
            CommonCreateOrderResponse data = response.getResult().getData();
            log.info(JSONUtil.toJsonStr(data));
        } catch (LopException e) {
            throw new RuntimeException(e);
        }

    }

    /*public static void main(String[] args) {

        //示例为调用京东快递下单前置校验接口
        try {

            //设置接口域名(有的对接方案同时支持生产和沙箱环境，有的仅支持生产，具体以对接方案中的【API文档-请求地址】为准)，生产域名：https://api.jdl.com 预发环境域名：https://uat-api.jdl.com
            //DefaultDomainApiClient对象全局只需要创建一次
            DefaultDomainApiClient client = new DefaultDomainApiClient("https://uat-api.jdl.com",500,15000);

            //入参对象（请记得更换为自己要使用的接口入参对象）
            EcapV1OrdersPrecheckLopRequest request = new EcapV1OrdersPrecheckLopRequest();

            //设置入参（请记得更换为自己要使用的接口入参）
            Contact senderContact  = new Contact();
            senderContact.setFullAddress("河北省廊坊市广阳区万庄镇中心小学");
            Contact receiverContact = new Contact();
            receiverContact.setFullAddress("河北省廊坊市广阳区万庄镇中心小学");
            CommonCreateOrderRequest requestDTO = new  CommonCreateOrderRequest();
            requestDTO.setOrderOrigin(1);
            requestDTO.setCustomerCode(CUSTOMER_CODE);
            CommonProductInfo productInfo = new CommonProductInfo();
            productInfo.setProductCode("ed-m-0001");
            requestDTO.setProductsReq(productInfo);
            requestDTO.setReceiverContact(receiverContact);
            requestDTO.setSenderContact(senderContact);
            request.setRequest(requestDTO);

            //设置插件，必须的操作，不同类型的应用入参不同，请看入参注释，公共参数按顺序分别为AppKey、AppSecret、AccessToken
            //使用开放平台ISV/自研商家应用调用接口
            LopPlugin lopPlugin = OAuth2PluginFactory.produceLopPlugin(APP_KEY, APP_SECRET, ACCESS_TOKEN);
            request.addLopPlugin(lopPlugin);

            //使用开放平台合作伙伴应用调用接口
//            LopPlugin lopPlugin = OAuth2PluginFactory.produceLopPlugin("bae34******************8fd", "661e4d**********************ec", "");
//            request.addLopPlugin(lopPlugin);

            //使用JOS应用调用物流开放平台接口
//            request.setUseJosAuth(true);
//            LopPlugin lopPlugin = OAuth2PluginFactory.produceLopPlugin("DE79844E3***********43236CC", "7b01ff52c2********7b661448", "b89114***************d4e9da950m2u");
//            request.addLopPlugin(lopPlugin);

            EcapV1OrdersPrecheckLopResponse response = client.execute(request);
            System.out.println(response.getMsg());
        } catch (LopException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}