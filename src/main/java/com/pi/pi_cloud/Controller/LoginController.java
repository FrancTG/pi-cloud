package com.pi.pi_cloud.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "formLogin";
    }

    @PostMapping("/login")
    public String login(Model model) {

        /*

        ...

        if (login) {
            return "redirect:/home";
        }

        return "redirect:/login";
        */

        return "redirect:/home";

    }
}
