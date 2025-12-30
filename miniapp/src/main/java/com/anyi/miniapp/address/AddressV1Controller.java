package com.anyi.miniapp.address;

import com.anyi.common.domain.param.Response;
import com.anyi.common.address.service.AddressService;
import com.anyi.common.address.vo.AddrSearchReq;
import com.anyi.common.address.vo.AddressVO;
import com.anyi.common.address.vo.DeleteReq;
import com.anyi.common.address.vo.GetLatestReq;
import com.anyi.miniapp.interceptor.UserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "地址管理")
@RestController
@RequestMapping("/mobile/addr")
public class AddressV1Controller {
    @Autowired
    private AddressService addressService;

    @ApiOperation("地址列表")
    @ResponseBody
    @GetMapping("getList")
    public Response<List<AddressVO>> getAddressList() {
        return Response.ok(addressService.getAddressList(UserManager.getCurrentUser().getEmployeeId()));
    }

    @ApiOperation("地址列表（支持搜索）")
    @ResponseBody
    @PostMapping("getList")
    public Response<List<AddressVO>> getAddressListBySearch(@RequestBody AddrSearchReq addrSearchReq) {
        return Response.ok(addressService.getAddressList(addrSearchReq, UserManager.getCurrentUser().getEmployeeId()));
    }

    @ApiOperation("新增地址")
    @ResponseBody
    @PostMapping("add")
    public Response add(@RequestBody AddressVO addressVO) {
        addressService.add(addressVO, UserManager.getCurrentUser().getEmployeeId(), UserManager.getCurrentUser().getNickname());
        return Response.ok();
    }

    @ApiOperation("修改地址")
    @ResponseBody
    @PostMapping("update")
    public Response update(@RequestBody AddressVO addressVO) {
        addressService.update(addressVO, UserManager.getCurrentUser().getEmployeeId(), UserManager.getCurrentUser().getNickname());
        return Response.ok();
    }

    @ApiOperation("获取最新使用过的收货地址")
    @ResponseBody
    @GetMapping("getLatestUse")
    public Response<AddressVO> getLatestUse(GetLatestReq req) {
        return Response.ok(addressService.getLatestUse(req, UserManager.getCurrentUser().getEmployeeId()));
    }

    @ApiOperation("根据id返回地址详情")
    @ResponseBody
    @GetMapping("getById")
    public Response<AddressVO> getById(@ApiParam("地址id") @RequestParam Long addressId) {
        return Response.ok(addressService.getById(addressId));
    }

    @ApiOperation("删除地址")
    @ResponseBody
    @PostMapping("delete")
    public Response delete(@RequestBody DeleteReq req) {
        addressService.delete(req);
        return Response.ok();
    }
}
