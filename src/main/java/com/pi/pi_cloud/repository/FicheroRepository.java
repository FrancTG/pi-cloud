package com.pi.pi_cloud.repository;

import com.pi.pi_cloud.Model.Fichero;
import com.pi.pi_cloud.Model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FicheroRepository extends CrudRepository<Fichero, Long> {

}
