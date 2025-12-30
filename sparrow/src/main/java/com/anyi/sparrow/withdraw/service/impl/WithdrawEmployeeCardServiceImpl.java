package com.anyi.sparrow.withdraw.service.impl;


import com.anyi.sparrow.withdraw.constant.WithdrawCardStatusEnum;
import com.anyi.sparrow.withdraw.constant.WithdrawCardTypeEnum;
import com.anyi.sparrow.withdraw.dao.mapper.WithdrawEmployeeCardMapper;
import com.anyi.sparrow.withdraw.domain.WithdrawEmployeeCard;
import com.anyi.sparrow.withdraw.service.IWithdrawEmployeeCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 账户提现方式绑定 服务实现类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Service
public class WithdrawEmployeeCardServiceImpl extends ServiceImpl<WithdrawEmployeeCardMapper, WithdrawEmployeeCard> implements IWithdrawEmployeeCardService {

    @Override
    public List<WithdrawEmployeeCard> findAvailableCards(Long employeeId) {
        return this.lambdaQuery().eq(WithdrawEmployeeCard::getEmployeeId, employeeId)
                .eq(WithdrawEmployeeCard::getStatus, WithdrawCardStatusEnum.normal.getType()).list();
    }

    @Override
    public long queryCardCountBy(Long employeeId, int cardType) {

        Long count = this.lambdaQuery().eq(WithdrawEmployeeCard::getEmployeeId, employeeId)
                .eq(WithdrawEmployeeCard::getType, cardType).count();
        return count == null ? 0L : count;
    }

    @Override
    public Boolean checkAccountNoExists(String accountNo) {

        Long count = this.lambdaQuery().eq(WithdrawEmployeeCard::getAccountNo, accountNo)
                .count();
        return count != null && count > 0L;
    }

    @Override
    public WithdrawEmployeeCard getCardBy(Long employeeId, Long cardId) {

        WithdrawEmployeeCard card = this.lambdaQuery()
                .eq(WithdrawEmployeeCard::getId, cardId)
                .eq(WithdrawEmployeeCard::getEmployeeId, employeeId)
                .one();
        return card;
    }

    @Override
    public List<WithdrawEmployeeCard> listCardBy(Long employeeId, WithdrawCardTypeEnum cardTypeEnum) {
        return this.lambdaQuery().eq(WithdrawEmployeeCard::getEmployeeId, employeeId)
                .eq(WithdrawEmployeeCard::getType, cardTypeEnum.getType()).list();
    }
}
