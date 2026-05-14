package com.HerexFullStack.Escuela.service;

import com.HerexFullStack.Escuela.model.UserSec;
import com.HerexFullStack.Escuela.repository.IUserSecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom implementation of Spring Security UserDetailsService.
 *
 * This service is responsible for loading the user authetication data
 * from the database and transforming roles and permissions into Spring
 * Security Granted Authority objects.
 *
 *The loaded UserDetails object is later used by Spring Security during the
 * authentication and authorisation processes.
 *
 * Responsibilities:
 *  * - Load users by username
 *  * - Validate user existence
 *  * - Convert roles into ROLE_ authorities
 *  * - Convert permissions into GrantedAuthority objects
 *  * - Build Spring Security UserDetails instances
 *  *
 *  * @author Jesus Hernandez Rexa
 *  * @since 1.0
 * */
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserSecRepository userSecRepository;


    /**
     * Loads a user from the database using the username.
     *
     * This method is automatically used by Spring Security during
     * the authentication process.
     *
     * The method retrieves:
     * - user credentials
     * - roles
     * - permissions
     * - account status flags
     *
     * Roles are transformed into authorities using the ROLE_ prefix.
     * Permissions are also converted into GrantedAuthority objects.
     *
     * Example:
     * ROLE_ADMIN
     * READ_PRODUCTS
     * CREATE_USERS
     *
     * @param username username used during authentication
     * @return fully configured UserDetails object
     * @throws UsernameNotFoundException if the username does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserSec userSec = userSecRepository.findUserEntityByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("The user " + username + " was not found."));

        // Convert roles into Spring Security authorities
        List<GrantedAuthority> authorityList = new ArrayList<>();
        userSec.getRoleList()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

        // Convert permissions into authorities
        userSec.getRoleList().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));

        return new User(userSec.getUsername(),
                userSec.getPassword(),
                userSec.isEnabled(),
                userSec.isAccountNotExpired(),
                userSec.isCredentialNotExpired(),
                userSec.isAccountNotLocked(),
                authorityList);
    }
}
