package com.anyi.common.insurance.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.insurance.domain.DiInsuranceOrder;
import com.anyi.common.insurance.domain.DiOption;
import com.anyi.common.insurance.mapper.DiOptionMapper;
import com.anyi.common.insurance.response.DiOptionDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 数保产品选项表 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Service
public class DiOptionService extends ServiceImpl<DiOptionMapper, DiOption> {

    public List<Tree<Long>> buildOptionTree() {
        return buildOptionTreeBase(null);
    }

    public List<Tree<Long>> buildOptionTree(List<Long> optionIds) {
        return buildOptionTreeBase(optionIds);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long optionId) {
        boolean success = this.lambdaUpdate()
                .set(DiOption::getDeleted, true)
                .eq(DiOption::getId, optionId)
                .eq(DiOption::getDeleted, false)
                .update(new DiOption());
        if (success) {
            String ancestors = StrUtil.format("{},", optionId);
            this.lambdaUpdate()
                    .set(DiOption::getDeleted, true)
                    .likeRight(DiOption::getAncestors, ancestors)
                    .eq(DiOption::getDeleted, false)
                    .update(new DiOption());
        }
    }

    private List<Tree<Long>> buildOptionTreeBase(List<Long> optionIds) {
        TreeNodeConfig treeNodeConfig = getTreeConfig();
        List<DiOptionDTO> options = listAllAvailableOption(optionIds);
        boolean hasOption = CollUtil.isNotEmpty(optionIds);
        return TreeUtil.build(options, -1L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setName(treeNode.getName());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getSort());// 倒序需要取反排序值
            tree.putExtra("type", treeNode.getType());
            tree.putExtra("required", treeNode.getRequired());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("description", treeNode.getDescription());
            //tree.putExtra("ancestors", treeNode.getAncestors());
            /*if (hasOption) {
                tree.putExtra("checked", optionIds.contains(treeNode.getId()));
            } else {
                tree.putExtra("checked", false);
            }*/
        });
    }

    private List<DiOptionDTO> listAllAvailableOption(List<Long> optionIds) {
        LambdaQueryWrapper<DiOption> qw = new LambdaQueryWrapper<DiOption>()
                .eq(DiOption::getDeleted, false)
                .in(CollUtil.isNotEmpty(optionIds),DiOption::getId,optionIds)
                .orderByAsc(DiOption::getLevel)
                .orderByDesc(DiOption::getSort);
        return BeanUtil.copyToList(this.list(qw), DiOptionDTO.class);
    }

    private TreeNodeConfig getTreeConfig() {
        // build tree
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        return treeNodeConfig;
    }

    public Map<Long, DiOption> getOptionInfoMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<DiOption> list = this.lambdaQuery().in(DiOption::getId, ids).list();
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(DiOption::getId, Function.identity()));
    }

}
