package com.app_language.hoctiengtrung_online.Ticker.controller;





import com.app_language.hoctiengtrung_online.Ticker.dto.TickerUserLoginRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.TickerUserRequestDTO;
import com.app_language.hoctiengtrung_online.Ticker.dto.Response.ApiResponseDTO;
import com.app_language.hoctiengtrung_online.Ticker.model.User;
import com.app_language.hoctiengtrung_online.Ticker.service.TickerUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController("tickerUserController")
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class TickerUserRepository {

    @Autowired
    private TickerUserService tickerUserService;

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<ApiResponseDTO> createUser(@Valid @RequestBody TickerUserRequestDTO tickerUserRequestDTO) {
        User user = tickerUserService.createUser(tickerUserRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("User created successfully", user));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponseDTO> registerUser(@Valid @RequestBody TickerUserRequestDTO tickerUserRequestDTO) {
        User user = tickerUserService.registerUser(tickerUserRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("User registered successfully", user));
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<ApiResponseDTO> login(@Valid @RequestBody TickerUserLoginRequestDTO loginRequestDTO) {
        User user = tickerUserService.login(loginRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Login successful", user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user")
    public ResponseEntity<ApiResponseDTO> updateUser(@PathVariable String id,
                                                     @Valid @RequestBody TickerUserRequestDTO tickerUserRequestDTO) {
        User user = tickerUserService.updateUser(id, tickerUserRequestDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("User updated successfully", user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<ApiResponseDTO> deleteUser(@PathVariable String id) {
        tickerUserService.deleteUser(id);
        return ResponseEntity.ok(ApiResponseDTO.success("User deleted successfully", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponseDTO> getUserById(@PathVariable String id) {
        User user = tickerUserService.getUserById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("User retrieved successfully", user));
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username")
    public ResponseEntity<ApiResponseDTO> getUserByUsername(@PathVariable String username) {
        User user = tickerUserService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponseDTO.success("User retrieved successfully", user));
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponseDTO> getAllUsers() {
        List<User> users = tickerUserService.getAllUsers();
        return ResponseEntity.ok(ApiResponseDTO.success("Users retrieved successfully", users));
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Get users by role")
    public ResponseEntity<ApiResponseDTO> getUsersByRole(@PathVariable User.UserRole role) {
        List<User> users = tickerUserService.getUsersByRole(role);
        return ResponseEntity.ok(ApiResponseDTO.success("Users retrieved successfully", users));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active users")
    public ResponseEntity<ApiResponseDTO> getActiveUsers() {
        List<User> users = tickerUserService.getActiveUsers();
        return ResponseEntity.ok(ApiResponseDTO.success("Active users retrieved successfully", users));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a user")
    public ResponseEntity<ApiResponseDTO> activateUser(@PathVariable String id) {
        User user = tickerUserService.activateUser(id);
        return ResponseEntity.ok(ApiResponseDTO.success("User activated successfully", user));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a user")
    public ResponseEntity<ApiResponseDTO> deactivateUser(@PathVariable String id) {
        User user = tickerUserService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponseDTO.success("User deactivated successfully", user));
    }
}