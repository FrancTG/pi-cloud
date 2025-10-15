package com.pi.pi_cloud.Controller;

import com.pi.pi_cloud.Model.User;
import com.pi.pi_cloud.Service.UserService;
import com.pi.pi_cloud.dto.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public String usersList(Model model) {
        model.addAttribute("users",userService.getAllUsers());
        return "usersList";
    }

    @PostMapping("/user")
    public String addUser(@ModelAttribute UserData userData) {
        userService.addUser(userData);

        return "redirect:/home";
    }
}
