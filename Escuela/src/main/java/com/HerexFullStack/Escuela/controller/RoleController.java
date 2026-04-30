package com.HerexFullStack.Escuela.controller;

import com.HerexFullStack.Escuela.model.Permission;
import com.HerexFullStack.Escuela.model.Role;
import com.HerexFullStack.Escuela.service.IPermissionService;
import com.HerexFullStack.Escuela.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    @GetMapping("/getAllRoles")
    public ResponseEntity getAllRoles(){
        List roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity getRoleById(@PathVariable Long id){
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity createRole(@RequestBody Role role){
        Set<Permission> permissionList = new HashSet<>();
        Permission readPermission;

        for(Permission per : role.getPermissionList()){
            readPermission = permissionService.findById(per.getId()).orElse(null);
            if(readPermission != null){
                permissionList.add(readPermission);
            }
        }
        role.setPermissionList(permissionList);
        Role newRole = roleService.saveRole(role);
        return ResponseEntity.ok(newRole);
    }
}
