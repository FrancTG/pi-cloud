package com.pi.pi_cloud.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "formLogin";
    }

    @GetMapping("/dashboard")
    public String homePage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            // Si el usuario no tiene sesión activa, redirige al login
            return "redirect:/login";
        }

        model.addAttribute("email", email);
        return "home";
    }
}
