package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public User findByUsername(String username);

    public void createNewUser(User user);

    public List<User> allUsers();

    public void deleteUser(Long id);

    public User getUser(Long id);

    public void update(Long id, User user);

    public List<Role> getAllRoles();
}
