package com.dressca.web.admin.controller.dto.user;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ユーザー情報を取得する際に用いる dto クラスです。
 */
@Data
@AllArgsConstructor
public class GetLoginUserResponse {
  @NotNull
  private String userName = "";
  @NotNull
  private List<String> roles = new ArrayList<>();
}
