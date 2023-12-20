package com.ryanshores.blog.controllers;

import com.ryanshores.blog.models.Account;
import com.ryanshores.blog.services.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final AccountService accountService;

    public RegisterController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("account", new Account());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute Account account) {
        accountService.save(account);

        return "redirect:/";
    }
}
