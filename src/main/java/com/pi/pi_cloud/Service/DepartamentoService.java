package com.pi.pi_cloud.Service;

import com.pi.pi_cloud.Model.Departamento;
import com.pi.pi_cloud.Model.Departamento;
import com.pi.pi_cloud.Model.Departamento;
import com.pi.pi_cloud.repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;


    @Transactional
    public Iterable<Departamento> getAllDepartments() {
        return departamentoRepository.findAll();
    }

    public void addDepartamento(Departamento departamento) {
        departamentoRepository.save(departamento);
    }

    @Transactional
    public void guardar(Departamento departamento) {
        departamentoRepository.save(departamento);
    }

    public void eliminarDepartamento(Departamento departamento) {
        departamentoRepository.delete(departamento);

    }

    public void eliminarDepartamentoPorId(Long id) {
        departamentoRepository.deleteById(id);
    }
}
