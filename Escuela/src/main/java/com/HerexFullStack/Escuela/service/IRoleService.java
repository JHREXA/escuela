package com.HerexFullStack.Escuela.service;

import com.HerexFullStack.Escuela.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    List findAll();
    Optional<Role> findById(Long id);
    Role saveRole(Role role);
    void deleteById(Long id);
    Role update(Role role);

}
