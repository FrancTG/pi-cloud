package com.pi.pi_cloud.Service;

import com.pi.pi_cloud.Model.Usuario;
import com.pi.pi_cloud.dto.UserData;
import com.pi.pi_cloud.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Iterable<Usuario> getAllUsers() {
        return userRepository.findAll();
    }
}
