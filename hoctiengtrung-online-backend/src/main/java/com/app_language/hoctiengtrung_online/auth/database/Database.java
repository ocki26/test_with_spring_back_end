package com.app_language.hoctiengtrung_online.auth.database;


import com.app_language.hoctiengtrung_online.auth.model.RefCodeGenerator;
import com.app_language.hoctiengtrung_online.auth.model.Role;
import com.app_language.hoctiengtrung_online.auth.model.User;
import com.app_language.hoctiengtrung_online.auth.repository.UserRepository;
import com.app_language.hoctiengtrung_online.wallet.repository.WalletRepository;
import com.app_language.hoctiengtrung_online.wallet.service.WalletService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class Database {

    @Bean
    CommandLineRunner initDatabase(WalletRepository walletRepository, UserRepository userRepository,
                                   PasswordEncoder passwordEncoder, WalletService walletService ) {
        return args -> {


            if (userRepository.findByUsername("admin").isEmpty()) {
                String uniqueRefCode = RefCodeGenerator.generateUniqueRefCode(userRepository);
                User admin = User.builder()
                        .username("admin")
                        .email("admin@gmail.com")
                        .phone("84965741051")
                        .password(passwordEncoder.encode("A123456a@"))  // Mã hóa mật khẩu
                        .role(Role.ADMIN).refCode(uniqueRefCode)
                        .createdAt(LocalDateTime.now())
                        .balance(java.math.BigDecimal.ZERO)
                        .build();
                walletService.createWallet(admin.getEmail());
               User newUser =   userRepository.save(admin);

                System.out.println("✅ Tài khoản admin mặc định đã được khởi tạo.");
            }

        };
    }
}
