package com.app_language.hoctiengtrung_online.auth.controller;

import com.app_language.hoctiengtrung_online.auth.dto.AgentRegistrationRequest;
import com.app_language.hoctiengtrung_online.auth.dto.ChangePasswordRequest;
import com.app_language.hoctiengtrung_online.auth.dto.CreateUserRequest;
import com.app_language.hoctiengtrung_online.auth.dto.UserProfileResponse;
import com.app_language.hoctiengtrung_online.auth.model.AgentDetails;
import com.app_language.hoctiengtrung_online.auth.model.RefCodeGenerator;
import com.app_language.hoctiengtrung_online.auth.model.Role;
import com.app_language.hoctiengtrung_online.auth.model.User;
import com.app_language.hoctiengtrung_online.auth.repository.UserRepository;
import com.app_language.hoctiengtrung_online.wallet.model.Wallet;
import com.app_language.hoctiengtrung_online.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, WalletService walletService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.walletService = walletService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + userDetails.getUsername()));

        Wallet wallet = walletService.getWalletByUser(user.getEmail());

        if (user.getRefCode() != null && !user.getRefCode().isBlank()) {
            long referralCount = userRepository.countByReferrerCode(user.getRefCode());
            user.setReferralCount((int) referralCount);
        }

        return ResponseEntity.ok(new UserProfileResponse(user, wallet));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu hiện tại không chính xác");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    /**
     * [ADMIN] Lấy danh sách tất cả người dùng trong hệ thống.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            if (user.getRefCode() != null && !user.getRefCode().isBlank()) {
                user.setReferralCount((int) userRepository.countByReferrerCode(user.getRefCode()));
            }
        });

        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('DAILY')")
    @GetMapping("/admin/referrals")
    public ResponseEntity<List<User>> getReferralsByRefCode(@AuthenticationPrincipal UserDetails userDetails) {


        User userFound = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + userDetails.getUsername()));

        String refCode = userFound.getRefCode();
        if (refCode == null || refCode.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Gọi phương thức Repository mới để tìm tất cả người dùng có referrerCode khớp
        List<User> referredUsers = userRepository.findAllByReferrerCode(refCode);

        // Tùy chọn: Tính referralCount cho các cấp dưới này nếu cần
        referredUsers.forEach(user -> {
            if (user.getRefCode() != null && !user.getRefCode().isBlank()) {
                user.setReferralCount((int) userRepository.countByReferrerCode(user.getRefCode()));
            }
        });

        return ResponseEntity.ok(referredUsers);
    }



    // =============================================================
    // === API: LẤY CẤP DƯỚI THEO REFCODE (ADMIN/DAILY) ===
    // =============================================================

    /**
     * [ADMIN] Lấy danh sách tất cả người dùng được giới thiệu (cấp dưới trực tiếp)
     * bởi một mã giới thiệu (refCode) cụ thể.
     * Endpoint: GET /api/user/admin/referrals/{refCode}
     * LƯU Ý: Phải là Admin mới xem được refCode bất kỳ.
     * @param refCode Mã giới thiệu của Đại lý/User cha.
     * @return Danh sách người dùng được giới thiệu.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('DAILY')") // Giả định DAILY chỉ tự xem được cấp dưới của mình
    @GetMapping("/admin/referrals/{refCode}")
    public ResponseEntity<List<User>> getReferralsByRefCode(@PathVariable String refCode, @AuthenticationPrincipal UserDetails userDetails) {

        // Nếu là DAILY, chỉ cho phép xem cấp dưới của chính mình
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DAILY"))) {
            User currentUser = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            if (currentUser.getRefCode() == null || !currentUser.getRefCode().equals(refCode)) {
                // Nếu Daily cố gắng xem mã ref không phải của mình, chặn
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập mã giới thiệu này.");
            }
        }

        if (refCode == null || refCode.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<User> referredUsers = userRepository.findAllByReferrerCode(refCode);

        // Tính referralCount cho các cấp dưới này (cho mục đích hiển thị phân cấp)
        referredUsers.forEach(user -> {
            if (user.getRefCode() != null && !user.getRefCode().isBlank()) {
                user.setReferralCount((int) userRepository.countByReferrerCode(user.getRefCode()));
            }
        });

        return ResponseEntity.ok(referredUsers);
    }

    // =============================================================
    // === CÁC API TẠO TÀI KHOẢN (ADMIN ONLY) ===
    // =============================================================

    /**
     * [ADMIN] Tạo tài khoản Quản trị (ADMIN).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create-admin")
    public ResponseEntity<User> createAdmin(@RequestBody @Valid CreateUserRequest request) {
        User newAdmin = createUser(request, Role.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAdmin);
    }

    /**
     * [ADMIN] Tạo tài khoản Đại lý (DAILY).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create-agent")
    public ResponseEntity<User> createAgent(@RequestBody @Valid AgentRegistrationRequest request) {
        User newAgent = registerAgent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAgent);
    }

    /**
     * [ADMIN] Tạo tài khoản Vận hành (VANHANH).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create-operator")
    public ResponseEntity<User> createOperator(@RequestBody @Valid CreateUserRequest request) {
        User newOperator = createUser(request, Role.VANHANH);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOperator);
    }

    /**
     * [ADMIN] Tạo tài khoản Người dùng Thưởng (USER).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create-user-thuong")
    public ResponseEntity<User> createUserThuong(@RequestBody @Valid CreateUserRequest request) {
        User newUserThuong = createUser(request, Role.USER);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserThuong);
    }


    // =============================================================
    // === CÁC HÀM XỬ LÝ NỘI BỘ (PRIVATE METHODS) ===
    // =============================================================

    /**
     * Hàm dùng chung để tạo người dùng với một Role cụ thể (trừ DAILY).
     */
    private User createUser(@Valid CreateUserRequest request, Role role) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã được sử dụng.");
        }

        String uniqueRefCode = RefCodeGenerator.generateUniqueRefCode(userRepository);

        User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .refCode(uniqueRefCode)
                .phone(request.getPhone())
                .username(request.getUsername() != null && !request.getUsername().isBlank() ? request.getUsername() : request.getEmail())
                .referrerCode(request.getReferrerCode() != null && !request.getReferrerCode().isBlank() ? request.getReferrerCode() : null)
                // Chỉ set AgentDetails cho DAILY
                .agentDetails(role == Role.DAILY ? new AgentDetails() : null)
                .build();

        newUser = userRepository.save(newUser);

        walletService.createWallet(newUser.getEmail());

        return newUser;
    }

    /**
     * Hàm nội bộ để đăng ký tài khoản Đại lý (DAILY) với chi tiết AgentDetails.
     */
    public User registerAgent(AgentRegistrationRequest request) {
        // 1. Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Lỗi: Email đã được sử dụng!");
        }

        // 2. Tạo đối tượng AgentDetails từ request
        AgentDetails agentDetails = new AgentDetails();
        agentDetails.setAgentCode(request.getAgentCode());
        agentDetails.setCommissionRate(request.getCommissionRate());
        agentDetails.setMaxCommissionRate(request.getMaxCommissionRate());
        agentDetails.setOperatingArea(request.getOperatingArea());
        agentDetails.setAllowSubAgents(request.isAllowSubAgents());
        agentDetails.setContactAddress(request.getContactAddress());
        agentDetails.setUplineAgentId(request.getUplineAgentId());
        agentDetails.setBusinessLicenseNumber(request.getBusinessLicenseNumber());
        agentDetails.setEmergencyContactName(request.getEmergencyContactName());
        agentDetails.setEmergencyContactPhone(request.getEmergencyContactPhone());
        agentDetails.setNotes(request.getNotes());

        // 3. Tạo User bằng Builder
        String uniqueRefCode = RefCodeGenerator.generateUniqueRefCode(userRepository);

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .refCode(uniqueRefCode)
                .role(Role.DAILY)
                .agentDetails(agentDetails)
                .build();

        // 4. Lưu User vào cơ sở dữ liệu
        user = userRepository.save(user);

        // 5. Tạo Wallet
        walletService.createWallet(user.getEmail());

        return user;
    }
}