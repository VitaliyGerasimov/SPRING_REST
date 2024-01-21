package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService,UserDetailsService {
    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElse(null);
    }

    @Override
    @Transactional
    public void createNewUser(User user) {
        Role userRole = roleRepository.findById(1L).orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }


    @Override
    @Transactional
    public void deleteUser(Long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    @Transactional
    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void update(Long id, User user) {
        User existingUser = entityManager.find(User.class, id);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setRoles(user.getRoles());
            existingUser.setName(user.getName());
            existingUser.setLastName(user.getLastName());
            entityManager.merge(existingUser);
        }
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}