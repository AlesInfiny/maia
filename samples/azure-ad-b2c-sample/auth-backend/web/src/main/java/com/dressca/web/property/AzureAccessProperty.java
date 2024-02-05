package com.dressca.web.property;

import org.springframework.beans.factory.annotation.Value;
import lombok.Getter;

/**
 * Azure AD B2Cの接続情報格納クラス。
 */
@Getter
public class AzureAccessProperty {
  @Value("${azure.graph.app.client-id}")
  private String clientId;

  @Value("${azure.graph.scope}")
  private String scope;

  @Value("${azure.graph.app.client-secret}")
  private String clientSecret;

  @Value("${azure.graph.grant-type}")
  private String clientCredentials;

  @Value("${azure.graph.get-token-endpoint}")
  private String tokenEndpoint;

  @Value("${azure.graph.user-endpoint}")
  private String userEndpoint;
}
