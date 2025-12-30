package com.anyi.common.product.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.product.domain.OrderOption;
import com.anyi.common.product.domain.enums.OptionCodeEnum;
import com.anyi.common.product.mapper.OrderOptionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/26
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderOptionService extends ServiceImpl<OrderOptionMapper, OrderOption> {
    public static final String OPTION_CODE = "code";

    public OrderOption buildMachineStatusOptionInfo(Tree<Long> node, OptionCodeEnum codeEnum, long id, long orderId) {
        Tree<Long> machineStatus = getParentOptionByCode(node, codeEnum);
        if (machineStatus == null) {
            return null;
        }
        OrderOption oi = OrderOption.builder()
                .id(id)
                .orderId(orderId)
                .optionId(machineStatus.getId())
                .code((String) machineStatus.get(OPTION_CODE))
                .value(machineStatus.getName().toString())
                .build();
        String title = getOptionTitleByCode(machineStatus, OptionCodeEnum.MCHSTAT_TITLE);
        oi.setTitle(title);
        return oi;
    }

    public Tree<Long> getParentOptionByCode(Tree<Long> node, OptionCodeEnum codeEnum) {
        String code = (String) node.get(OPTION_CODE);
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
        return getParentOptionByCode(parent, codeEnum);
    }

    public String getOptionTitleByCode(Tree<Long> node, OptionCodeEnum codeEnum) {
        String code = (String) node.get(OPTION_CODE);
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

    public List<OrderOption> getAllOptionsByOrderId(Long orderId) {
        if (orderId == null) {
            return new ArrayList<>();
        }
        return this.lambdaQuery()
                .eq(OrderOption::getOrderId, orderId)
                .orderByAsc(OrderOption::getCreateTime)
                .list();
    }

    public List<OrderOption> listAllOptionsByOrderIds(List<Long> orderIds, List<String> codes) {
        if (CollUtil.isEmpty(orderIds)) {
            return new ArrayList<>();
        }
        return this.lambdaQuery()
                .in(OrderOption::getOrderId, orderIds)
                .in(CollUtil.isNotEmpty(codes), OrderOption::getCode, codes)
                .orderByAsc(OrderOption::getCreateTime)
                .list();
    }

    public OrderOption getByCode(List<OrderOption> list, OptionCodeEnum code) {
        if (CollUtil.isEmpty(list) || code == null) {
            return null;
        }
        return list.stream()
                .filter(Objects::nonNull)
                .filter(o -> o.getCode().equals(code.getCode()))
                .findFirst()
                .orElse(null);
    }

    public List<String> listValueByCode(List<OrderOption> list, OptionCodeEnum code) {
        if (CollUtil.isEmpty(list) || code == null) {
            return new ArrayList<>();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .filter(o -> o.getCode().equals(code.getCode()))
                .map(OrderOption::getValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<OrderOption> listByCode(List<OrderOption> list, List<OptionCodeEnum> codes) {
        if (CollUtil.isEmpty(list) || CollUtil.isEmpty(codes)) {
            return new ArrayList<>();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .filter(o -> codes.stream().anyMatch(c -> c.getCode().equals(o.getCode())))
                .collect(Collectors.toList());
    }

    public Map<Long, List<String>> buildOrderImagesInfoMap(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<String> codes = Arrays.asList(
                OptionCodeEnum.IMG.getCode(),
                OptionCodeEnum.OTHER_IMG.getCode()
        );
        List<OrderOption> list = listAllOptionsByOrderIds(ids, codes);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        // 将list转换为Map<Long, List<String>>形式，Long为list的orderId，List<String>为list的value，也就是图片列表
        return list.stream()
                .filter(Objects::nonNull)
                .filter(o -> StrUtil.isNotBlank(o.getValue()))
                .collect(Collectors.groupingBy(OrderOption::getOrderId,
                        Collectors.mapping(OrderOption::getValue, Collectors.toList())));
    }

    public Map<Long, String> buildOrderSpecInfoMap(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<String> codes = Arrays.asList(
                OptionCodeEnum.ROM.getCode(),
                OptionCodeEnum.RAM.getCode(),
                OptionCodeEnum.COLOR.getCode()
        );
        List<OrderOption> list = listAllOptionsByOrderIds(ids, codes);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        // 将list转换为Map<Long, List<OrderOption>>形式
        Map<Long, List<OrderOption>> collect = buildOrderOptionInfoMap(list);
        if (CollUtil.isEmpty(collect)) {
            return Collections.emptyMap();
        }
        Map<Long, String> resp = new HashMap<>(1);
        collect.forEach((orderId, values) -> {
            Map<String, String> codeMap = values.stream().collect(Collectors.toMap(OrderOption::getCode, OrderOption::getValue));
            String str = buildSpecInfoStr(codeMap);
            if (StrUtil.isNotBlank(str)) {
                resp.put(orderId, str);
            }
        });
        return resp;
    }

    public String buildOrderSpecInfo(List<OrderOption> list) {
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        List<String> codes = Arrays.asList(
                OptionCodeEnum.ROM.getCode(),
                OptionCodeEnum.RAM.getCode(),
                OptionCodeEnum.COLOR.getCode()
        );
        Map<String, String> codeMap = list.stream()
                .filter(o -> StrUtil.isNotBlank(o.getCode()) && codes.contains(o.getCode()))
                .collect(Collectors.toMap(OrderOption::getCode, OrderOption::getValue));
        return buildSpecInfoStr(codeMap);
    }

    private Map<Long, List<OrderOption>> buildOrderOptionInfoMap(List<OrderOption> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(OrderOption::getOrderId));
    }

    private String buildSpecInfoStr(Map<String, String> codeMap) {
        Optional<String> ram = Optional.ofNullable(codeMap.get(OptionCodeEnum.RAM.getCode()));
        Optional<String> rom = Optional.ofNullable(codeMap.get(OptionCodeEnum.ROM.getCode()));
        Optional<String> color = Optional.ofNullable(codeMap.get(OptionCodeEnum.COLOR.getCode()));
        return Stream.of(ram, rom, color)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(prop -> prop + "+")
                .reduce("", (a, b) -> a + b)
                // 移除末尾的 +
                .replaceAll("\\+$", "");
    }

}