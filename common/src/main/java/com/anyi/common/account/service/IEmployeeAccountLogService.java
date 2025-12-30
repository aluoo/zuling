package com.anyi.common.account.service;

import com.anyi.common.account.domain.EmployeeAccountLog;
import com.anyi.common.account.dto.EmployeeAccoutLogStatDTO;
import com.anyi.common.account.req.PartnerAccountLogReq;
import com.anyi.common.account.vo.PartnerAccountLogVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.Page;
import com.anyi.common.account.req.QueryAccountLogReq;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 个人账户变动明细表 服务类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
public interface IEmployeeAccountLogService extends IService<EmployeeAccountLog> {


    Page<EmployeeAccountLog> listUserFocusAccountLog(Long employeeId, QueryAccountLogReq req);

    EmployeeAccoutLogStatDTO AccountLogSum(Long employeeId, QueryAccountLogReq req);

    PageInfo<PartnerAccountLogVO> partnerAccountLog(PartnerAccountLogReq req);
}
