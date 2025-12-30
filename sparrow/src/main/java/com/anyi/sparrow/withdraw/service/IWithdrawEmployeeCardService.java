package com.anyi.sparrow.withdraw.service;


import com.anyi.sparrow.withdraw.constant.WithdrawCardTypeEnum;
import com.anyi.sparrow.withdraw.domain.WithdrawEmployeeCard;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 账户提现方式绑定 服务类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
public interface IWithdrawEmployeeCardService extends IService<WithdrawEmployeeCard> {

    List<WithdrawEmployeeCard> findAvailableCards(Long employeeId);

    long queryCardCountBy(Long employeeId, int cardType);

    Boolean checkAccountNoExists(String accountNo);

    WithdrawEmployeeCard getCardBy(Long employeeId, Long cardId);

    List<WithdrawEmployeeCard> listCardBy(Long employeeId, WithdrawCardTypeEnum cardTypeEnum);
}
