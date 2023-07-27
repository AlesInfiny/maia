package com.example.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Serviceクラス
 */
@Service
public class CallAPIService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${azure.graph.app.client-id}")
    private String clientId;

    @Value("${azure.graph.scope}")
    private String scope;

    @Value("${azure.graph.app.client-secret}")
    private String clientSecret;

    @Value("${azure.graph.grant-type}")
    private String clientCredentials;

    @Value("${azure.graph.get-token-endpoint}")
    private String getTokenEndpoint;

    @Value("${azure.graph.user-endpoint}")
    private String userEndpoint;

    /**
     * ログイン中のユーザ情報を取得する
     *
     * @param getUserId
     */
    public String getUser(String getUserId) throws Exception {

        String accessToken = getToken();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        String bearerToken = "Bearer " + accessToken;
        httpHeaders.add("Authorization", bearerToken);

        String getUserUrl = userEndpoint + getUserId;
        ResponseEntity<LoginUser> response = restTemplate.exchange(getUserUrl, HttpMethod.GET, new HttpEntity(httpHeaders), LoginUser.class);

        LoginUser responseUser = response.getBody();
        return responseUser.getDisplayName();
    }

    /**
     * ログイン中のユーザを削除する
     *
     * @param deleteUserId
     */
    public void deleteUser(String deleteUserId) throws Exception {

        String accessToken = getToken();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        String bearerToken = "Bearer " + accessToken;
        httpHeaders.add("Authorization", bearerToken);

        String deleteUrl = userEndpoint + deleteUserId;
        restTemplate.exchange(deleteUrl, HttpMethod.DELETE, new HttpEntity(httpHeaders), String.class);
    }

    /**
     * GraphAPI呼出に必要なアクセストークンを取得する
     *
     * @return accessToken
     */
    private String getToken() throws JsonProcessingException {

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("scope", scope);
        map.add("client_secret", clientSecret);
        map.add("grant_type", clientCredentials);

        RequestEntity request = RequestEntity.post(URI.create(getTokenEndpoint))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .body(map);

        String responseBody = restTemplate.exchange(request, String.class).getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        String accessToken = "";
        try {
            JsonNode json = objectMapper.readTree(responseBody);
            accessToken = json.get("access_token").textValue();
        }catch(JsonProcessingException ex) {
            throw ex;
        }
        return accessToken;
    }
}
