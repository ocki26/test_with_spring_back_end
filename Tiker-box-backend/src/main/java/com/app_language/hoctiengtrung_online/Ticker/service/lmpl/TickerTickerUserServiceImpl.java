package com.app_language.hoctiengtrung_online.Ticker.service.lmpl;


import com.app_language.hoctiengtrung_online.Ticker.dto.TickerUserLoginRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.TickerUserRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.User;
import com.app_language.hoctiengtrung_online.Ticker.repository.TickerUserRepository;
import com.app_language.hoctiengtrung_online.Ticker.service.TickerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TickerTickerUserServiceImpl implements TickerUserService {

    @Autowired
    private TickerUserRepository tickerUserRepository;

    @Override
    public User createUser(TickerUserRequestDTO userRequestDTO) {
        // Check if username already exists
        if (existsByUsername(userRequestDTO.getUsername())) {
            throw new RuntimeException("Username already exists: " + userRequestDTO.getUsername());
        }

        // Check if email already exists
        if (existsByEmail(userRequestDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + userRequestDTO.getEmail());
        }

        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(userRequestDTO.getPassword()); // In production, encrypt this!
        user.setEmail(userRequestDTO.getEmail());
        user.setFullName(userRequestDTO.getFullName());
        user.setPhone(userRequestDTO.getPhone());
        user.setRole(userRequestDTO.getRole() != null ? userRequestDTO.getRole() : User.UserRole.CUSTOMER);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return tickerUserRepository.save(user);
    }

    @Override
    public User registerUser(TickerUserRequestDTO userRequestDTO) {
        // For registration, default role is CUSTOMER
        if (userRequestDTO.getRole() == null) {
            userRequestDTO.setRole(User.UserRole.CUSTOMER);
        }
        return createUser(userRequestDTO);
    }

    @Override
    public User login(TickerUserLoginRequestDTO loginRequestDTO) {
        return tickerUserRepository.findByUsernameAndPassword(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }

    @Override
    public User updateUser(String id, TickerUserRequestDTO userRequestDTO) {
        Optional<User> userOpt = tickerUserRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if username is being changed and conflicts with existing user
            if (!user.getUsername().equals(userRequestDTO.getUsername()) &&
                    existsByUsername(userRequestDTO.getUsername())) {
                throw new RuntimeException("Username already exists: " + userRequestDTO.getUsername());
            }

            // Check if email is being changed and conflicts with existing user
            if (!user.getEmail().equals(userRequestDTO.getEmail()) &&
                    existsByEmail(userRequestDTO.getEmail())) {
                throw new RuntimeException("Email already exists: " + userRequestDTO.getEmail());
            }

            user.setUsername(userRequestDTO.getUsername());
            if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
                user.setPassword(userRequestDTO.getPassword()); // Encrypt in production!
            }
            user.setEmail(userRequestDTO.getEmail());
            user.setFullName(userRequestDTO.getFullName());
            user.setPhone(userRequestDTO.getPhone());
            if (userRequestDTO.getRole() != null) {
                user.setRole(userRequestDTO.getRole());
            }
            user.setUpdatedAt(LocalDateTime.now());

            return tickerUserRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + id);
    }

    @Override
    public void deleteUser(String id) {
        Optional<User> userOpt = tickerUserRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false);
            user.setUpdatedAt(LocalDateTime.now());
            tickerUserRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public User getUserById(String id) {
        return tickerUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User getUserByUsername(String username) {
        return tickerUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public List<User> getAllUsers() {
        return tickerUserRepository.findAll();
    }

    @Override
    public List<User> getUsersByRole(User.UserRole role) {
        return tickerUserRepository.findByRole(role);
    }

    @Override
    public List<User> getActiveUsers() {
        return tickerUserRepository.findByActiveTrue();
    }

    @Override
    public User activateUser(String id) {
        Optional<User> userOpt = tickerUserRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(true);
            user.setUpdatedAt(LocalDateTime.now());
            return tickerUserRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + id);
    }

    @Override
    public User deactivateUser(String id) {
        Optional<User> userOpt = tickerUserRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false);
            user.setUpdatedAt(LocalDateTime.now());
            return tickerUserRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + id);
    }

    @Override
    public List<User> searchUsers(String keyword) {
        return tickerUserRepository.searchActiveUsers(keyword);
    }

    @Override
    public boolean existsByUsername(String username) {
        return tickerUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return tickerUserRepository.existsByEmail(email);
    }
}