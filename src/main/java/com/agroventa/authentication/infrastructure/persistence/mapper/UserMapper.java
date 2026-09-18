package com.agroventa.authentication.infrastructure.persistence.mapper;

import com.agroventa.authentication.application.dto.RegistryCommand;
import com.agroventa.authentication.infrastructure.persistence.entity.UserEntity;

public class UserMapper {

    public static UserEntity requestToEntityUser(RegistryCommand register){
        UserEntity userEntity = new UserEntity();
        userEntity.setName(register.name());
        userEntity.setLastName(register.lastName());
        userEntity.setPhone(register.phone());
        userEntity.setEmail(register.email());
        userEntity.setPasswordHash(register.password());
        userEntity.setStatus(Boolean.TRUE);
        return userEntity;
    }

}
