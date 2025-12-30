package com.anyi.sparrow.common;

import com.anyi.sparrow.common.utils.HexUtil;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final String Level_key = "channel_level";
    public static final String QL_BOX_VOLUME = "ql_box_volume";

    //安逸出行公司id
    public static Long LAN_HAI_CMP_ID = 100000000000000000L;

    //安逸出行管理部门id
    public static Long LAN_HAI_MANDEPT_ID = 100000000000000000L;

    //安逸出行管理部门code
    public static String LAN_HAI_MANDEPT_CODE = "1";

    //安逸出行队长id
    public static Long LAN_HAI_CAPTAIN_ID = 100000000000000000L;

    //物料中心部门id
    public static Long MAT_CENTER_DEPT_ID = 100000000000000100L;

    //物料中心人员id
    public static Long MAT_CENTER_EMP_ID = 100000000000000100L;

    //物料中心人员名字
    public static String MAT_CENTER_EMP_NAME = "安逸出行物料中心";

    //物料中心默认收货地址id
    public static Long MAT_CENTER_DEF_ADDRESS_ID = 200000000000000100l;

    //小程序自主推广部门
    public static Long LAN_HAI_USER_DEPT_ID = 100000000000000200L;

    //小程序自主推广部门code
    public static String LAN_HAI_USER_DEPT_CODE = "1-6";

    //小程序自主推广人员ID
    public static Long LAN_HAI_USER_EM_ID = 100000000000000200L;


    public static final String dict_name_cache_prefix = "dict:name:";

    public static final String bankNotSameKey = "bankNotSame";

    public static final String dict_type_cache_prefix = "dict:type:";

    public static final String cancel_reason_type = "cancel_reason";

    public static final String dict_sys_config_type = "sys_config";

    public static final String lanhai = "lanhai";

    public static final String partner_channel_pengwei = "pengwei";

    //齐鲁qrcode redis前缀
    public static final String ego_qrcode_redis_prfix = "pangu:egoqr:";


    public static final String erro_feedback_redis_prefix = "erro:feesback:";

    public static final String erro_redis_prefix = "erro:posmsg:";

    public static final Map<String, String> colorMap = new HashMap<String, String>() {{
        put("蓝色", "0");
        put("渐变绿色", "4");
    }};

    public static final String ccb_cache_prefix = "ccb:code:";
    public static final String ccb_not_valid = "ccb:not_valid";

    public static void main(String[] args) {
        String x = "CDEE4631323334343500000000000100002d121104020019040740500000000000000000000000000000000CDEE46313233343435000000000000005D645BB2";
        System.out.println(x.substring(0, 24));
        System.out.println(HexUtil.hexStringToString(x.substring(16, 20)));
    }

    public static final int PERCENT = 100;
    public static final String DECIMAL_FORMAT = "0.00";


}
