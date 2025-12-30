package com.anyi.common.qfu;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.anyi.common.account.constant.EmployAccountChangeEnum;
import com.anyi.common.account.service.EmployeeAccountChangeService;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.notice.domain.CommonNoticeEmployeeMsg;
import com.anyi.common.notice.domain.enums.MsgBizTypeEnum;
import com.anyi.common.notice.service.CommonNoticeEmployeeMsgService;
import com.anyi.common.qfu.dto.QfuPaymentCheckResp;
import com.anyi.common.withdraw.domain.CommonWithdrawEmployeeApply;
import com.anyi.common.withdraw.domain.dto.StateSnapshotDTO;
import com.anyi.common.withdraw.domain.enums.WithdrawApplyStatusEnum;
import com.anyi.common.withdraw.service.CommonWithdrawEmployeeApplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/22
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class WithdrawPaymentCheckService {
    @Autowired
    private QfuService qfuService;
    @Autowired
    private CommonWithdrawEmployeeApplyService withdrawEmployeeApplyService;
    @Autowired
    private EmployeeAccountChangeService employeeAccountChangeService;
    @Autowired
    private CommonNoticeEmployeeMsgService messageService;

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void paymentCheck(CommonWithdrawEmployeeApply apply) {
        log.info("auto payment check start applyNo-{}", apply.getApplyNo());
        QfuPaymentCheckResp resp = qfuService.paymentCheck(apply.getApplyNo(), apply.getType());
        log.info("auto payment check applyNo-{} resp-{}", apply.getApplyNo(), JSONUtil.toJsonStr(resp));
        if (resp == null) {
            log.info("applyNo-{} auto payment check failed", apply.getApplyNo());
            return;
        }
        if (resp.getCode().equals(0)) {
            log.info("applyNo-{} auto payment check still paying", apply.getApplyNo());
            return;
        }
        if (resp.getCode().equals(1)) {
            log.info("auto payment check success applyNo-{}", apply.getApplyNo());
            // 成功
            success(apply);
        } else {
            log.info("auto payment check failed applyNo-{}", apply.getApplyNo());
            // 失败
            String failedMessage = resp.getMessage();
            String wrappedFailedMessage = resp.getMessage();
            if (resp.getCode().equals(-2) || resp.getCode().equals(-3)) {
                // -2钱包余额不足 -3渠道余额不足
                wrappedFailedMessage = "系统升级维护，请稍后再试";
            }
            failed(apply, failedMessage, wrappedFailedMessage);
        }
        log.info("auto payment check end applyNo-{}", apply.getApplyNo());
    }

    private void success(CommonWithdrawEmployeeApply apply) {
        JSONArray jsonArray = JSONUtil.parseArray(apply.getStateSnapshot());
        // 打款成功
        List<StateSnapshotDTO> stateSnapshotDTOs = jsonArray.stream().map(item -> JSONUtil.toBean((JSONObject) item, StateSnapshotDTO.class)).collect(Collectors.toList());
        StateSnapshotDTO paySuccessSnapshot = new StateSnapshotDTO(WithdrawApplyStatusEnum.pay_success.getType(), null, LocalDateTime.now());
        stateSnapshotDTOs.add(paySuccessSnapshot);
        apply.setStateSnapshot(JSONUtil.toJsonStr(stateSnapshotDTOs));
        apply.setStatus(WithdrawApplyStatusEnum.pay_success.getType());
        apply.setUpdateTime(LocalDateTime.now());
        withdrawEmployeeApplyService.updateById(apply);
        // 账户余额变动
        boolean success = employeeAccountChangeService.changeAccount(apply.getEmployeeId(), EmployAccountChangeEnum.withdrawPass, apply.getAmount(), apply.getId(), null);
        if (!success) {
            log.error("applyNo-{} employeeId-{} changeAccount failed", apply.getApplyNo(), apply.getEmployeeId());
            throw new BusinessException(-1, "员工账户资金变动失败");
        }
        // 插入通知_员工消息
        CommonNoticeEmployeeMsg msg = CommonNoticeEmployeeMsg.builder()
                .employeeId(apply.getEmployeeId())
                .bizType(MsgBizTypeEnum.WITHDRAW.getCode())
                .title("提现打款成功")
                .pushTime(new Date())
                .hasRead(false)
                .content("提现打款成功")
                .bizId(apply.getId())
                .createTime(new Date())
                .build();
        messageService.save(msg);
    }

    private void failed(CommonWithdrawEmployeeApply apply, String failedMessage, String wrappedFailedMessage) {
        JSONArray jsonArray = JSONUtil.parseArray(apply.getStateSnapshot());
        // 打款失败 记录原因
        List<StateSnapshotDTO> stateSnapshotDTOs = jsonArray.stream().map(item -> JSONUtil.toBean((JSONObject) item, StateSnapshotDTO.class)).collect(Collectors.toList());
        StateSnapshotDTO failedSnapshot = new StateSnapshotDTO(WithdrawApplyStatusEnum.fail.getType(), wrappedFailedMessage, LocalDateTime.now());
        stateSnapshotDTOs.add(failedSnapshot);
        apply.setStateSnapshot(JSONUtil.toJsonStr(stateSnapshotDTOs));
        apply.setStatus(WithdrawApplyStatusEnum.fail.getType());
        apply.setUpdateTime(LocalDateTime.now());
        apply.setRemark(failedMessage);
        withdrawEmployeeApplyService.updateById(apply);
        // 账户余额变动
        boolean success = employeeAccountChangeService.changeAccount(apply.getEmployeeId(), EmployAccountChangeEnum.withdrawUnPass, apply.getAmount(), apply.getId(), null);
        if (!success) {
            log.error("applyNo-{} employeeId-{} changeAccount failed", apply.getApplyNo(), apply.getEmployeeId());
            throw new BusinessException(-1, "员工账户资金变动失败");
        }
        // 插入通知_员工消息
        CommonNoticeEmployeeMsg msg = CommonNoticeEmployeeMsg.builder()
                .employeeId(apply.getEmployeeId())
                .bizType(MsgBizTypeEnum.WITHDRAW.getCode())
                .title("提现打款失败")
                .pushTime(new Date())
                .hasRead(false)
                .content("提现打款失败，原因是" + wrappedFailedMessage)
                .bizId(apply.getId())
                .createTime(new Date())
                .build();
        messageService.save(msg);
    }
}