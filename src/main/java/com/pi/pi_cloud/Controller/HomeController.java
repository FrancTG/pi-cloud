package com.pi.pi_cloud.Controller;

import com.pi.pi_cloud.Model.Departamento;
import com.pi.pi_cloud.Model.Fichero;
import com.pi.pi_cloud.Model.Organizacion;
import com.pi.pi_cloud.Model.Usuario;
import com.pi.pi_cloud.Service.FicheroService;
import com.pi.pi_cloud.Service.UserService;
import com.pi.pi_cloud.dto.FicheroData;
import com.pi.pi_cloud.repository.DepartamentoRepository;
import com.pi.pi_cloud.repository.OrganizacionRepository;
import com.pi.pi_cloud.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    FicheroService ficheroService;

    @Autowired
    OrganizacionRepository organizacionRepository;

    @Autowired
    DepartamentoRepository departamentoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    // Usuario logueado
    private Usuario user;

    @GetMapping("/")
    public String rootGet() {
        return "redirect:/login";
    }

    @GetMapping("home")
    public String home(Model model, HttpSession session) {

        String sessionEmail = (String) session.getAttribute("email");
        if (sessionEmail == null) {
            return "home";
        }

        Usuario usuario = userRepository.findByEmail(sessionEmail.toString()).orElse(null);
        List<FicheroData> ficheros = userService.getFicherosFromUsuario(usuario.getId());

        if (!ficheros.isEmpty()) {
            model.addAttribute("ficheros",ficheros);
        }


        return "home";
    }

    @PostMapping("upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {

        ficheroService.addFile(file,session);

        return "redirect:/home";
    }

    @GetMapping("/fichero/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable(value="id") Long fileId, HttpSession session) {

        Fichero fichero = ficheroService.findByIdDescifrado(fileId,session);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fichero.getNombre() + "\"")
                .body(fichero.getDatos());
    }
}
