package com.anyi.sparrow.assist.notice.dao;


import com.anyi.sparrow.assist.notice.dao.mapper.NoticeMapper;
import com.anyi.sparrow.assist.notice.domain.Notice;
import com.anyi.sparrow.assist.notice.domain.NoticeExample;
import com.anyi.sparrow.assist.notice.enums.NoticeStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class NoticeDao {


    @Autowired
    private NoticeMapper noticeMapper;


    public List<Notice> dataList(Integer bizType, Integer userType) {
        List<Integer> userTypes = new ArrayList<Integer>() {{
            add(userType);
        }};
        NoticeExample noticeExample = new NoticeExample();
        noticeExample.createCriteria().andStatusEqualTo(NoticeStatusEnum.ENABLE.getCode()).
                andBizTypeEqualTo(bizType).andEndTimeGreaterThanOrEqualTo(new Date()).andUserTypeIn(userTypes);
        noticeExample.setOrderByClause("sort_index");
        return noticeMapper.selectByExample(noticeExample);
    }


}
