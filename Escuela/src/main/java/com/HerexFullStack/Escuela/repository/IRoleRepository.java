package com.HerexFullStack.Escuela.repository;

import com.HerexFullStack.Escuela.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
}
