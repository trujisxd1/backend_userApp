package com.gustavo.backend.userapp.backenduserapp.repository;

import com.gustavo.backend.userapp.backenduserapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {

}
