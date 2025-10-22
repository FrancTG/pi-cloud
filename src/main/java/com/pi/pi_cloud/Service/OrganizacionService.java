package com.pi.pi_cloud.Service;

import com.pi.pi_cloud.repository.OrganizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizacionService {

    @Autowired
    private OrganizacionRepository organizacionRepository;


}
