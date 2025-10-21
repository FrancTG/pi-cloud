package com.pi.pi_cloud.Controller;

import com.pi.pi_cloud.Model.Departamento;
import com.pi.pi_cloud.Model.Fichero;
import com.pi.pi_cloud.Service.DepartamentoService;
import com.pi.pi_cloud.Service.FicheroService;
import com.pi.pi_cloud.dto.FicheroData;
import jakarta.annotation.Resource;
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
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    FicheroService ficheroService;

    @GetMapping("home")
    public String home(Model model) {
        List<FicheroData> ficheros = ficheroService.getFicherosFromDepartamento();

        if (ficheros != null) {
            model.addAttribute("ficheros",ficheros);
        }


        return "home";
    }

    @PostMapping("upload")
    public String uploadFile(@RequestParam("file")MultipartFile file, Model model) throws IOException {

        ficheroService.addFile(file);

        return "redirect:/home";
    }

    @GetMapping("/fichero/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable(value="id") Long fileId) {

        Fichero fichero = ficheroService.findById(fileId).orElse(null);


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fichero.getNombre() + "\"")
                .body(fichero.getDatos());
    }
}
