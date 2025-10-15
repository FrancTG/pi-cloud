package com.pi.pi_cloud.repository;

import com.pi.pi_cloud.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
