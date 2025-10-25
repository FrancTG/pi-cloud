package com.pi.pi_cloud.Controller;

import com.pi.pi_cloud.Model.Usuario;
import com.pi.pi_cloud.Service.UserService;
import com.pi.pi_cloud.dto.LoginRequestDTO;
import com.pi.pi_cloud.dto.RegisterRequestDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    // Registro de usuario (REST)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO dto) {
        try {
            Usuario usuario = userService.registerUser(dto);
            return ResponseEntity.ok("Usuario registrado. Guarda este secreto MFA: " + usuario.getTotpSecret());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar usuario: " + e.getMessage());
        }
    }

    // Login (REST)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto, HttpSession session) {
        try {
            boolean success = userService.loginUser(dto);

            if (success) {
                // Guardar email del usuario autenticado en sesión
                session.setAttribute("email", dto.getEmail());
                return ResponseEntity.ok("Login exitoso ");
            } else {
                return ResponseEntity.status(401).body("Credenciales o código TOTP incorrectos ");
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // Logout (REST)
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Sesión cerrada correctamente ");
    }

    // Generar QR para MFA (REST)
    @GetMapping("/qr/{email}")
    public ResponseEntity<?> getMfaQr(@PathVariable String email) {
        try {
            String qrBase64 = userService.generateMfaQr(email);
            String html = "<html><body><h3>Escanea este QR en Google Authenticator</h3>"
                    + "<img src='data:image/png;base64," + qrBase64 + "'/>"
                    + "<p>O usa el secreto manual: <b>" + userService.getUserMfaSecret(email) + "</b></p>"
                    + "</body></html>";
            return ResponseEntity.ok().header("Content-Type", "text/html").body(html);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generando QR: " + e.getMessage());
        }
    }
}
