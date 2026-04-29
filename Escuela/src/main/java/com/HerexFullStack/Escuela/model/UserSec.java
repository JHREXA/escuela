package com.HerexFullStack.Escuela.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UserSec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private boolean enabled;
    private boolean accountNotExpired;
    private boolean accountNotLocked;
    private boolean credentialNotExpired;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name= "user_roles", joinColumns = @JoinColumn(name ="user_id"), inverseJoinColumns = @JoinColumn(name = ))
    private Set<Role> roleList = new HashSet<>();
}
