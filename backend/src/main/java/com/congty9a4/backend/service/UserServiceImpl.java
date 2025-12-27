package com.congty9a4.backend.service;

import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Userchan> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Userchan getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Userchan createUser(Userchan user) {
        return userRepository.save(user);
    }

    @Override
    public Userchan updateUser(UUID id, Userchan user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}

