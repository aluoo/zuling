package com.anyi.sparrow.applet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author peng can
 * @date 2022/12/13
 */
@Getter
@AllArgsConstructor
public enum CreditState {

    ADD(2,"增加"),
    REDUCE(1,"减少");

    Integer state;

    String name;
}
