package com.anyi.sparrow.third.shumai;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.anyi.common.advice.BizError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/10/9
 */
@Slf4j
@Component
public class ShuMaiRealNameVerifyApi {
    private static final String URL = "https://eid.shumaidata.com/eid/check";
    private static final String APP_CODE = "b1a73d35367541b4a5261bac5647d4db";
    private static final String APP_KEY = "204140006";
    private static final String APP_SECRET = "zQ2cbRwZvom8M8I4jIe69atM0wWxOGwJ";

    private static final String MATCH = "1";
    private static final String NOT_MATCH = "2";
    private static final String NOT_FOUND = "3";
    /**
     *   温馨提示：
     *   1.解析结果时，先判断code
     *   2.出现'无记录'时，有以下几种原因
     *       (1)现役军人、武警官兵、特殊部门人员及特殊级别官员；
     *       (2)退役不到2年的军人和士兵（根据军衔、兵种不同，时间会有所不同，一般为2年）；
     *       (3)户口迁出，且没有在新的迁入地迁入；
     *       (4)户口迁入新迁入地，当地公安系统未将迁移信息上报到公安部（上报时间地域不同而有所差异）；
     *       (5)更改姓名，当地公安系统未将更改信息上报到公安部（上报时间因地域不同而有所差异）；
     *       (6)移民；
     *       (7)未更换二代身份证；
     *       (8)死亡。
     *       (9)身份证号确实不存在
     *   {
     *       "code": "0", //返回码，0：成功，非0：失败（详见错误码定义）
     *             //当code=0时，再判断下面result中的res；当code!=0时，表示调用已失败，无需再继续
     *       "message": "成功", //返回码说明
     *       "result": {
     *           "name": "冯天", //姓名
     *           "idcard": "350301198011129422", //身份证号
     *           "res": "1", //核验结果状态码，1 一致；2 不一致；3 无记录
     *           "description": "一致",  //核验结果状态描述
     *          "sex": "男",
     *           "birthday": "19940320",
     *           "address": "江西省南昌市东湖区"
     *       }
     *   }
     **/
    public static Response verify(String name, String idCard) {
        Map<String, String> params = new HashMap<>();
        params.put("idcard", idCard);
        params.put("name", name);
        HttpRequest request = HttpUtil.createRequest(Method.POST, URL);
        request.header("Authorization", "APPCODE " + APP_CODE);
        request.form(JSONUtil.parseObj(params));
        Response resp;
        try (HttpResponse response = request.execute()) {
            log.info("ShuMaiRealNameVerifyApi.verify.info: response.body-{}", response.body());
            log.info("ShuMaiRealNameVerifyApi.verify.info: response is ok - {}", response.isOk());
            if (!response.isOk() || Objects.isNull(response.body())) {
                log.error("ShuMaiRealNameVerifyApi.verify.error: response is not ok, status-{}", response.getStatus());
                return Response.fail(BizError.NET_ERROR.getMessage());
            }
            String respBody = response.body();
            resp = JSONUtil.toBean(respBody, Response.class);
            if (resp == null) {
                resp = Response.fail();
            }
        }
        return resp;
    }

    public static void main(String[] args) {
        String name = "李聪";
        String idCard = "340403199902252814";
        Response response = verify(name, idCard);
        System.out.println(response.isSuccess());
        System.out.println(JSONUtil.toJsonStr(response));
        System.out.println(Response.checkVerifyRes(response));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer code;
        private String message;
        private Result result;

        public Boolean isSuccess() {
            return this.code != null && this.code == 0;
        }

        public static Response fail() {
            return fail("失败");
        }

        public static Response fail(String msg) {
            return Response.builder().code(-1).message(msg).build();
        }

        public static Boolean checkVerifyRes(Response response) {
            if (response.getResult() == null) {
                return false;
            }
            return MATCH.equals(response.getResult().getRes());
        }

        public static Integer getVerifyRes(Response response) {
            if (response.getResult() == null) {
                return Integer.valueOf(NOT_FOUND);
            }
            return Integer.valueOf(response.getResult().getRes());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class Result implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private String idcard;
        private String res;
        private String description;
        private String sex;
        private String birthday;
        private String address;
    }
}