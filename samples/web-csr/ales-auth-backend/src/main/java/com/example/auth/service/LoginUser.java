package com.example.auth.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザクラス。GraphAPIのユーザ取得で使用
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    private String displayName;
}
