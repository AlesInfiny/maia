package com.example.auth.controller;

import com.example.auth.common.util.TokenUtil;
import com.example.auth.service.CallAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controllerクラス
 */
@Controller
public class AuthController {
    @Autowired
    private CallAPIService callAPIService;

    /**
     * ログイン時のメッセージを取得する
     *
     * @return "こんにちは %sさん"
     */
    @ResponseBody
    @GetMapping("/hello")
    @CrossOrigin
    public String hello(@RequestHeader(name="Authorization",required=true) String accessToken) throws Exception {

        String userId = TokenUtil.getObjectIdByAccessToken(accessToken);
        String displayUserName = callAPIService.getUser(userId);

        return String.format("こんにちは %sさん", displayUserName);
    }

    /**
     * ログイン中のユーザを削除する
     *
     * @return "success"
     */
    @ResponseBody
    @DeleteMapping("/users")
    @CrossOrigin
    public String deleteUser(
            @RequestHeader(name="Authorization",required=true) String accessToken) throws Exception{

        String userId = TokenUtil.getObjectIdByAccessToken(accessToken);
        callAPIService.deleteUser(userId);

        return String.format("sucess");
    }
}
