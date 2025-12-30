package com.anyi.sparrow.assist.system.controller;

import com.anyi.sparrow.common.enums.ImageType;
import com.anyi.sparrow.common.vo.FileUploadRes;
import com.anyi.sparrow.common.vo.ImgConfigRs;
import com.anyi.common.oss.FileUploader;
import com.anyi.sparrow.assist.system.service.CommonService;
import com.anyi.common.domain.param.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

@RestController
@Api(description = "通用接口--王")
@RequestMapping("common")
public class CommonController {
    @Autowired(required = false)
    private FileUploader uploader;

    @Autowired
    private CommonService commonService;

    @ApiOperation("文件上传")
    @ResponseBody
    @PostMapping("/v1.0/uploadFile")
    public Response<FileUploadRes> uploadFile(@RequestParam("file") CommonsMultipartFile file) {
        FileUploadRes res = new FileUploadRes();
        //res.setFileUrl(uploader.upload(file));
        return Response.ok(res);
    }
    @GetMapping("v1.1/image-config")
    @ApiOperation("查询图片配置信息")
    public Response<List<ImgConfigRs>> getImageConfig(@RequestParam ImageType imageType){
        return Response.ok(commonService.imgConfig(imageType));
    }
}
