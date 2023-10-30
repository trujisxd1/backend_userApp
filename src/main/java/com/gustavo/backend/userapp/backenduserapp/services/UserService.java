package com.gustavo.backend.userapp.backenduserapp.services;


import com.gustavo.backend.userapp.backenduserapp.model.User;
import com.gustavo.backend.userapp.backenduserapp.model.UserRequest;
import com.gustavo.backend.userapp.backenduserapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@Transactional
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;




    @Transactional(readOnly = true)
    public List<User> listar(){

        return  this.repository.findAll();
    }



    public User save(User user){

        String passwordBc= passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordBc);
        return this.repository.save(user);

    }

    public Optional<User> findById(Long id){

        return this.repository.findById(id);
    }

    public void delete (Long id){

        this.repository.deleteById(id);
    }

    public  Optional<User> update(UserRequest user, Long id){

        Optional<User>o=this.findById(id);
        if (o.isPresent()){
            User useDB = o.orElseThrow();

            useDB.setUsername(user.getUsername());
            useDB.setEmail(user.getEmail());

    return Optional.ofNullable( this.save(useDB));

        }

        return  Optional.ofNullable(null);

    }

}
