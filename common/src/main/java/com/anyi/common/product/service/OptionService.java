package com.anyi.common.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.product.domain.Option;
import com.anyi.common.product.domain.OrderOption;
import com.anyi.common.product.domain.dto.OptionDTO;
import com.anyi.common.product.domain.enums.OptionCodeEnum;
import com.anyi.common.product.mapper.OptionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OptionService extends ServiceImpl<OptionMapper, Option> {

    public List<Tree<Long>> buildOptionTree() {
        return buildOptionTreeBase(null);
    }

    public List<Tree<Long>> buildOptionTree(List<Long> optionIds) {
        return buildOptionTreeBase(optionIds);
    }

    private List<Tree<Long>> buildOptionTreeBase(List<Long> optionIds) {
        TreeNodeConfig treeNodeConfig = getTreeConfig();
        List<OptionDTO> options = listAllAvailableOption();
        boolean hasOption = CollUtil.isNotEmpty(optionIds);
        return TreeUtil.build(options, -1L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setName(treeNode.getName());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getSort());// 倒序需要取反排序值
            tree.putExtra("type", treeNode.getType());
            tree.putExtra("required", treeNode.getRequired());
            tree.putExtra("level", treeNode.getLevel());
            //tree.putExtra("ancestors", treeNode.getAncestors());
            if (hasOption) {
                tree.putExtra("checked", optionIds.contains(treeNode.getId()));
            } else {
                tree.putExtra("checked", false);
            }
        });
    }

    public List<Tree<Long>> buildOptionTreeBaseByProductId(Long productId) {
        TreeNodeConfig treeNodeConfig = getTreeConfig();
        List<OptionDTO> options = listAvailableOptionByProductId(productId);
        return TreeUtil.build(options, -1L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setName(treeNode.getName());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getSort());// 倒序需要取反排序值
            tree.putExtra("type", treeNode.getType());
            tree.putExtra("required", treeNode.getRequired());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("description", treeNode.getDescription());
            tree.putExtra("checked", false);
            tree.putExtra("sampleImage", treeNode.getSampleImage());
            tree.putExtra("code", treeNode.getCode());
        });
    }

    public List<Tree<Long>> buildOptionTreeBaseByProductIdFillValueFromExistOrder(Long productId, List<OrderOption> allOptionsByOrderId) {
        TreeNodeConfig treeNodeConfig = getTreeConfig();
        List<OptionDTO> options = listAvailableOptionByProductId(productId);
        Map<Long, OrderOption> collect = allOptionsByOrderId.stream().collect(Collectors.toMap(OrderOption::getOptionId, Function.identity()));
        List<Tree<Long>> t = TreeUtil.build(options, -1L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setName(treeNode.getName());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getSort());// 倒序需要取反排序值
            tree.putExtra("type", treeNode.getType());
            tree.putExtra("required", treeNode.getRequired());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("description", treeNode.getDescription());
            tree.putExtra("checked", false);
            tree.putExtra("sampleImage", treeNode.getSampleImage());
            tree.putExtra("code", treeNode.getCode());
            OrderOption orderOption = collect.get(tree.getId());
            if (orderOption != null) {
                tree.putExtra("checked", true);
                tree.putExtra("value", orderOption.getValue());
            }
        });
        allOptionsByOrderId.forEach(o -> {
            if (o.getCode().equals(OptionCodeEnum.FUNC_ERR.getCode())) {
                Tree<Long> parentNode = t.get(0).getNode(o.getOptionId()).getParent();
                parentNode.putExtra("checked", true);
            }
        });
        return t;
    }

    private List<OptionDTO> listAllAvailableOption() {
        LambdaQueryWrapper<Option> qw = new LambdaQueryWrapper<Option>()
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByAsc(Option::getLevel)
                .orderByDesc(Option::getSort);
        return BeanUtil.copyToList(this.list(qw), OptionDTO.class);
    }

    private List<OptionDTO> listAvailableOptionByProductId(Long productId) {
        List<Option> options = this.getBaseMapper().listOptionsByProductId(productId);
        return BeanUtil.copyToList(options, OptionDTO.class);
    }

    private TreeNodeConfig getTreeConfig() {
        // build tree
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        return treeNodeConfig;
    }
}