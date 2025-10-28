package com.pi.pi_cloud.Service;

import com.pi.pi_cloud.Model.Fichero;
import com.pi.pi_cloud.Model.Usuario;
import com.pi.pi_cloud.Security.RSAUtil;
import com.pi.pi_cloud.dto.FicheroData;
import com.pi.pi_cloud.lib.AesResult;
import com.pi.pi_cloud.lib.Cifrado;
import com.pi.pi_cloud.repository.FicheroRepository;
import com.pi.pi_cloud.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FicheroService {

    @Autowired
    private FicheroRepository ficheroRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public void addFile(MultipartFile file, HttpSession session) throws IOException {

        String sessionEmail = (String) session.getAttribute("email");
        if (sessionEmail == null) {
            return;
        }

        Usuario usuario = userRepository.findByEmail(sessionEmail).orElse(null);
        if (usuario == null){
            return;
        }

        try {
            SecretKey claveDatos = Cifrado.generarClaveAes();
            AesResult datosCifrados = Cifrado.cifrarArchivoConAes(file.getBytes(),claveDatos);

            PrivateKey pkey = RSAUtil.decodePrivateKey(usuario.getEncryptedPrivateKey());
            byte[] claveDatosCifradaByte = Cifrado.cifrarClaveConRSA(claveDatos.getEncoded(), pkey);

            SecretKey claveDatosCifrada = new SecretKeySpec(claveDatosCifradaByte,"AES");

            Fichero fichero = new Fichero();
            fichero.setNombre(file.getOriginalFilename());
            fichero.setDatos(datosCifrados.toBlob());
            fichero.setClaveCifrada(claveDatosCifrada);
            fichero.setUsuario(usuario);
            ficheroRepository.save(fichero);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Transactional
    public List<FicheroData> getFicherosFromUsuario(Usuario user) {
        return ficheroRepository.findByUsuario(user).stream().map(fichero -> modelMapper.map(fichero, FicheroData.class)).collect(Collectors.toList());
    }

    @Transactional
    public Optional<Fichero> findById(Long id) {
        return ficheroRepository.findById(id);
    }

    public Fichero findByIdDescifrado(Long id, HttpSession session) {

        String sessionEmail = (String) session.getAttribute("email");
        if (sessionEmail == null) {
            return null;
        }

        Usuario usuario = userRepository.findByEmail(sessionEmail).orElse(null);

        try {
            PublicKey pkey = RSAUtil.decodePublicKey(usuario.getPublicKey());

            Fichero fichero = ficheroRepository.findById(id).orElse(null);
            byte[] claveDatosDescifradaByte = Cifrado.descifrarClaveConRSA(fichero.getClaveCifrada().getEncoded(), pkey);
            SecretKey claveDatosDescifrada = new SecretKeySpec(claveDatosDescifradaByte,"AES");

            fichero.setDatos(Cifrado.descifrarArchivoConAes(fichero.getDatos(), claveDatosDescifrada));
            return fichero;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

}
