package com.anyi.task.task;

import com.anyi.common.service.OrderTaskService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/28
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class OrderTask {
    @Autowired
    OrderTaskService orderTaskService;

    @XxlJob("cancelReceivePayment")
    public void cancelReceivePayment() {
        XxlJobHelper.log("==========自动作废超时收款码任务开始========");
        orderTaskService.cancelReceivePayment();
        XxlJobHelper.log("==========自动作废超时收款码任务开始========");
    }

    @XxlJob("autoCloseOverdueOrder")
    public void autoCloseOverdueOrder() {
        XxlJobHelper.log("==========自动关闭报价订单任务开始========");
        orderTaskService.autoCloseOverdueOrder();
        XxlJobHelper.log("==========自动关闭报价订单任务结束========");
    }

    @XxlJob("autoCloseOverdueOrderQuote")
    public void autoCloseOverdueOrderQuote() {
        XxlJobHelper.log("==========自动关闭报价订单报价功能任务开始========");
        orderTaskService.autoCloseOverdueOrderQuote();
        XxlJobHelper.log("==========自动关闭报价订单报价功能任务结束========");
    }

    @XxlJob("autoCloseOverdueShippingOrder")
    public void autoCloseOverdueShippingOrder() {
        XxlJobHelper.log("==========自动关闭发货订单任务开始========");
        orderTaskService.autoCloseOverdueShippingOrder();
        XxlJobHelper.log("==========自动关闭发货订单任务结束========");
    }

    @XxlJob("repairQuoteSpentTimeReal")
    public void repairQuoteSpentTimeReal() {
        orderTaskService.repairQuoteSpentTimeReal();
    }

    @XxlJob("autoCloseOverdueInsuranceOrder")
    public void autoCloseOverdueInsuranceOrder() {
        XxlJobHelper.log("==========自动关闭数保订单任务开始========");
        orderTaskService.autoCloseOverdueInsuranceOrder();
        XxlJobHelper.log("==========自动关闭数保订单任务结束========");
    }

    @XxlJob("autoUploadInsuranceOrder")
    public void autoUploadInsuranceOrder() {
        XxlJobHelper.log("==========自动上传数保订单任务开始========");
        orderTaskService.autoUploadInsuranceOrder();
        XxlJobHelper.log("==========自动上传数保订单任务结束========");
    }
}