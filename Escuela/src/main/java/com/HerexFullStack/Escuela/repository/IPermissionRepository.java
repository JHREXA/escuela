package com.HerexFullStack.Escuela.repository;

import com.HerexFullStack.Escuela.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {
}
