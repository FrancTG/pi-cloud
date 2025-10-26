package com.pi.pi_cloud.run;

import java.util.HashSet;
import java.util.Set;

import com.pi.pi_cloud.Model.Organizacion;
import com.pi.pi_cloud.Model.Usuario;
import com.pi.pi_cloud.Model.Departamento;
import com.pi.pi_cloud.dto.UserData;
import com.pi.pi_cloud.repository.DepartamentoRepository;
import com.pi.pi_cloud.repository.OrganizacionRepository;
import com.pi.pi_cloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import jakarta.transaction.Transactional;

@Component
public class OnApplicationRun implements CommandLineRunner{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private OrganizacionRepository organizacionRepository;




    @Override
    @Transactional
    public void run(String... args) throws Exception {

        generateUserAdminDefault(userRepository, organizacionRepository, departamentoRepository);

    }

    private void generateUserAdminDefault(UserRepository userRepository, OrganizacionRepository organizacionRepository, DepartamentoRepository departamentoRepository) {

        if( userRepository.count() == 0 ) {

            //Generamos roles por defecto: role de admin y rol de user

            /*Organizacion org = new Organizacion();
            org.setDepartamentos();
            org.setNombre("YUKA");



            Departamento dep = new Departamento();
            dep.setNombre("HHRR");
            dep.setOrganizacion(org);
            dep.setUsuarios(null);
            dep.setOrganizacion(org);*/

            Usuario user;


            Organizacion organizacion = new Organizacion("example_org_1");
            organizacion = organizacionRepository.save(organizacion);
            Departamento departamento = new Departamento("example_dep_1", organizacion);
            departamento = departamentoRepository.save(departamento);
            user = new Usuario("example@example.com","12345",false,departamento, "12345");
            user = userRepository.save(user);

            Organizacion organizacion2 = new Organizacion("example_org_2");
            organizacion2 = organizacionRepository.save(organizacion2);
            Departamento departamento2 = new Departamento("example_dep_2", organizacion);
            departamento2 = departamentoRepository.save(departamento2);
            user = new Usuario("example2@example.com","12345",true,departamento2, "12345");
            user = userRepository.save(user);

            Organizacion organizacion3 = new Organizacion("example_org_3");
            organizacion3 = organizacionRepository.save(organizacion3);
            Departamento departamento3 = new Departamento("example_dep_3", organizacion3);
            departamento3 = departamentoRepository.save(departamento3);
            user = new Usuario("example3@example.com","12345",true,departamento3, "12345");
            user = userRepository.save(user);


        }else{
            return;
        }
    }
}


