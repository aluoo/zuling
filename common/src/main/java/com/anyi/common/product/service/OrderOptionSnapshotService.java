package com.anyi.common.product.service;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.product.domain.OrderOptionSnapshot;
import com.anyi.common.product.mapper.OrderOptionSnapshotMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/2
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderOptionSnapshotService extends ServiceImpl<OrderOptionSnapshotMapper, OrderOptionSnapshot> {

    @Transactional(rollbackFor = Exception.class)
    public void saveSnapshot(Long orderId, List<Tree<Long>> optionals) {
        OrderOptionSnapshot detail = OrderOptionSnapshot.builder()
               .orderId(orderId)
               .detail(JSONUtil.toJsonStr(optionals))
               .build();
        this.save(detail);
    }

    public List<Tree<Long>> getTreeByOrderId(Long orderId) {
        if (orderId == null) {
            return null;
        }
        OrderOptionSnapshot snapshot = this.lambdaQuery().eq(OrderOptionSnapshot::getOrderId, orderId).one();
        if (snapshot == null || StrUtil.isBlank(snapshot.getDetail())) {
            return null; // null or empty Tree?
        }
        return buildTree(snapshot.getDetail());
    }

    private List<Tree<Long>> buildTree(String json) {
        TypeReference<List<Tree<?>>> typeRef = new TypeReference<List<Tree<?>>>() {
        };
        return JSONUtil.toBean(json, typeRef.getType(), false);
    }
}