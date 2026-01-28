package com.dressca.cms.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.dressca.cms.web.models.base.LoginViewModel;

/**
 * ログイン画面のコントローラー。
 */
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class LoginController {

  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

  /**
   * ログイン画面を表示します。
   * 
   * @param email メールアドレス（クエリ文字列から取得、省略可）。
   * @param model モデル。
   * @return ログイン画面のビュー名。
   */
  @GetMapping("/login")
  public String index(@RequestParam(value = "email", required = false, defaultValue = "") String email, Model model) {
    LoginViewModel viewModel = new LoginViewModel();
    viewModel.setEmail(email);
    model.addAttribute("viewModel", viewModel);
    return "authentication/login";
  }

  /**
   * ログイン処理を行います。
   * 
   * @param viewModel     ログイン画面のビューモデル。
   * @param bindingResult バインディング結果。
   * @param returnUrl     ログイン後に遷移する URL（クエリ文字列から取得、省略可）。
   * @param request       HTTP リクエスト。
   * @param model         モデル。
   * @return リダイレクト先の URL またはログイン画面のビュー名。
   */
  @PostMapping("/login")
  public String login(@Validated @ModelAttribute("viewModel") LoginViewModel viewModel,
      BindingResult bindingResult, @RequestParam(value = "return-url", required = false) String returnUrl,
      HttpServletRequest request, Model model) {

    if (bindingResult.hasErrors()) {
      return "authentication/login";
    }

    try {
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          viewModel.getEmail(), viewModel.getPassword());
      Authentication authentication = authenticationManager.authenticate(authenticationToken);

      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(authentication);
      SecurityContextHolder.setContext(context);
      securityContextRepository.saveContext(context, request, null);

      if (returnUrl != null && !returnUrl.isEmpty()
          && returnUrl.startsWith("/")
          && !returnUrl.startsWith("//")
          && !returnUrl.contains("\n")
          && !returnUrl.contains("\r")) {
        return "redirect:" + returnUrl;
      }
      return "redirect:/top";

    } catch (BadCredentialsException e) {
      bindingResult.reject("authentication.login.authenticationFailed");
      model.addAttribute("viewModel", viewModel);
      return "authentication/login";
    }
  }
}
