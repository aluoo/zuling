package com.anyi.sparrow.cyx.constant;

/**
 * @author shenbh
 * @date 2022/4/28 10:32
 */
public class SignInfoConstants {

   //头部签名key
    public final static String SIGN_HEAD_NAME="Sign-Info";

    //redis 签名密钥key
    public final static String SIGN_LIST_KEY="sign_list";

    //验证成功后的签名头，传输至下游
    public final static String SIGN_HEAD_SUCCESS="sign_info_head";

   public final static String PARAMS_HEAD="params_head";

   public final static String SIGN_REDIS_KEY="openapi:appid:%s";

    public final static String REQUEST_START_TIME="request_start_time";


    public final static String CYX_APP_ID="cyx00001";
    public final static String CYX_APP_SECRETKEY="DBbw2hIB4saGGAGj";

//    public static void main(String[] args) {
//        System.out.println( RandomUtil.randomStr(16));
//    }
}
