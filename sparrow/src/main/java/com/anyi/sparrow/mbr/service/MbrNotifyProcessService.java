package com.anyi.sparrow.mbr.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.mbr.domain.MbrNotifyLog;
import com.anyi.common.mbr.req.MbrChangeOrderNotifyReq;
import com.anyi.common.mbr.req.MbrCreateOrderNotifyReq;
import com.anyi.common.mbr.req.MbrHwzCreateOrderNotifyReq;
import com.anyi.common.mbr.service.MbrNotifyLogService;
import com.anyi.common.mbr.service.MbrOrderService;
import com.anyi.common.mbr.service.MbrShopCodeService;
import com.anyi.sparrow.mbr.enums.MbrNotifyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenjian
 * @Description
 * @Date 2025/6/3
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class MbrNotifyProcessService {

    @Autowired
    private MbrOrderService mbrOrderService;
    @Autowired
    private MbrNotifyLogService mbrNotifyLogService;
    @Autowired
    private CompanyService companyService;

    public void doneOrder(MbrHwzCreateOrderNotifyReq req) {
        MbrNotifyLog notifyLog = new MbrNotifyLog();
        notifyLog.setThirdOrderId(req.getData().getSaleOrderId());
        notifyLog.setResBody(JSONUtil.toJsonStr(req));
        // save log
        ThreadUtil.execAsync(() -> mbrNotifyLogService.save(notifyLog));
        MbrNotifyType notifyType = EnumUtil.getBy(MbrNotifyType::getCode,req.getMethod());
        switch (notifyType) {
            case CreteOrder:
                MbrCreateOrderNotifyReq notifyReq = parseNotifyParam(req);
                creteOrderDoAction(notifyReq);
                break;
            case PassOrder:
                MbrCreateOrderNotifyReq passReq = parseNotifyParam(req);
                PassOrderDoAction(passReq);
                break;
            case ChangeOrder:
                MbrChangeOrderNotifyReq changeReq = parseChangeNotifyParam(req);
                changeOrderDoAction(changeReq);
                break;
            default:
                return;
        }
    }

    private void creteOrderDoAction(MbrCreateOrderNotifyReq req){
        mbrOrderService.createOrder(req);
    }

    private void PassOrderDoAction(MbrCreateOrderNotifyReq req){
        mbrOrderService.passOrder(req);
    }

    private void changeOrderDoAction(MbrChangeOrderNotifyReq req){
        mbrOrderService.changeOrder(req);
    }

    private MbrCreateOrderNotifyReq parseNotifyParam(MbrHwzCreateOrderNotifyReq req){
        MbrCreateOrderNotifyReq notifyReq = new MbrCreateOrderNotifyReq();
        //地址都是员工ID
        notifyReq.setStoreEmployeeId(Long.valueOf(req.getData().getOutShopId()));
        //门店做单和店主区别
        Company company = companyService.getById(Long.valueOf(req.getData().getOutShopId()));
        if(ObjectUtil.isNotNull(company) && StrUtil.isNotBlank(req.getData().getOutEmployeeId())){
            notifyReq.setStoreEmployeeId(Long.valueOf(req.getData().getOutEmployeeId()));
        }
        if(ObjectUtil.isNotNull(company) && StrUtil.isBlank(req.getData().getOutEmployeeId())){
            notifyReq.setStoreEmployeeId(company.getEmployeeId());
        }
        notifyReq.setProductName(req.getData().getSkuName());
        StringBuilder spec = new StringBuilder();
        req.getData().getSpec().forEach(specItem -> {
            spec.append(specItem.getSpecName()+specItem.getSpecValue()+",");
        });
        notifyReq.setProductSpec(spec.toString().substring(0,spec.length()-1));
        notifyReq.setThirdOrderId(req.getData().getSaleOrderId());
        notifyReq.setProductType(req.getData().getSecondHand());
        notifyReq.setPeriod(req.getData().getRentPhase());
        notifyReq.setCustomName(req.getData().getMemberName());
        notifyReq.setCustomPhone(req.getData().getMemberMobile());
        notifyReq.setStatus(req.getData().getStatus());
        notifyReq.setIdCard(req.getData().getIdCard());
        notifyReq.setSettleAmount(req.getData().getProductCost().longValue());
        notifyReq.setPlanAmount(req.getData().getTotalRent().longValue());
        notifyReq.setDepositAmount(req.getData().getDeposit().longValue());
        return notifyReq;
    }

    private MbrChangeOrderNotifyReq parseChangeNotifyParam(MbrHwzCreateOrderNotifyReq req){
        MbrChangeOrderNotifyReq notifyReq = new MbrChangeOrderNotifyReq();
        notifyReq.setStoreEmployeeId(Long.valueOf(req.getData().getOutShopId()));
        notifyReq.setThirdOrderId(req.getData().getSaleOrderId());
        notifyReq.setSettleLink(req.getData().getSettleLink());
        notifyReq.setStatus(req.getData().getStatus());
        return notifyReq;
    }

}