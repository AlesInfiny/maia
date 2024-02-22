package com.dressca.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * トークン共通処理。
 */
public class TokenDecoder {
  /**
   * クライアントから渡されたアクセストークンからobjectIdを取得。
   *
   * @param bearerToken アクセストークン
   * @return objectId
   */
  public static String getObjectIdByAccessToken(String bearerToken) {
    String jwtToken = bearerToken.substring(7);
    DecodedJWT jwt = JWT.decode(jwtToken);
    String objectId = jwt.getSubject();

    return objectId;
  }
}
