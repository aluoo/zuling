package com.anyi.sparrow.hk.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.account.constant.EmployAccountChangeEnum;
import com.anyi.common.commission.domain.CommissionSettle;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.service.CommissionSettleService;
import com.anyi.common.hk.domain.HkApplyOrder;
import com.anyi.common.hk.domain.HkNotifyLog;
import com.anyi.common.hk.domain.HkProduct;
import com.anyi.common.hk.service.HkApplyOrderService;
import com.anyi.common.hk.service.HkNotifyLogService;
import com.anyi.common.hk.service.HkProductService;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.sparrow.common.utils.DateUtils;
import com.anyi.sparrow.hk.dto.HkNotifyReq;
import com.anyi.sparrow.hk.enums.HkOrderStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author chenjian
 * @Description
 * @Date 2025/6/3
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class HkNotifyProcessService {

    @Autowired
    private HkApplyOrderService hkApplyOrderService;
    @Autowired
    private HkNotifyLogService hkNotifyLogService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private HkProductService hkProductService;
    @Autowired
    private CommissionSettleService commissionSettleService;


    public void doneOrder(HkNotifyReq req) {
        if(StrUtil.isBlank(req.getOrder_sn())) return;
        HkNotifyLog notifyLog = new HkNotifyLog();
        BeanUtil.copyProperties(req, notifyLog);
        notifyLog.setData(JSONUtil.toJsonStr(req));
        notifyLog.setOrderSn(req.getOrder_sn());
        notifyLog.setThirdOrderSn(req.getThird_order_sn());
        notifyLog.setExpressBill(req.getExpress_bill());
        notifyLog.setIsActived(req.getIs_actived());
        notifyLog.setActiveTime(req.getActive_time());
        notifyLog.setPlanMobileNumber(req.getPlan_mobile_number());
        notifyLog.setExpress(req.getExpress());
        notifyLog.setIsReturnUrl(req.getIs_return_url());
        // save log
        ThreadUtil.execAsync(() -> hkNotifyLogService.save(notifyLog));
        changeOrderDoAction(req);
    }



    private void changeOrderDoAction(HkNotifyReq req){
        HkApplyOrder hkApplyOrder = hkApplyOrderService.getById(req.getThird_order_sn());
        if(ObjectUtil.isNull(hkApplyOrder)) return;

        /*if(req.getStatus()<hkApplyOrder.getStatus()){
            log.error("订单状态不能往回更新订单号{}的订单记录",req.getThird_order_sn());
            return;
        }*/

        //状态相同就单独更新理由字段
        if(req.getStatus()==hkApplyOrder.getStatus()){
            hkApplyOrder.setReason(req.getReason());
            hkApplyOrderService.updateById(hkApplyOrder);
            return;
        }

        if(StrUtil.isNotBlank(req.getActive_time())){
            hkApplyOrder.setActiveTime(DateUtils.strToDate(req.getActive_time(),"yyyy-MM-dd HH:mm:ss"));
        }
        hkApplyOrder.setPlanMobileNumber(req.getPlan_mobile_number());
        hkApplyOrder.setThirdOrderSn(req.getOrder_sn());
        hkApplyOrder.setStatus(req.getStatus());
        hkApplyOrder.setReason(req.getReason());
        hkApplyOrder.setExpressBill(req.getExpress_bill());
        hkApplyOrder.setExpress(req.getExpress());
        hkApplyOrder.setUpdateTime(new Date());
        hkApplyOrderService.updateById(hkApplyOrder);

        //判断结算表是否有数据
        LambdaQueryWrapper<CommissionSettle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommissionSettle::getCorrelationId, hkApplyOrder.getId());
        List<CommissionSettle> settleList = commissionSettleService.list(queryWrapper);
        if(CollUtil.isNotEmpty(settleList)){
            return;
        }

        HkProduct product = hkProductService.lambdaQuery().eq(HkProduct::getId,hkApplyOrder.getProductId()).one();
        //分佣节点分佣
        if(ObjectUtil.isNotNull(product) && product.getCommissionStatus().intValue()==req.getStatus().intValue()){
            //结算佣金
            EmployAccountChangeEnum employAccountChangeEnum = EmployAccountChangeEnum.hk_commission;
            commissionSettleService.waitSettleOrder(hkApplyOrder.getId(), CommissionBizType.HK_SERVICE, product.getCommissionTypePackageId(),hkApplyOrder.getEmployeeId(),null,employAccountChangeEnum.getRemark());
            commissionSettleService.settleOrder(hkApplyOrder.getId(), CommissionBizType.HK_SERVICE, product.getCommissionTypePackageId(),employAccountChangeEnum,employAccountChangeEnum.getRemark());
        }

        HkOrderStatusEnum orderStatusEnum = EnumUtil.getBy(HkOrderStatusEnum::getCode,req.getStatus());
        //进件单日志
        orderLogService.addLog(
                1L,
                hkApplyOrder.getId(),
                orderStatusEnum.getCode(),
                orderStatusEnum.getCode(),
                orderStatusEnum.getName(),
                orderStatusEnum.getName()
        );
    }




}