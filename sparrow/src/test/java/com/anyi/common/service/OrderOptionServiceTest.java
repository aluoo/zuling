package com.anyi.common.service;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.product.domain.enums.OptionCodeEnum;
import com.anyi.common.product.service.OptionService;
import com.anyi.common.product.service.OrderOptionService;
import com.anyi.common.product.service.OrderOptionSnapshotService;
import com.anyi.sparrow.SparrowApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/29
 * @Copyright
 * @Version 1.0
 */
public class OrderOptionServiceTest extends SparrowApplicationTest {
    @Autowired
    OrderOptionService service;
    @Autowired
    OrderOptionSnapshotService snapshotService;
    @Autowired
    private OptionService optionService;

    @Test
    public void test3() {
        List<Tree<Long>> trees = optionService.buildOptionTreeBaseByProductId(1201655355887403008L);
        Tree<Long> node = trees.get(0).getNode(1202923988387217408L);
        System.out.println(node.getParentsName(true));
        System.out.println(node.get("code"));
        System.out.println(node.get("level"));
        String funcTitle = getOptionTitleByCode(node, OptionCodeEnum.FUNC_TITLE);
        System.out.println(funcTitle);

        Tree<Long> rootNode = getRootNode(node);
        System.out.println(rootNode.getName());
        Tree<Long> mch = getParentByCode(node, OptionCodeEnum.MCHSTAT);
        String mch_stat = getOptionTitleByCode(mch, OptionCodeEnum.MCHSTAT_TITLE);
        System.out.println(mch_stat);
        System.out.println(mch.toString());
    }

    private String getOptionTitleByCode(Tree<Long> node, OptionCodeEnum codeEnum) {
        String code = (String) node.get("code");
        if (StrUtil.isBlank(code)) {
            return null;
        }
        if (code.equals(codeEnum.getCode())) {
            return node.getName().toString();
        }
        Tree<Long> parent = node.getParent();
        if (parent == null) {
            return null;
        }
        return getOptionTitleByCode(parent, codeEnum);
    }

    private Tree<Long> getRootNode(Tree<Long> node) {
        if (node == null) {
            return null;
        }
        Tree<Long> parent = node.getParent();
        if (parent == null || StrUtil.isBlank(parent.getName())) {
            return node;
        }
        return getRootNode(parent);
    }

    private Tree<Long> getParentByCode(Tree<Long> node, OptionCodeEnum codeEnum) {
        String code = (String) node.get("code");
        if (StrUtil.isBlank(code)) {
            return null;
        }
        if (code.equals(codeEnum.getCode())) {
            return node;
        }
        Tree<Long> parent = node.getParent();
        if (parent == null || StrUtil.isBlank(parent.getName())) {
            return null;
        }
        return getParentByCode(parent, codeEnum);
    }
}