package com.HerexFullStack.Escuela.controller;

import com.HerexFullStack.Escuela.model.Role;
import com.HerexFullStack.Escuela.model.UserSec;
import com.HerexFullStack.Escuela.repository.IRoleRepository;
import com.HerexFullStack.Escuela.repository.IUserSecRepository;
import com.HerexFullStack.Escuela.service.IRoleService;
import com.HerexFullStack.Escuela.service.IUserSecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserSecController {

    @Autowired
    private IUserSecService userSecService;

    @Autowired
    private IRoleService roleService;

    @GetMapping("/getAllUsers")
    public ResponseEntity getAllUsers(){
        List users = userSecService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable Long id){
        Optional<UserSec> user = userSecService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserSec userSec) {

        Set<Role> roles = new HashSet<>();

        if (userSec.getRoleList() == null || userSec.getRoleList().isEmpty()) {
            return ResponseEntity.badRequest().body("Debe enviar al menos un rol");
        }

        userSec.setPassword(userSecService.encriptPassword(userSec.getPassword()));

        for (Role role : userSec.getRoleList()) {

            System.out.println("ROLE ID RECIBIDO: " + role.getId());

            if (role.getId() == null) {
                return ResponseEntity
                        .badRequest()
                        .body("El id del rol no puede ser null");
            }

            Role readRole = roleService.findById(role.getId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + role.getId()));

            roles.add(readRole);
        }

        userSec.setRoleList(roles);

        UserSec newUserSec = userSecService.save(userSec);

        return ResponseEntity.ok(newUserSec);
    }
}
