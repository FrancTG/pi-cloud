package com.pi.pi_cloud.Controller;

import com.pi.pi_cloud.Model.Usuario;
import com.pi.pi_cloud.Service.DepartamentoService;
import com.pi.pi_cloud.Service.UserService;
import com.pi.pi_cloud.dto.RegisterRequestDTO;
import com.pi.pi_cloud.dto.UserData;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    DepartamentoService depService;

    //@Autowired
    RegisterRequestDTO userRegister;

    @GetMapping("/users")
    public String usersList(Model model, HttpSession session) {
        var users = userService.getAllUsers();
        var deps = depService.getAllDepartments();

        model.addAttribute("users",users);
        model.addAttribute("deps",deps);

        String sessionEmail = (String) session.getAttribute("email");
        if (sessionEmail != null) {
            model.addAttribute("email",sessionEmail);
        }

        return "panel";
    }

    @PostMapping("/user")
    public String addUser(@ModelAttribute UserData userData) {
        userService.addUser(userData);
        return "redirect:/home";
    }

    @GetMapping("/eliminaruser")
    public RedirectView eliminar(Usuario usuario) {
        userService.eliminarUsuario(usuario);
        return new RedirectView("/users");
    }

    @PostMapping("/user/guardar")
    public RedirectView guardar(@Valid RegisterRequestDTO usuario, Errors errores) throws Exception {

        if (errores.hasErrors()) {
            return new RedirectView("/users");
        }

        userService.registerUser(usuario);


        return new RedirectView("/users");
    }
}
