package com.dressca.web.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GraphAPIのユーザ取得で使用するユーザクラス。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
  private String displayName;
}