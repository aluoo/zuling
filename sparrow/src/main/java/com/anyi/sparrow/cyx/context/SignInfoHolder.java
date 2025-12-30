package com.anyi.sparrow.cyx.context;

import com.anyi.sparrow.cyx.model.SignInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Kern
 */
@Slf4j
public class SignInfoHolder {

    private static final ThreadLocal<SignInfo> CLIENT_ID_HOLDER = new ThreadLocal<>();

    public static void setSignInfo(SignInfo signInfo) {
        CLIENT_ID_HOLDER.set(signInfo);
    }

    public static SignInfo getSignInfo() {
        return CLIENT_ID_HOLDER.get();
    }


    public static void removeSignInfo() {
        CLIENT_ID_HOLDER.remove();
    }
}
