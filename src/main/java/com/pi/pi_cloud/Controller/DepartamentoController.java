package com.pi.pi_cloud.Controller;

import com.pi.pi_cloud.Model.Departamento;
import com.pi.pi_cloud.Model.Departamento;
import com.pi.pi_cloud.Service.DepartamentoService;
import com.pi.pi_cloud.Service.OrganizacionService;
import com.pi.pi_cloud.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
public class DepartamentoController {

    @Autowired
    UserService userService;

    @Autowired
    DepartamentoService depService;

    @Autowired
    OrganizacionService orgService;

    @GetMapping("/departamentos")
    public String usersList(Model model, HttpSession session) {
        //var users = userService.getAllUsers();
        var deps = depService.getAllDepartments();
        var orgs = orgService.getAllOrgs();

        //model.addAttribute("users",users);
        model.addAttribute("deps",deps);
        model.addAttribute("orgs",orgs);

        String sessionEmail = (String) session.getAttribute("email");
        if (sessionEmail != null) {
            model.addAttribute("email",sessionEmail);
        }

        return "departamentos";
    }

//    @GetMapping("/eliminardep")
//    public RedirectView eliminar(Departamento departamento) {
//        depService.eliminarDepartamento(departamento);
//        return new RedirectView("/departamentos");
//    }

    @GetMapping("/eliminardep")
    public RedirectView eliminar(@RequestParam("id") Long id) {
        depService.eliminarDepartamentoPorId(id);
        return new RedirectView("/departamentos");
    }

    @PostMapping("/dep/guardar")
    public RedirectView guardar(@Valid Departamento departamento, Errors errores) throws IOException {

        if (errores.hasErrors()) {
            return new RedirectView("/departamentos");
        }

        depService.guardar(departamento);


        return new RedirectView("/departamentos");
    }
}
