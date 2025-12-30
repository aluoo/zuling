package com.anyi.common.domain.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/5/24
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppReviewControlConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String os;
    private String appVersion;

    @AllArgsConstructor
    public enum OS {
        iOS("iOS", "iOS"),
        ANDROID("android", "android"),
        ALL("ALL", "ALL"),
        ;
        @Getter
        private final String name;
        @Getter
        private final String desc;
    }
}