package com.anyi.sparrow.hk.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.enums.CommissionPackage;
import com.anyi.common.commission.service.CommissionSettleService;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.hk.domain.HkApplyOrder;
import com.anyi.common.hk.domain.HkOperator;
import com.anyi.common.hk.domain.HkProduct;
import com.anyi.common.hk.domain.HkSupplier;
import com.anyi.common.hk.service.*;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.sparrow.base.security.Constants;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.hk.dto.*;
import com.anyi.sparrow.hk.enums.HkOrderStatusEnum;
import com.anyi.sparrow.hk.util.HkApiUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenjian
 * @Description
 * @Date 2025/4/21
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class HkOrderManageService {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private HkApplyOrderService hkApplyOrderService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private HkProductService hkProductService;
    @Autowired
    private HkOperateService hkOperateService;
    @Autowired
    private HkSupplierService hkSupplierService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private HkProductEmployeeService hkProductEmployeeService;
    @Autowired
    private CommissionSettleService commissionSettleService;

    @Transactional(rollbackFor = Exception.class)
    public void applyOrder(ApplyOrderReq req){
        HkProduct hkProduct = hkProductService.lambdaQuery().eq(HkProduct::getStatus,1)
                .eq(HkProduct::getCode,req.getFetchCode()).one();
        if(ObjectUtil.isNull(hkProduct)){
            throw new BusinessException(99999,"产品不存在或已下架");
        }

        List<Long> productIds = hkProductEmployeeService.getProductByCompanyId(LoginUserContext.getUser().getCompanyId());
        if (!productIds.contains(hkProduct.getId())) {
            throw new BusinessException(99999,"暂无权限办理该产品");
        }

        Long id = snowflakeIdService.nextId();
        String tokenValue = getToken();
        req.setThirdOrderSn(id.toString());
        ApplyOrderRsp applyOrderRsp = HkApiUtil.applyOrder(req,tokenValue);

        HkApplyOrder hkApplyOrder = new HkApplyOrder();
        BeanUtil.copyProperties(req,hkApplyOrder);
        hkApplyOrder.setId(id);
        hkApplyOrder.setFetchName(hkProduct.getName());
        hkApplyOrder.setProductId(hkProduct.getId());
        hkApplyOrder.setThirdOrderSn(applyOrderRsp.getOrder_sn());
        hkApplyOrder.setEmployeeId(LoginUserContext.getUser().getId());
        hkApplyOrder.setCompanyId(LoginUserContext.getUser().getCompanyId());
        hkApplyOrder.setAncestors(LoginUserContext.getUser().getAncestors());
        hkApplyOrder.setStatus(HkOrderStatusEnum.ONE.getCode());
        hkApplyOrder.setOperatorId(hkProduct.getOperatorId());
        hkApplyOrder.setSupplierId(hkProduct.getSupplierId());
        hkApplyOrderService.save(hkApplyOrder);

        //号卡佣金绑定订单
        commissionSettleService.orderBindSettleRule(hkApplyOrder.getId(), CommissionBizType.HK_SERVICE, hkProduct.getCommissionTypePackageId(),hkApplyOrder.getEmployeeId());

        //进件单日志
        orderLogService.addLog(
                1L,
                hkApplyOrder.getId(),
                HkOrderStatusEnum.ONE.getCode(),
                HkOrderStatusEnum.ONE.getCode(),
                HkOrderStatusEnum.ONE.getName(),
                HkOrderStatusEnum.ONE.getName()
        );
    }

    public List<TaskNumberRsp> taskNumber(TaskNumberReq req){
        String token = getToken();
        List<TaskNumberRsp> resultList = HkApiUtil.taskNumber(req,token);
        if(CollUtil.isEmpty(resultList)){
            throw new BusinessException(99999,"该区域暂无号卡");
        }
        return resultList;
    }

    public String getToken() {
        String tokenValue = redisTemplate.opsForValue().get(Constants.HK_TOKEN_KEY);
        if (StrUtil.isBlank(tokenValue)) {
            TokenRsp tokenRsp = HkApiUtil.getToken();
            //比获取到的提前过期，在重新获取
            redisTemplate.opsForValue().set(Constants.HK_TOKEN_KEY, tokenRsp.getToken(), tokenRsp.getExpire()-100, TimeUnit.SECONDS);
            return tokenRsp.getToken();
        }
        return tokenValue;
    }

    public PageInfo<HkOrderDetailVO> listOrder(HkOrderQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<HkApplyOrder> list = hkApplyOrderService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .likeRight(HkApplyOrder::getAncestors, LoginUserContext.getUser().getAncestors())
                .eq(req.getOnlySelf() != null && req.getOnlySelf(), HkApplyOrder::getEmployeeId, LoginUserContext.getUser().getId())
                .eq(req.getStatus() != null, HkApplyOrder::getStatus, req.getStatus())
                .ge(req.getBeginTime() != null, HkApplyOrder::getCreateTime, req.getBeginTime())
                .le(req.getEndTime() != null, HkApplyOrder::getCreateTime, req.getEndTime())
                .and(StrUtil.isNotBlank(req.getKeyword()), wp -> wp.eq(HkApplyOrder::getPlanMobileNumber, req.getKeyword()).or().eq(HkApplyOrder::getIdCard, req.getKeyword()))
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }
        List<HkOrderDetailVO> vos = BeanUtil.copyToList(list, HkOrderDetailVO.class);
        List<Long> companyIds = vos.stream().map(HkOrderDetailVO::getCompanyId).collect(Collectors.toList());
        List<Long> employeeIds = vos.stream().map(HkOrderDetailVO::getEmployeeId).collect(Collectors.toList());
        Map<Long, Company> companyMap = companyService.getCompanyInfoMap(companyIds);
        Map<Long, Employee> employeeMap = employeeService.getEmployeeInfoMap(employeeIds);
        vos.forEach(vo -> {
            vo.setCompanyName(companyMap.get(vo.getCompanyId()).getName());
            vo.setEmployeeName(employeeMap.get(vo.getEmployeeId()).getName());
            vo.setEmployeeMobile(employeeMap.get(vo.getEmployeeId()).getMobileNumber());

        });
        PageInfo<HkOrderDetailVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

    public HkOrderDetailVO detail(Long orderId) {
        HkApplyOrder order = hkApplyOrderService.getById(orderId);
        HkOrderDetailVO vo = new HkOrderDetailVO();
        BeanUtil.copyProperties(order, vo);
        vo.setOperatorName(hkOperateService.getById(order.getOperatorId()).getName());
        vo.setSupplierName(hkSupplierService.getById(order.getSupplierId()).getName());
        vo.setCompanyName(companyService.getById(order.getCompanyId()).getName());
        vo.setEmployeeName(employeeService.getById(order.getEmployeeId()).getName());
        vo.setEmployeeMobile(employeeService.getById(order.getEmployeeId()).getMobileNumber());
        return vo;
    }

    public PageInfo<HkProductDetailVO> productList(ProductQueryReq req) {
        List<Long> productIds = hkProductEmployeeService.getProductByCompanyId(LoginUserContext.getUser().getCompanyId());
        if (CollUtil.isEmpty(productIds)) {
            return PageInfo.emptyPageInfo();
        }
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<HkProduct> list = hkProductService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .like(StrUtil.isNotBlank(req.getProductName()), HkProduct::getName, req.getProductName())
                .eq(ObjectUtil.isNotNull(req.getOperatorId()), HkProduct::getOperatorId, req.getOperatorId())
                .in(HkProduct::getId, productIds)
                .eq(HkProduct::getStatus,1)
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .orderByDesc(AbstractBaseEntity::getUpdateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageInfo.emptyPageInfo();
        }
        List<HkProductDetailVO> vos = BeanUtil.copyToList(list, HkProductDetailVO.class);

        List<Long> operatorIds = vos.stream().map(HkProductDetailVO::getOperatorId).collect(Collectors.toList());
        List<Long> supplierIds = vos.stream().map(HkProductDetailVO::getSupplierId).collect(Collectors.toList());

        Map<Long, HkOperator> operatorMap = hkOperateService.getOperatorInfoMap(operatorIds);
        Map<Long, HkSupplier> supplierMap = hkSupplierService.getSupplierInfoMap(supplierIds);

        vos.forEach(vo -> {
            vo.setOperatorName(operatorMap.get(vo.getOperatorId()).getName());
            vo.setSupplierName(supplierMap.get(vo.getSupplierId()).getName());
        });
        PageInfo<HkProductDetailVO> resp = PageInfo.of(vos);
        resp.setTotal(page.getTotal());
        PageHelper.clearPage();
        return resp;
    }

}