package com.pi.pi_cloud.Service;

import com.pi.pi_cloud.Model.Departamento;
import com.pi.pi_cloud.Model.Fichero;
import com.pi.pi_cloud.Model.Usuario;
import com.pi.pi_cloud.dto.FicheroData;
import com.pi.pi_cloud.dto.UserData;
import com.pi.pi_cloud.dto.LoginRequestDTO;
import com.pi.pi_cloud.dto.RegisterRequestDTO;
import com.pi.pi_cloud.lib.AesResult;
import com.pi.pi_cloud.lib.Cifrado;
import com.pi.pi_cloud.lib.LoginStatus;
import com.pi.pi_cloud.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pi.pi_cloud.Security.PBKDF2Util;
import com.pi.pi_cloud.Security.RSAUtil;
import com.pi.pi_cloud.Security.TOTPUtil;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.util.Optional;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
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

    public void eliminarUsuario(Usuario usuario) {
        userRepository.delete(usuario);
    }

    /**
     * Registra un nuevo usuario con contraseña protegida (PBKDF2),
     * secreto TOTP para MFA, y par de claves RSA.
     */
    @Transactional
    public Usuario registerUser(RegisterRequestDTO dto) throws Exception {
        String salt = PBKDF2Util.generateSalt();
        String hashedPassword = PBKDF2Util.hashPassword(dto.getPassword(), salt);


        // Generar par de claves RSA
        KeyPair keyPair = RSAUtil.generateKeyPair();
        String publicKey = RSAUtil.encodeKey(keyPair.getPublic());
        String privateKey = RSAUtil.encodeKey(keyPair.getPrivate());


        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setSalt(salt);
        usuario.setPassword(hashedPassword);
        usuario.setPublicKey(publicKey);
        usuario.setAdmin(dto.isAdmin());
        usuario.setDepartamento(dto.getDepartamentoId());
        usuario.setFirstLogin(false);

        // Cifrado de la clave privada
        SecretKey contrasenaSK = Cifrado.derivarClave(dto.getPassword().toCharArray(),salt.getBytes(StandardCharsets.UTF_8));
        AesResult cpCifrada = Cifrado.cifrarArchivoConAes(Base64.getDecoder().decode(privateKey),contrasenaSK);
        byte[] blob = cpCifrada.toBlob();
        usuario.setEncryptedPrivateKey(Base64.getEncoder().encodeToString(blob));

        if (dto.isRequiresTOTP()){
            String totpSecret = TOTPUtil.generateSecret();
            usuario.setTotpSecret(totpSecret);
            usuario.setFirstLogin(true);
        }

        return userRepository.save(usuario);
    }

    /**
     * Verifica credenciales de login (email, password, TOTP)
     */
    @Transactional
    public LoginStatus loginUser(LoginRequestDTO dto) throws Exception {
        Optional<Usuario> opt = userRepository.findByEmail(dto.getEmail());
        if (opt.isEmpty()) return LoginStatus.INVALID_LOGIN;

        Usuario usuario = opt.get();

        if (usuario.isFirstLogin()) {
            return LoginStatus.FIRST_LOGIN;
        }

        boolean validPassword = PBKDF2Util.validatePassword(dto.getPassword(), usuario.getPassword(), usuario.getSalt());
        if (!validPassword) return LoginStatus.INVALID_LOGIN;

        // Si el usuario no tiene doble factor puede iniciar sesión (Cuentas administradoras)
        if (usuario.getTotpSecret() == null) {
            return LoginStatus.SUCCESS;
        }

        boolean totpVerified = TOTPUtil.verifyCode(usuario.getTotpSecret(), dto.getTotpCode());

        if (totpVerified) {
            return LoginStatus.SUCCESS;
        }

        return LoginStatus.INVALID_LOGIN;
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

    @Transactional
    public void guardarClavePrivadaEnSession(String password, HttpSession session) {

        String sessionEmail = (String) session.getAttribute("email");
        if (sessionEmail == null) {
            return;
        }

        Usuario usuario = userRepository.findByEmail(sessionEmail).orElse(null);
        if (usuario == null) {
            return;
        }

        try{
            SecretKey contrasenaSK = Cifrado.derivarClave(password.toCharArray(),usuario.getSalt().getBytes(StandardCharsets.UTF_8));
            byte[] blob = Base64.getDecoder().decode(usuario.getEncryptedPrivateKey());
            byte[] privateKeyByte = Cifrado.descifrarArchivoConAes(blob,contrasenaSK);
            String pkBase64 = Base64.getEncoder().encodeToString(privateKeyByte);

            session.setAttribute("privateKey", pkBase64);

        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Transactional
    public void finFirstLogin(String email) {
        Usuario usuario = userRepository.findByEmail(email).orElse(null);

        if (usuario != null) {
            usuario.setFirstLogin(false);
            userRepository.save(usuario);
        }
    }
}
