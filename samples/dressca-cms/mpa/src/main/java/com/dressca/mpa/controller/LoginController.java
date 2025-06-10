package com.dressca.mpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dressca.mpa.dto.LoginForm;

import jakarta.validation.Valid;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginForm") LoginForm loginForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        // 認証処理をここに記述
        return "redirect:/index";
    }
}
