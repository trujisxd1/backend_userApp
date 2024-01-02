package com.gustavo.backend.userapp.backenduserapp.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "no puede estar vacio")
    @Column(unique = true)
    private String  username;

    @NotEmpty(message = "no puede esatar vacio")
    private String password;
    @Column(unique = true)

    @Email(message = "el correo no es valido")
    @NotEmpty(message = "no puede estar vacio")
    private String email;


    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id")
            ,inverseJoinColumns = @JoinColumn(name = "role_id")
            ,uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","role_id"})})
     private  List<Role> roles;

}
