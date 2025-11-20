package com.app_language.hoctiengtrung_online.wallet.controller;
import com.app_language.hoctiengtrung_online.auth.model.User;
import com.app_language.hoctiengtrung_online.auth.repository.UserRepository;
import com.app_language.hoctiengtrung_online.wallet.dto.DepositRequest;
import com.app_language.hoctiengtrung_online.wallet.dto.WithdrawRequest;
import com.app_language.hoctiengtrung_online.wallet.model.Transaction;
import com.app_language.hoctiengtrung_online.wallet.model.Wallet;
import com.app_language.hoctiengtrung_online.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);
    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private final UserRepository userRepository;


    // Sử dụng múi giờ Việt Nam (Asia/Ho_Chi_Minh) cho các tham số ngày tháng từ người dùng
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private WalletService walletService;

    public WalletController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable String userId) {
        return ResponseEntity.ok(walletService.getWalletByUser(userId));
    }

    @PostMapping("/deposit-winner")
    public ResponseEntity<Transaction> deposit(@RequestBody DepositRequest request) {
        return ResponseEntity.ok(walletService.depositWinner(request.getUserId(), request.getAmount(), request.getMethod()));
    }

    @DeleteMapping("/delete-all-transaction")
    public ResponseEntity<?> deleteAll() {
        walletService.deleteAll();
        return ResponseEntity.ok("Delete All transaction");
    }

    @PostMapping("/deposit-admin")
    public ResponseEntity<Transaction> depositAdmin(@RequestBody DepositRequest request) {
        return ResponseEntity.ok(walletService.depositAdmin(request.getUserId(), request.getAmount(), request.getMethod()));
    }

    @PostMapping("/withdraw-admin")
    public ResponseEntity<Transaction> withdrawAdmin(@RequestBody DepositRequest request) {
        return ResponseEntity.ok(walletService.withdrawAdmin(request.getUserId(), request.getAmount(), request.getMethod()));
    }


    @PostMapping("/reset")
    public ResponseEntity<Transaction> reset(@RequestBody DepositRequest request) {
        return ResponseEntity.ok(walletService.resetDeposite(request.getUserId()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(walletService.withdraw(request.getUserId(), request.getAmount()));
    }


    // =============================================================
    // ====> CÁC API MỚI ĐỂ LẤY LỊCH SỬ GIAO DỊCH <====
    // =============================================================

    /**
     * Lấy lịch sử giao dịch của người dùng đang đăng nhập.
     */
    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getCurrentUserHistory(@AuthenticationPrincipal UserDetails userDetails) {
        List<Transaction> transactions = walletService.getTransactionsByUserId(userDetails.getUsername());
        return ResponseEntity.ok(transactions);
    }

    /**
     * [ADMIN] Lấy lịch sử giao dịch của một người dùng cụ thể.
     */
    @GetMapping("/history/user/{userId}")
    public ResponseEntity<List<Transaction>> getUserHistory(@PathVariable String userId) {
        List<Transaction> transactions = walletService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * [ADMIN] Lấy lịch sử giao dịch của tất cả người dùng trong một khoảng ngày.
     */
    @GetMapping("/admin/history-range")
    public ResponseEntity<?> getAllTransactionsByDateRange(@RequestParam String fromDate, @RequestParam String toDate) {
        try {
            LocalDateTime startUTC = parseToUtcStartOfDay(fromDate);
            LocalDateTime endUTC = parseToUtcEndOfDay(toDate);
            List<Transaction> transactions = walletService.getAllTransactionsByDateRange(startUTC, endUTC);
            return ResponseEntity.ok(transactions);
        } catch (DateTimeParseException e) {
            logger.warn("Yêu cầu getAllTransactionsByDateRange không hợp lệ: from={}, to={}", fromDate, toDate, e);
            return ResponseEntity.badRequest().body("Định dạng ngày không hợp lệ. Vui lòng dùng yyyy-MM-dd.");
        }
    }

    // Hàm trợ giúp để xử lý múi giờ
    private LocalDateTime parseToUtcStartOfDay(String date) {
        ZonedDateTime startVN = LocalDate.parse(date).atStartOfDay(VIETNAM_ZONE);
        return startVN.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    private LocalDateTime parseToUtcEndOfDay(String date) {
        ZonedDateTime endVN = LocalDate.parse(date).atTime(LocalTime.MAX).atZone(VIETNAM_ZONE);
        return endVN.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    //=============================================================
    // ====> CÁC API MỚI ĐỂ LẤY LỊCH SỬ RÚT/NẠP THEO NGÀY <====
    // =============================================================

    /**
     * Lấy lịch sử giao dịch RÚT TIỀN của người dùng đang đăng nhập trong khoảng ngày.
     * Mặc định: 7 ngày gần nhất.
     */

    @GetMapping("/history/withdrawals")
    public ResponseEntity<?> withdrawFull(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {

        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        LocalDateTime endVN = LocalDateTime.now(VIETNAM_ZONE);
        LocalDateTime startVN = endVN.minusWeeks(1); // Mặc định là 1 tuần trước
        if (fromDate != null) {
            startVN = LocalDate.parse(fromDate, DATE_FORMATTER).atStartOfDay();
        }
        if (toDate != null) {
            endVN = LocalDate.parse(toDate, DATE_FORMATTER).atTime(LocalTime.MAX);
        }
        LocalDateTime startUTC = startVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endUTC = endVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        List<Transaction> transactions = walletService.getTransactionWithdrawFullByUserID(currentUser.getEmail(), startUTC, endUTC);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/history/withdrawals/success")
    public ResponseEntity<?> withdrawSuccess(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {
        LocalDateTime endVN = LocalDateTime.now(VIETNAM_ZONE);
        LocalDateTime startVN = endVN.minusWeeks(1); // Mặc định là 1 tuần trước
        if (fromDate != null) {
            startVN = LocalDate.parse(fromDate, DATE_FORMATTER).atStartOfDay();
        }
        if (toDate != null) {
            endVN = LocalDate.parse(toDate, DATE_FORMATTER).atTime(LocalTime.MAX);
        }
        LocalDateTime startUTC = startVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endUTC = endVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        List<Transaction> transactions = walletService.getTransactionWithdrawSuccess(startUTC, endUTC);
        return ResponseEntity.ok(transactions);
    }
    @GetMapping("/history/withdrawals/pending")
    public ResponseEntity<?> withdrawPending(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {
        LocalDateTime endVN = LocalDateTime.now(VIETNAM_ZONE);
        LocalDateTime startVN = endVN.minusWeeks(1); // Mặc định là 1 tuần trước
        if (fromDate != null) {
            startVN = LocalDate.parse(fromDate, DATE_FORMATTER).atStartOfDay();
        }
        if (toDate != null) {
            endVN = LocalDate.parse(toDate, DATE_FORMATTER).atTime(LocalTime.MAX);
        }
        LocalDateTime startUTC = startVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endUTC = endVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        List<Transaction> transactions = walletService.getTransactionWithdrawPending(startUTC, endUTC);
        return ResponseEntity.ok(transactions);
    }


    @GetMapping("/history/deposits")
    public ResponseEntity<?> depositHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate)
    {

        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        LocalDateTime endVN = LocalDateTime.now(VIETNAM_ZONE);
        LocalDateTime startVN = endVN.minusWeeks(1); // Mặc định là 1 tuần trước
        if (fromDate != null) {
            startVN = LocalDate.parse(fromDate, DATE_FORMATTER).atStartOfDay();
        }
        if (toDate != null) {
            endVN = LocalDate.parse(toDate, DATE_FORMATTER).atTime(LocalTime.MAX);
        }
        LocalDateTime startUTC = startVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endUTC = endVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        List<Transaction> transactions = walletService.getTransactionDepositFull(currentUser.getEmail(), startUTC, endUTC);
        return ResponseEntity.ok(transactions);

    }

    @GetMapping("/history/deposit/success")
    public ResponseEntity<?> depositSuccess(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {
        LocalDateTime endVN = LocalDateTime.now(VIETNAM_ZONE);
        LocalDateTime startVN = endVN.minusWeeks(1); // Mặc định là 1 tuần trước
        if (fromDate != null) {
            startVN = LocalDate.parse(fromDate, DATE_FORMATTER).atStartOfDay();
        }
        if (toDate != null) {
            endVN = LocalDate.parse(toDate, DATE_FORMATTER).atTime(LocalTime.MAX);
        }
        LocalDateTime startUTC = startVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endUTC = endVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        List<Transaction> transactions = walletService.getTransactionDepositSuccess(startUTC, endUTC);
        return ResponseEntity.ok(transactions);
    }
    @GetMapping("/history/deposit/failed")
    public ResponseEntity<?> depositFailed(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {
        LocalDateTime endVN = LocalDateTime.now(VIETNAM_ZONE);
        LocalDateTime startVN = endVN.minusWeeks(1); // Mặc định là 1 tuần trước
        if (fromDate != null) {
            startVN = LocalDate.parse(fromDate, DATE_FORMATTER).atStartOfDay();
        }
        if (toDate != null) {
            endVN = LocalDate.parse(toDate, DATE_FORMATTER).atTime(LocalTime.MAX);
        }
        LocalDateTime startUTC = startVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endUTC = endVN.atZone(VIETNAM_ZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        List<Transaction> transactions = walletService.getTransactionDepositFailed(startUTC, endUTC);
        return ResponseEntity.ok(transactions);
    }

}

