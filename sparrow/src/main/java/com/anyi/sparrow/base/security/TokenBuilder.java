package com.anyi.sparrow.base.security;

import java.util.UUID;

public class TokenBuilder {
    /**
     *
     * @param userType
     * @return
     */
    public static String build(int userType) {
        String token = UUID.randomUUID().toString().replace("-", "");
        return token + userType;
    }

    /**
     *
     * @param token
     * @return
     */
    public static int parseUserType(String token) {
       return Integer.parseInt(token.substring(token.length() - 1, token.length()));
    }
}
