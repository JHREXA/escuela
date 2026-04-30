package com.HerexFullStack.Escuela.service;

import com.HerexFullStack.Escuela.model.UserSec;

import java.util.List;
import java.util.Optional;

public interface IUserSecService {

    List findAll();
    Optional<UserSec> findById(Long id);
    UserSec  save(UserSec userSec);
    void deleteById(Long id);
    UserSec update(UserSec userSec);
}
