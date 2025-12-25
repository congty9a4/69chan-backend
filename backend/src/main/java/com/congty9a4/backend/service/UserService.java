package com.congty9a4.backend.service;

import com.congty9a4.backend.entity.Userchan;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<Userchan> getAllUsers();
    Userchan getUserById(UUID id);
    Userchan createUser(Userchan user);
    Userchan updateUser(UUID id, Userchan user);
    void deleteUser(UUID id);
}

