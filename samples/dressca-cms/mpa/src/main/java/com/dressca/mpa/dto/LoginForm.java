package com.dressca.mpa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {
    @NotBlank(message = "ユーザー名は必須です")
    @Email(message = "有効なメールアドレスを入力してください")
    private String username;

    @NotBlank(message = "パスワードは必須です")
    private String password;
}
