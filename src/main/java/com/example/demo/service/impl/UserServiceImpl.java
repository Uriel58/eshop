package com.example.demo.service.impl;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDAO userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
    	logger.info("編輯資料，ID={}", id);
        return userRepository.findById(id);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, User updatedUser) {
    	User existingUser = userRepository.findById(id);
    	if (existingUser == null) {
    	    throw new RuntimeException("User not found");
    	}


        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        // ✅ 如果使用者有輸入密碼，才更新密碼（避免覆蓋成 null 或空字串）
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(updatedUser.getPassword());
        }

        userRepository.save(existingUser);
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
    
    @Override
    public User findByEmail(String email) {
    	return userRepository.findByEmail(email);
    }

}
