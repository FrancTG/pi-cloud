package com.pi.pi_cloud.Service;

import com.pi.pi_cloud.Model.Departamento;
import com.pi.pi_cloud.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;


    public void addDepartamento(Departamento departamento) {
        departamentoRepository.save(departamento);
    }
}
