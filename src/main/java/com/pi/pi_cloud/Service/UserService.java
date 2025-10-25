package com.pi.pi_cloud.Service;

import com.pi.pi_cloud.Model.Fichero;
import com.pi.pi_cloud.Model.Usuario;
import com.pi.pi_cloud.dto.FicheroData;
import com.pi.pi_cloud.dto.UserData;
import com.pi.pi_cloud.dto.LoginRequestDTO;
import com.pi.pi_cloud.dto.RegisterRequestDTO;
import com.pi.pi_cloud.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pi.pi_cloud.Security.PBKDF2Util;
import com.pi.pi_cloud.Security.RSAUtil;
import com.pi.pi_cloud.Security.TOTPUtil;
import java.io.ByteArrayOutputStream;
import java.util.Optional;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.Base64;

import java.util.List;
import java.util.stream.Collectors;
import java.security.KeyPair;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Función de ejemplo para probar el servidor de BBDD
    @Transactional
    public void addUser(UserData userData) {
        Usuario user = modelMapper.map(userData, Usuario.class);
        userRepository.save(user);
    }

    @Transactional
    public Iterable<Usuario> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public List<FicheroData> getFicherosFromUsuario (Long userId) {
        Usuario usuario = userRepository.findById(userId).orElse(null);

        return usuario.getFicheros().stream().map(fichero -> modelMapper.map(fichero, FicheroData.class)).collect(Collectors.toList());
    }


    /**
     * Registra un nuevo usuario con contraseña protegida (PBKDF2),
     * secreto TOTP para MFA, y par de claves RSA.
     */
    @Transactional
    public Usuario registerUser(RegisterRequestDTO dto) throws Exception {
        String salt = PBKDF2Util.generateSalt();
        String hashedPassword = PBKDF2Util.hashPassword(dto.getPassword(), salt);
        String totpSecret = TOTPUtil.generateSecret();

        // Generar par de claves RSA
        KeyPair keyPair = RSAUtil.generateKeyPair();
        String publicKey = RSAUtil.encodeKey(keyPair.getPublic());
        String privateKey = RSAUtil.encodeKey(keyPair.getPrivate());

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSalt(salt);
        usuario.setPasswordHash(hashedPassword);
        usuario.setTotpSecret(totpSecret);
        usuario.setPublicKey(publicKey);
        usuario.setEncryptedPrivateKey(privateKey);
        usuario.setAdmin(dto.isAdmin());

        return userRepository.save(usuario);
    }

    /**
     * Verifica credenciales de login (email, password, TOTP)
     */
    @Transactional
    public boolean loginUser(LoginRequestDTO dto) throws Exception {
        Optional<Usuario> opt = userRepository.findByEmail(dto.getEmail());
        if (opt.isEmpty()) return false;

        Usuario usuario = opt.get();

        boolean validPassword = PBKDF2Util.validatePassword(dto.getPassword(), usuario.getPasswordHash(), usuario.getSalt());
        if (!validPassword) return false;

        return TOTPUtil.verifyCode(usuario.getTotpSecret(), dto.getTotpCode());
    }

    /**
     * Obtiene el secreto TOTP de un usuario (para mostrar QR o configurar Google Authenticator)
     */
    @Transactional
    public String getUserMfaSecret(String email) {
        Optional<Usuario> opt = userRepository.findByEmail(email);
        return opt.map(Usuario::getTotpSecret).orElse(null);
    }

    @Transactional
    public String generateMfaQr(String email) throws Exception {
        Optional<Usuario> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        Usuario usuario = opt.get();
        String secret = usuario.getTotpSecret();
        if (secret == null || secret.isEmpty()) {
            throw new IllegalStateException("El usuario no tiene configurado MFA");
        }

        // Nombre de la aplicación que aparecerá en Google Authenticator
        String issuer = "PI-Cloud";
        String otpauthUrl = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                issuer, usuario.getEmail(), secret, issuer
        );

        // Generar imagen QR
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpauthUrl, BarcodeFormat.QR_CODE, 250, 250);
        ByteArrayOutputStream pngOutput = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutput);

        return Base64.getEncoder().encodeToString(pngOutput.toByteArray());
    }
}
