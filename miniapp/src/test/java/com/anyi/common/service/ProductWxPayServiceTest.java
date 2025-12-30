package com.anyi.common.service;

import cn.hutool.json.JSONUtil;
import com.anyi.common.wx.MchIdService;
import com.anyi.common.wx.CommonWxUtils;
import com.anyi.miniapp.MiniApplicationTest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsQueryRequest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsResult;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/14
 * @Copyright
 * @Version 1.0
 */
@Slf4j
public class ProductWxPayServiceTest extends MiniApplicationTest {
    @Autowired
    private ProductWxPayService service;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private MchIdService mchIdService;

    @Test
    public void test() {
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        PartnerTransactionsQueryRequest queryReq = new PartnerTransactionsQueryRequest();
        queryReq.setOutTradeNo(CommonWxUtils.createUnionTradeNo());
        queryReq.setSpMchid(wxPayService.getConfig().getMchId());
        queryReq.setSubMchid(wxPayService.getConfig().getSubMchId());
        PartnerTransactionsResult res;
        try {
            res = service.queryPartnerTransactions(queryReq);
            log.debug(JSONUtil.toJsonStr(res));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}