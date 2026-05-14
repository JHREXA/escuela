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


@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserSecRepository userSecRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserSec userSec = userSecRepository.findUserEntityByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("The user " + username + " was not found."));

        List<GrantedAuthority> authorityList = new ArrayList<>();
        userSec.getRoleList()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

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
