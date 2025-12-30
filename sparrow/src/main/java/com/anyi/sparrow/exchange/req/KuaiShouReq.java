package com.anyi.sparrow.exchange.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class KuaiShouReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idfa;

    private String caid;

    private String os;

    private String model;

    private String ip;

    private String timestamp;

    private Integer app = 1;


}
