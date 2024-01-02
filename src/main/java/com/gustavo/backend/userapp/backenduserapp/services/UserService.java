package com.gustavo.backend.userapp.backenduserapp.services;


import com.gustavo.backend.userapp.backenduserapp.model.Dto.UserDto;
import com.gustavo.backend.userapp.backenduserapp.model.Dto.mapper.DtoMapper;
import com.gustavo.backend.userapp.backenduserapp.model.Role;
import com.gustavo.backend.userapp.backenduserapp.model.User;
import com.gustavo.backend.userapp.backenduserapp.model.UserRequest;
import com.gustavo.backend.userapp.backenduserapp.repository.RolRepository;
import com.gustavo.backend.userapp.backenduserapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
@Transactional
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;




    @Transactional(readOnly = true)
    public List<UserDto> listar(){

            List<User> users=(List<User>) this.repository.findAll();
        return users.stream()
                .map(u-> DtoMapper.getInstance()
                        .setUser(u).build())
                .collect(Collectors.toList());
    }



    public UserDto save(User user){

        String passwordBc= passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordBc);
        Optional<Role> o = rolRepository.findByName("ROLE_USER");
        List<Role>roles= new ArrayList<>();
        if (o.isPresent()){
            roles.add(o.orElseThrow());
        }
        user.setRoles(roles);

        return DtoMapper.getInstance().setUser(this.repository.save(user)).build();

    }

    public Optional<UserDto> findById(Long id){

            Optional<User> o =this.repository.findById(id);

            if (o.isPresent()){
                return  Optional.of(DtoMapper.getInstance().setUser(o.orElseThrow()).build());
            }

            return  Optional.empty();
    }

    public void delete (Long id){

        this.repository.deleteById(id);
    }

    public  Optional<UserDto> update(UserRequest user, Long id) {

        Optional<User> o = this.repository.findById(id);
        User userOptional = null;
        if (o.isPresent()) {

            User useDB = o.orElseThrow();

            useDB.setUsername(user.getUsername());
            useDB.setEmail(user.getEmail());

           userOptional=repository.save(useDB);

        }

        return Optional.ofNullable(DtoMapper.getInstance().setUser(userOptional).build());

    }

}
