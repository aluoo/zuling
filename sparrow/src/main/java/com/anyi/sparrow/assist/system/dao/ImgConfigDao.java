package com.anyi.sparrow.assist.system.dao;

import com.anyi.sparrow.assist.system.dao.mapper.ImgConfigMapper;
import com.anyi.sparrow.assist.system.domain.ImgConfigExample;
import com.anyi.sparrow.common.enums.ImageType;
import com.anyi.sparrow.assist.system.domain.ImgConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImgConfigDao {
    @Autowired
    private ImgConfigMapper imgConfigMapper;

    public List<ImgConfig> imgConfig(ImageType imageType) {
        ImgConfigExample example = new ImgConfigExample();
        example.createCriteria().andTypeEqualTo(imageType.getCode());
        example.setOrderByClause("sort");
        return imgConfigMapper.selectByExample(example);
    }
}
