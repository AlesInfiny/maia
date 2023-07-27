package com.example.auth.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * トークン共通処理
 */
public class TokenUtil {


    /**
     * クライアントから渡されたアクセストークンからobejctIdの取得を行う.
     *
     * @param bearerToken アクセストークン
     * @return objectId
     */
    public static String getObjectIdByAccessToken(String bearerToken) {
        String jwtToken = bearerToken.substring(7);
        DecodedJWT jwt = JWT.decode(jwtToken);
        String obejctId = jwt.getSubject();

        return obejctId;
    }
}
