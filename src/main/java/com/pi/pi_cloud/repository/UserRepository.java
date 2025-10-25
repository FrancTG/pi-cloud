package com.pi.pi_cloud.repository;

import com.pi.pi_cloud.Model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
