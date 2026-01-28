package com.dressca.cms.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * トップ画面のコントローラー。
 */
@Controller
public class HomeController {

  /**
   * トップ画面を表示します。
   * 
   * @return トップ画面のビュー名。
   */
  @GetMapping({ "/", "/top" })
  public String top() {
    return "top";
  }
}
