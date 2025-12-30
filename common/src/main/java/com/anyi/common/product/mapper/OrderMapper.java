package com.anyi.common.product.mapper;

import com.anyi.common.mobileStat.domain.CompanyDataDailyBase;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.dto.OrderShippingCountDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/1
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface OrderMapper extends BaseMapper<Order> {
    /**
     * 根据当前时间条件统计不同状态的发货数量
     * countType=1 统计今日待发货
     * countType=2 统计超时未发货
     * countType=null 统计发货途中数量
     * @param req OrderShippingCountDTO
     * @return OrderShippingCountDTO
     */
    List<OrderShippingCountDTO> conditionCountOrderGroupByRecycler(@Param("req") OrderShippingCountDTO req);

    /**
     * 统计回收商订单已核验/未核验数量
     * @param req OrderShippingCountDTO
     * @return OrderShippingCountDTO
     */
    OrderShippingCountDTO countOrderVerifyGroupByRecycler(@Param("req") OrderShippingCountDTO req);



    /**
     * 门店数据看板 每日回收数量
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    CompanyDataDailyBase companyTransStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("employeeIds") List<Long> employeeIds);

    /**
     * 门店数据看板 每日询价数量
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    CompanyDataDailyBase companyOrderStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("employeeIds") List<Long> employeeIds);

    /**
     * 门店数据看板 每日报价数量
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    CompanyDataDailyBase priceOrderStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("employeeIds") List<Long> employeeIds);

    /**
     * 门店数据看板 每日取消数量
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    CompanyDataDailyBase cancelStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("employeeIds") List<Long> employeeIds);

    /**
     * 门店数据看板 每日作废数量
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    CompanyDataDailyBase overTimeStatGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("employeeIds") List<Long> employeeIds);
}