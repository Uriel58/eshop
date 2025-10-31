package com.example.demo.dao;

import com.example.demo.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    
    List<User> findAll();
    
    Optional<User> findById(Long id);//User findById(Long id);改用Optional<User>
    
    void save(User user);//改用User
    
    void delete(Long id);
    
    User findByEmail(String email);

    
}
