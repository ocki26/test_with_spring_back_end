package com.app_language.hoctiengtrung_online.Ticker.service;



import com.app_language.hoctiengtrung_online.Ticker.dto.TickerUserLoginRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.TickerUserRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.User;
import java.util.List;

public interface TickerUserService {
    User createUser(TickerUserRequestDTO userRequestDTO);
    User registerUser(TickerUserRequestDTO userRequestDTO);
    User login(TickerUserLoginRequestDTO loginRequestDTO);
    User updateUser(String id, TickerUserRequestDTO userRequestDTO);
    void deleteUser(String id);
    User getUserById(String id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    List<User> getUsersByRole(User.UserRole role);
    List<User> getActiveUsers();
    User activateUser(String id);
    User deactivateUser(String id);
    List<User> searchUsers(String keyword);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}