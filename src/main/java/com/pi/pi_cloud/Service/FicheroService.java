package com.pi.pi_cloud.Service;

import com.pi.pi_cloud.Model.Fichero;
import com.pi.pi_cloud.Model.Usuario;
import com.pi.pi_cloud.dto.FicheroData;
import com.pi.pi_cloud.lib.AesResult;
import com.pi.pi_cloud.lib.Cifrado;
import com.pi.pi_cloud.repository.FicheroRepository;
import com.pi.pi_cloud.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
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
    public void addFile(MultipartFile file, Usuario usuario) throws IOException {

        // La clave sk se debe cifrar con una clave del servidor

        try {
            SecretKey sk = Cifrado.generarClaveAes();
            AesResult res = Cifrado.cifrarArchivoConAes(file.getBytes(),sk);

            Fichero fichero = new Fichero();
            fichero.setNombre(file.getOriginalFilename()); //Falta cifrarlo?
            fichero.setDatos(res.toBlob());
            fichero.setClaveCifrada(sk);
            fichero.setUsuario(usuario);
            ficheroRepository.save(fichero);

        } catch (GeneralSecurityException ex) {

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

    public Fichero findByIdDescifrado(Long id) {

        // Fichero.getClaveCifrada() se debe decifrar cuando se cifre con la clave del servidor

        Fichero fichero = null;
        try {
            fichero = ficheroRepository.findById(id).orElse(null);
            fichero.setDatos(Cifrado.decryptAesGcm(fichero.getDatos(), fichero.getClaveCifrada()));

        } catch (GeneralSecurityException ex) {

        }
        return fichero;
    }

}
