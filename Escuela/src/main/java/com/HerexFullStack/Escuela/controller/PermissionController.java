package com.HerexFullStack.Escuela.controller;

import com.HerexFullStack.Escuela.model.Permission;
import com.HerexFullStack.Escuela.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Repository("/api/permissions")
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @GetMapping("/getAllPermissions")
    public ResponseEntity<List> getAllPermissions(){
        List<Permission> permissions = permissionService.findAll();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPermissionById(@PathVariable Long id){
        Optional<Permission> permission = permissionService.findById(id);
        return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/createPermission")
    public ResponseEntity createPermission(Permission permission){
        Permission newPermission = permissionService.savePermission(permission);
        return ResponseEntity.ok(newPermission);
    }
}
