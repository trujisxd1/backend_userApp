package com.gustavo.backend.userapp.backenduserapp.repository;

import com.gustavo.backend.userapp.backenduserapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    @Query("select u from User u where  u.username=?1")
    Optional<User> getUserByUsername(String username);


}
