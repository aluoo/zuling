package com.anyi.sparrow.assist.notice.service;

import com.alibaba.fastjson.JSONArray;
import com.anyi.sparrow.assist.notice.domain.Notice;
import com.anyi.sparrow.assist.notice.dao.NoticeDao;
import com.anyi.sparrow.assist.notice.vo.NoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public List<NoticeVo> homDataList() {
        return dataList(1, 1);
    }


    public List<NoticeVo> dataList(Integer bizType, Integer userType) {

        String strDataList = redisTemplate.opsForValue().get(new StringBuilder("noLs").append("_").append(bizType).append("_").append(userType).toString());
        if (strDataList == null) {
            List<NoticeVo> noticeVoList = new ArrayList<>();
            List<Notice> notices = noticeDao.dataList(bizType, userType);
            for (Notice notice : notices) {
                NoticeVo noticeVo = new NoticeVo();
                noticeVo.setContent(notice.getContent());
                noticeVoList.add(noticeVo);
            }
            cachNoticeList(bizType, userType, noticeVoList);
            return noticeVoList;
        } else {
            List<NoticeVo> noticeVoList = JSONArray.parseArray(strDataList).toJavaList(NoticeVo.class);
            return noticeVoList;
        }
    }


    private void cachNoticeList(Integer bizType, Integer userType, List<NoticeVo> noticeList) {

        String key = new StringBuilder("noLs").append("_").append(bizType).append("_").append(userType).toString();
        redisTemplate.opsForValue().set(key, JSONArray.toJSONString(noticeList), 30, TimeUnit.MINUTES);
    }

}
