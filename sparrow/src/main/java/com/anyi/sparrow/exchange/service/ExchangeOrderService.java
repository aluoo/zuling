package com.anyi.sparrow.exchange.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.req.CompanyReq;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.company.vo.AgencyCompanyVO;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmType;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.exchange.domain.MbExchangeOrder;
import com.anyi.common.exchange.domain.MbExchangePic;
import com.anyi.common.exchange.enums.ExchangeOrderTypeEnum;
import com.anyi.common.exchange.enums.ExchangeStatus;
import com.anyi.common.exchange.service.MbExchangeOrderService;
import com.anyi.common.exchange.service.MbExchangePicService;
import com.anyi.common.product.domain.OrderLog;
import com.anyi.common.product.domain.request.ExchangeOrderListReq;
import com.anyi.common.product.domain.response.ExchangeOrderVO;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/4/7
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ExchangeOrderService {
    @Autowired
    MbExchangeOrderService exchangeOrderService;
    @Autowired
    MbExchangePicService exchangePicService;
    @Autowired
    CompanyService companyService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    private OrderLogService orderLogService;

    public PageInfo<ExchangeOrderVO> listOrder(ExchangeOrderListReq req) {
        if (req.getStoreCompanyId() == null) {
            CompanyReq companyReq = new CompanyReq();
            companyReq.setPage(1);
            companyReq.setPageSize(10);
            companyReq.setAncestors(LoginUserContext.getUser().getAncestors());
            req.setStoreCompanyIds(companyService.ancestorsCompany(companyReq).stream().map(AgencyCompanyVO::getId).collect(Collectors.toList()));
            if (CollUtil.isEmpty(req.getStoreCompanyIds())) {
                req.setStoreCompanyId(LoginUserContext.getUser().getCompanyId());
            }
        }
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<MbExchangeOrder> list = exchangeOrderService.lambdaQuery()
                .eq(req.getStoreCompanyId()!=null,MbExchangeOrder::getStoreCompanyId, req.getStoreCompanyId())
                .isNotNull(MbExchangeOrder::getBdId)
                .eq(req.getStoreEmployeeId()!=null,MbExchangeOrder::getStoreEmployeeId,req.getStoreEmployeeId())
                .in(CollUtil.isNotEmpty(req.getStoreCompanyIds()),MbExchangeOrder::getStoreCompanyId,req.getStoreCompanyIds())
                .eq(req.getStatus() != null && req.getStatus()!=0, MbExchangeOrder::getStatus, req.getStatus())
                .in(req.getStatus() != null && req.getStatus()==0, MbExchangeOrder::getStatus, Arrays.asList(ExchangeStatus.SYS_PASS.getCode(),ExchangeStatus.TO_AUDIT.getCode(),ExchangeStatus.SYS_Fail.getCode()))
                .ge(req.getBeginTime() != null, MbExchangeOrder::getCreateTime, req.getBeginTime())
                .le(req.getEndTime() != null, MbExchangeOrder::getCreateTime, req.getEndTime())
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }
        List<ExchangeOrderVO> vos = BeanUtil.copyToList(list, ExchangeOrderVO.class);
        List<Long> orderIds = vos.stream().map(ExchangeOrderVO::getId).collect(Collectors.toList());
        Map<Long, List<MbExchangePic>> pictures = this.getPictures(orderIds);
        List<Long> companyIds = vos.stream().map(ExchangeOrderVO::getStoreCompanyId).collect(Collectors.toList());
        List<Long> employeeIds = vos.stream().map(ExchangeOrderVO::getStoreEmployeeId).collect(Collectors.toList());
        Map<Long, Company> companyInfoMap = companyService.getCompanyInfoMap(companyIds);
        Map<Long, Employee> employeeInfoMap = employeeService.getEmployeeInfoMap(employeeIds);
        List<OrderLog> logs = orderLogService.lambdaQuery()
                .in(OrderLog::getOrderId, orderIds)
                .eq(OrderLog::getOperationStatus, -1)
                .list();
        Map<Long, OrderLog> logMap = logs.stream().collect(Collectors.toMap(OrderLog::getOrderId, Function.identity(), (k1, k2) -> k1));
        vos.forEach(vo -> {
            List<MbExchangePic> pics = pictures.get(vo.getId());
            if(CollUtil.isNotEmpty(pics)){
                List<String> collect = pics.stream().map(MbExchangePic::getImageUrl).collect(Collectors.toList());
                vo.setImages(collect);
            }
            vo.setTypeName(EnumUtil.getBy(ExchangeOrderTypeEnum::getCode,vo.getType()).getDesc());
            vo.setStoreCompanyName(Optional.ofNullable(companyInfoMap.get(vo.getStoreCompanyId())).map(Company::getName).orElse(null));
            vo.setStoreEmployeeName(Optional.ofNullable(employeeInfoMap.get(vo.getStoreEmployeeId())).map(Employee::getName).orElse(null));
            //系统审核失败且是自审的才展示系统审核失败原因
            if(vo.getStatus().equals(ExchangeStatus.SYS_Fail.getCode()) && !vo.getPlatCheck()){
                vo.setFailedReason(vo.getSysRemark());
            }
            if (vo.getStatus().equals(ExchangeStatus.FAIL.getCode())) {
                vo.setFailedReason(Optional.ofNullable(logMap.get(vo.getId())).map(OrderLog::getRemark).orElse(null));
            }
        });
        PageInfo<ExchangeOrderVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    private Map<Long, List<MbExchangePic>> getPictures(List<Long> orderIds) {
        List<MbExchangePic> pictures = exchangePicService.lambdaQuery().in(MbExchangePic::getOrderId, orderIds).list();
        return pictures.stream().collect(Collectors.groupingBy(MbExchangePic::getOrderId));
    }
}