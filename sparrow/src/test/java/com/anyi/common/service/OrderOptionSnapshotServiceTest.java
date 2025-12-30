package com.anyi.common.service;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.product.domain.response.ProductDetailVO;
import com.anyi.common.product.service.OrderOptionSnapshotService;
import com.anyi.sparrow.SparrowApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/2
 * @Copyright
 * @Version 1.0
 */
public class OrderOptionSnapshotServiceTest extends SparrowApplicationTest {
    @Autowired
    OrderOptionSnapshotService service;
    @Autowired
    ProductManageService productManageService;

    @Test
    public void test() {
        Long productId = 1201655354553614336L;
        ProductDetailVO vo = productManageService.detailProduct(productId);
        List<Tree<Long>> optionals = (List<Tree<Long>>) vo.getOptionals();
        //System.out.println(JSONUtil.toJsonStr(optionals));

        Tree<Long> node = optionals.get(0).getNode(1202923993688817665L);
        System.out.println(JSONUtil.toJsonStr(node));
        System.out.println(TreeUtil.getParentsName(optionals.get(0).getNode(1202909878186524673L), true));
        System.out.println(TreeUtil.getParentsId(optionals.get(0).getNode(1202909878186524673L), true));
        //System.out.println(optionals.get(0).getNode(1202909878186524673L).getParentsName(true));
        //optionals.get(0).walk(tn -> {
        //    if (tn.get("type").equals(OptionTypeEnum.IMG.getCode())) {
        //        System.out.println(tn.getName() + " img-type");
        //    }
        //    if (tn.get("checked").equals(Boolean.TRUE)) {
        //        System.out.println(tn.getName() + " checked");
        //    }
        //});

        //OrderOptionSnapshot detail = OrderOptionSnapshot.builder()
        //        .orderId(2L)
        //        .detail(JSONUtil.toJsonStr(optionals))
        //        .build();
        //service.save(detail);

        //OrderOptionSnapshot detail = service.getById(1753297261164244993L);
        //TypeReference<List<Tree<Long>>> typeRef = new TypeReference<List<Tree<Long>>>() {
        //};
        //List<Tree<Long>> bean = JSONUtil.toBean(detail.getDetail(), typeRef.getType(), false);
        //List<Tree> list = JSONUtil.toList(detail.getDetail(), Tree.class);
        //
        //System.out.println(JSONUtil.toJsonStr(list));
        //System.out.println(JSONUtil.toJsonStr(optionals).equals(JSONUtil.toJsonStr(list)));
    }
}