package com.HerexFullStack.Escuela.service;

import com.HerexFullStack.Escuela.model.UserSec;
import com.HerexFullStack.Escuela.repository.IUserSecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSecService implements IUserSecService{

    @Autowired
    private IUserSecRepository userSecRepository;

    @Override
    public List findAll() {
        return userSecRepository.findAll();
    }

    @Override
    public Optional findById(Long id) {
        return userSecRepository.findById(id);
    }

    @Override
    public UserSec save(UserSec userSec) {
        return userSecRepository.save(userSec);
    }

    @Override
    public void deleteById(Long id) {
        userSecRepository.deleteById(id);
    }

    @Override
    public UserSec update(UserSec userSec) {
        return userSecRepository.save(userSec);
    }
}
