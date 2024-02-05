package com.dressca.web.controller;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import com.dressca.web.property.AzureAccessProperty;
import com.dressca.web.util.LoginUser;
import com.dressca.web.util.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Azure AD B2Cに接続するためのコントローラークラス。
 */
@RestController
public class AuthController {

  @Autowired
  private RestTemplate restTemplate;

  private AzureAccessProperty azureAccessConstant = new AzureAccessProperty();

  /**
   * ログイン時のメッセージ取得。
   * 
   * @param accessToken トークン
   * @return レスポンス
   * @throws Exception 例外
   */
  @GetMapping("/get_auth")
  public ResponseEntity<LoginUser> get(@RequestHeader(name = "Authorization", required = true) String accessToken)
      throws Exception {

    String userId = TokenUtil.getObjectIdByAccessToken(accessToken);
    HttpHeaders httpHeaders = new HttpHeaders();
    String bearerToken = "Bearer " + getTokenFromAzure();
    httpHeaders.add("Authorization", bearerToken);
    String userUrl = azureAccessConstant.getUserEndpoint() + userId;
    ResponseEntity<LoginUser> response = restTemplate.exchange(userUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders),
        LoginUser.class);

    return response;
  }

  /**
   * ログイン中のユーザ削除。
   * 
   * @param accessToken トークン
   * @return レスポンス
   * @throws Exception 例外
   */
  @DeleteMapping("/delete_auth")
  public ResponseEntity<String> delete(@RequestHeader(name = "Authorization", required = true) String accessToken)
      throws Exception {

    String userId = TokenUtil.getObjectIdByAccessToken(accessToken);
    HttpHeaders httpHeaders = new HttpHeaders();
    String bearerToken = "Bearer " + getTokenFromAzure();
    httpHeaders.add("Authorization", bearerToken);
    String userUrl = azureAccessConstant.getUserEndpoint() + userId;
    ResponseEntity<String> response = restTemplate.exchange(userUrl, HttpMethod.DELETE, new HttpEntity<>(httpHeaders),
        String.class);
    return response;
  }

  private String getTokenFromAzure() throws JsonProcessingException {
    MultiValueMap<String, String> tokenMap = new LinkedMultiValueMap<>();
    tokenMap.add("client_id", azureAccessConstant.getClientId());
    tokenMap.add("scope", azureAccessConstant.getScope());
    tokenMap.add("client_secret", azureAccessConstant.getClientSecret());
    tokenMap.add("grant_type", azureAccessConstant.getClientCredentials());

    RequestEntity<MultiValueMap<String, String>> request = RequestEntity
        .post(URI.create(azureAccessConstant.getTokenEndpoint()))
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_FORM_URLENCODED)
        .body(tokenMap);

    String responseBody = restTemplate.exchange(request, String.class).getBody();
    JsonNode json = new ObjectMapper().readTree(responseBody);
    return json.get("access_token").textValue();
  }

}
