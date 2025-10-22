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
    public void addFile(MultipartFile file) throws IOException {

        Usuario user = userRepository.findById(0L).orElse(null);

        try {
            SecretKey sk = Cifrado.generarClaveAes();
            AesResult res = Cifrado.cifrarArchivoConAes(file.getBytes(),sk);

            Fichero fichero = new Fichero();
            fichero.setNombre(file.getOriginalFilename()); //Falta cifrarlo?
            fichero.setDatos(res.toBlob());
            fichero.setClaveCifrada(sk);
            fichero.setUsuario(user);
            ficheroRepository.save(fichero);

        } catch (GeneralSecurityException ex) {

        }

    }

    @Transactional
    public List<FicheroData> getFicherosFromUsuario(Usuario user) {
        List<FicheroData> ficheros = user.getFicheros().stream().map(fichero -> modelMapper.map(fichero,FicheroData.class)).collect(Collectors.toList());
        return ficheros;
    }

    @Transactional
    public Optional<Fichero> findById(Long id) {
        return ficheroRepository.findById(id);
    }

}
