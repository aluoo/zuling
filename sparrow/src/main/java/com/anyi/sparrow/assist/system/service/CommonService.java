package com.anyi.sparrow.assist.system.service;

import com.anyi.sparrow.common.enums.ImageType;
import com.anyi.sparrow.common.vo.ImgConfigRs;
import com.anyi.sparrow.assist.system.dao.ImgConfigDao;
import com.anyi.sparrow.assist.system.domain.ImgConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonService {
    @Autowired
    private ImgConfigDao imgConfigDao;

    public List<ImgConfigRs> imgConfig(ImageType imageType) {
        List<ImgConfig> imgConfigs = imgConfigDao.imgConfig(imageType);
        List<ImgConfigRs> result = new ArrayList<>();
        imgConfigs.forEach(imgConfig -> {
            ImgConfigRs imgConfigRs = new ImgConfigRs();
            BeanUtils.copyProperties(imgConfig, imgConfigRs);
            result.add(imgConfigRs);
        });
        return result;
    }
}
