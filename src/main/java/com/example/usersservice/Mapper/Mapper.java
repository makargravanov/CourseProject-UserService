package com.example.usersservice.Mapper;

import com.example.usersservice.Data.UserDataOut;
import com.example.usersservice.Data.UserRegisterData;
import com.example.usersservice.Entity.MusicalTrackKeysEntity;
import com.example.usersservice.Entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public interface Mapper {

    public default UserEntity registerDataToUserEntity(UserRegisterData userRegisterData){
        Set<MusicalTrackKeysEntity> tracks = new HashSet<>();
        return new UserEntity( null,userRegisterData.getName(), userRegisterData.getLogin(), userRegisterData.getPassword(), userRegisterData.getEmail(), "USER", LocalDateTime.now(), tracks);
    }
    public default UserDataOut userEntityToUserDataOut(UserEntity userEntity){
        return new UserDataOut(userEntity.getId(), userEntity.getName(), userEntity.getLogin(), userEntity.getEmail());
    }

}
