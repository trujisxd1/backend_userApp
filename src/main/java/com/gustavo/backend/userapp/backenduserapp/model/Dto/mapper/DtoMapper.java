package com.gustavo.backend.userapp.backenduserapp.model.Dto.mapper;


import com.gustavo.backend.userapp.backenduserapp.model.Dto.UserDto;
import com.gustavo.backend.userapp.backenduserapp.model.User;

public class DtoMapper {


 private User user;



    private DtoMapper() {

    }

    public static DtoMapper getInstance(){
        return new DtoMapper();

    }
    public DtoMapper setUser(User user) {
        this.user = user;
        return this;
    }

    public UserDto build(){
    if(user==null){
       throw  new RuntimeException("debe pasar el entity user");
    }
    UserDto userDto= new UserDto(this.user.getId(),user.getUsername(),user.getEmail());

    return userDto;
    }
}
