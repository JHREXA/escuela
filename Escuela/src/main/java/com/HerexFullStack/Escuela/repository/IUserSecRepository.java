package com.HerexFullStack.Escuela.repository;

import com.HerexFullStack.Escuela.model.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserSecRepository extends JpaRepository<UserSec, Long> {

    Optional findUserEntityByUsername(String username);
}
