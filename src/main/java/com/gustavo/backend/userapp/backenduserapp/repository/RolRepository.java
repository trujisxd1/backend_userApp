package com.gustavo.backend.userapp.backenduserapp.repository;

import com.gustavo.backend.userapp.backenduserapp.model.Role;
import com.gustavo.backend.userapp.backenduserapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);



}
