package com.anyi.common.mbr.service;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.mbr.domain.MbrOrder;
import com.anyi.common.mbr.domain.MbrShopCode;
import com.anyi.common.mbr.enums.MbrOrderStatusEnum;
import com.anyi.common.mbr.mapper.MbrOrderMapper;
import com.anyi.common.mbr.req.MbrChangeOrderNotifyReq;
import com.anyi.common.mbr.req.MbrCreateOrderNotifyReq;
import com.anyi.common.product.service.OrderLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class MbrOrderService extends ServiceImpl<MbrOrderMapper, MbrOrder>  {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MbrShopCodeService shopCodeService;

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(MbrCreateOrderNotifyReq req){
        MbrOrder order = new MbrOrder();
        BeanUtils.copyProperties(req, order);
        MbrOrder oriOrder = this.lambdaQuery()
                .eq(MbrOrder::getThirdOrderId, order.getThirdOrderId()).last("limit 1")
                .one();
        if(ObjectUtil.isNotNull(oriOrder)){
            throw new BusinessException(99999,"订单号已经存在");
        }
        Employee employee = employeeService.getById(req.getStoreEmployeeId());
        if(ObjectUtil.isNull(employee)){
            throw new BusinessException(99999,"员工不存在");
        }
        order.setStoreEmployeeId(employee.getId());
        order.setStoreCompanyId(employee.getCompanyId());
        order.setAncestors(employee.getAncestors());
        this.save(order);
        //进件单日志
        orderLogService.addLog(
                1L,
                order.getId(),
                MbrOrderStatusEnum.ORDER_CREATE.getCode(),
                MbrOrderStatusEnum.ORDER_CREATE.getCode(),
                MbrOrderStatusEnum.ORDER_CREATE.getDesc(),
                MbrOrderStatusEnum.ORDER_CREATE.getDesc()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void passOrder(MbrCreateOrderNotifyReq req){
        MbrOrder oriOrder = this.lambdaQuery()
                .eq(MbrOrder::getThirdOrderId, req.getThirdOrderId()).last("limit 1")
                .one();
        if(ObjectUtil.isNull(oriOrder)){
            throw new BusinessException(99999,"第三方订单不存在");
        }
        BeanUtils.copyProperties(req, oriOrder);
        this.updateById(oriOrder);
        //进件单日志
        orderLogService.addLog(
                1L,
                oriOrder.getId(),
                MbrOrderStatusEnum.CHECK_PASS.getCode(),
                MbrOrderStatusEnum.CHECK_PASS.getCode(),
                MbrOrderStatusEnum.CHECK_PASS.getDesc(),
                MbrOrderStatusEnum.CHECK_PASS.getDesc()
        );
    }


    @Transactional(rollbackFor = Exception.class)
    public void changeOrder(MbrChangeOrderNotifyReq req){

        MbrOrder oriOrder = this.lambdaQuery()
                .eq(MbrOrder::getThirdOrderId, req.getThirdOrderId())
                .eq(MbrOrder::getDeleted, 0)
                .last("limit 1")
                .one();

        if(ObjectUtil.isNull(oriOrder)){
            throw new BusinessException(99999,"订单号不存在");
        }

        if(req.getStatus().intValue()<oriOrder.getStatus().intValue()){
            throw new BusinessException(99999,"订单状态不支持回滚更新");
        }

        if(MbrOrderStatusEnum.FINISHED.getCode().equals(req.getStatus())
                && StrUtil.isNotBlank(req.getSettleLink())){
            Company company = companyService.getById(oriOrder.getStoreCompanyId());
            shopCodeService.lambdaUpdate()
                    .eq(MbrShopCode::getEmployeeId,company.getEmployeeId())
                    .eq(MbrShopCode::getDeleted,0)
                    .set(MbrShopCode::getSettleLink,req.getSettleLink()).update();
        }


        MbrOrderStatusEnum orderStatusEnum = EnumUtil.getBy(MbrOrderStatusEnum::getCode,req.getStatus());
        oriOrder.setStatus(req.getStatus());
        oriOrder.setUpdateTime(new Date());
        this.updateById(oriOrder);

        //进件单日志
        orderLogService.addLog(
                1L,
                oriOrder.getId(),
                orderStatusEnum.getCode(),
                orderStatusEnum.getCode(),
                orderStatusEnum.getDesc(),
                orderStatusEnum.getDesc()
        );
    }
}
