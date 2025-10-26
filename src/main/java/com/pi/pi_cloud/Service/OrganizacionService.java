package com.pi.pi_cloud.Service;

import com.pi.pi_cloud.Model.Organizacion;
import com.pi.pi_cloud.Model.Usuario;
import com.pi.pi_cloud.repository.OrganizacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizacionService {

    @Autowired
    private OrganizacionRepository organizacionRepository;


    @Transactional
    public Iterable<Organizacion> getAllOrgs() {
        return organizacionRepository.findAll();
    }
}
