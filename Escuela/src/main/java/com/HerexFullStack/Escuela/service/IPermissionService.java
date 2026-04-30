package com.HerexFullStack.Escuela.service;

import com.HerexFullStack.Escuela.model.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {

    List findAll();
    Optional<Permission> findById(Long id);
    Permission savePermission(Permission permission);
    void deleteById(Long id);
    Permission update(Permission permission);
}
