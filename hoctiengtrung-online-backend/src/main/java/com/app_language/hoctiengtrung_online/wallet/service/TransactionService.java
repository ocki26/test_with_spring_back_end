package com.app_language.hoctiengtrung_online.wallet.service;

import com.app_language.hoctiengtrung_online.wallet.model.Transaction;
import com.app_language.hoctiengtrung_online.wallet.model.TransactionStatus;
import com.app_language.hoctiengtrung_online.wallet.model.TransactionType;
import com.app_language.hoctiengtrung_online.wallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
   // private final OnePayService onePayService;

    // Inject TransactionRepository
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
     //   this.onePayService = onePayService;
    }

    /**
     * Tạo một giao dịch rút tiền (withdrawal) mới với trạng thái PENDING.
     * Cần kiểm tra refCode để tránh tạo giao dịch trùng lặp.
     *
     * @param refCode Mã tham chiếu duy nhất cho giao dịch (ví dụ: ID yêu cầu từ bên ngoài).
     * @param userId ID của người dùng thực hiện rút tiền.
     * @param walletId ID của ví tiền bị trừ tiền.
     * @param amount Số tiền muốn rút.
     * @param description Mô tả giao dịch.
     * @return Đối tượng Transaction đã được lưu với trạng thái PENDING.
     * @throws IllegalStateException nếu refCode đã tồn tại.
     */
    @Transactional
    public Transaction createPendingWithdrawal(String refCode, String userId, String walletId, BigDecimal amount, String description) {
        // 1. Kiểm tra refCode (Mã tham chiếu) đã tồn tại chưa để tránh giao dịch trùng lặp
        Optional<Transaction> existingTransaction = transactionRepository.findByRefCode(refCode);
        if (existingTransaction.isPresent()) {
            // Tùy chọn: ném exception hoặc trả về giao dịch đã tồn tại
            throw new IllegalStateException("Transaction with refCode " + refCode + " already exists.");
        }

        // 2. Kiểm tra điều kiện đầu vào cơ bản
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (userId == null || userId.isEmpty() || walletId == null || walletId.isEmpty()) {
            throw new IllegalArgumentException("User ID and Wallet ID must be provided.");
        }


        // 3. Tạo đối tượng Transaction
        Transaction transaction = new Transaction();

        transaction.setRefCode(refCode);
        transaction.setUserId(userId);
        transaction.setWalletId(walletId);
        transaction.setAmount(amount.negate()); // Số tiền rút nên được lưu dưới dạng số âm để dễ dàng tính toán số dư ví.
        // HOẶC: Lưu số dương và dựa vào Type (WITHDRAW) để biết đây là trừ tiền.
        // Trong ví dụ này, tôi sẽ giữ số dương và dựa vào TransactionType.
        // Nếu bạn muốn lưu số âm, hãy bỏ comment dòng trên và comment dòng dưới.
//        transaction.setAmount(amount);
//
        transaction.setDescription(description != null ? description : "Pending withdrawal");
        transaction.setType(TransactionType.WITHDRAW);

        // Thiết lập trạng thái và thời gian tạo (Mặc dù có giá trị mặc định trong model, thiết lập lại để rõ ràng)
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreatedAt(LocalDateTime.now());

        // 4. Lưu giao dịch vào cơ sở dữ liệu
        return transactionRepository.save(transaction);
    }



    /**
     * Cập nhật trạng thái của một giao dịch dựa trên ID.
     *
     * @param transactionId ID của giao dịch.
     * @param newStatus Trạng thái mới (ví dụ: COMPLETED, FAILED).
     * @return Đối tượng Transaction đã được cập nhật.
     * @throws RuntimeException nếu không tìm thấy giao dịch.
     */
    @Transactional
    public Transaction updateTransactionStatus(String transactionId, TransactionStatus newStatus) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));

        if (transaction.getStatus() != newStatus) {
            transaction.setStatus(newStatus);
            // Lưu lại giao dịch đã được cập nhật trạng thái
            return transactionRepository.save(transaction);
        }

        return transaction; // Trả về nếu trạng thái không thay đổi
    }

    /**
     * Cập nhật trạng thái của một giao dịch dựa trên refCode.
     *
     * @param refCode Mã tham chiếu của giao dịch.
     * @param newStatus Trạng thái mới (ví dụ: COMPLETED, FAILED).
     * @return Đối tượng Transaction đã được cập nhật.
     * @throws RuntimeException nếu không tìm thấy giao dịch.
     */
    @Transactional
    public Transaction updateTransactionStatusByRefCode(String refCode, TransactionStatus newStatus) {
        Transaction transaction = transactionRepository.findByRefCode(refCode)
                .orElseThrow(() -> new RuntimeException("Transaction not found with refCode: " + refCode));

        if (transaction.getStatus() != newStatus) {
            transaction.setStatus(newStatus);
            // Lưu lại giao dịch đã được cập nhật trạng thái
            return transactionRepository.save(transaction);
        }

        return transaction; // Trả về nếu trạng thái không thay đổi
    }

    /**
     * Tìm tất cả các giao dịch của một người dùng.
     *
     * @param userId ID của người dùng.
     * @return Danh sách giao dịch, sắp xếp theo thời gian tạo giảm dần.
     */
    public List<Transaction> findTransactionsByUserId(String userId) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Tìm tất cả các giao dịch trong một khoảng thời gian cụ thể.
     *
     * @param start Thời gian bắt đầu.
     * @param end Thời gian kết thúc.
     * @return Danh sách giao dịch.
     */
    public List<Transaction> findTransactionsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findAllByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
    }

    /**
     * Tìm tất cả các giao dịch của một người dùng trong một khoảng thời gian.
     *
     * @param userId ID của người dùng.
     * @param start Thời gian bắt đầu.
     * @param end Thời gian kết thúc.
     * @return Danh sách giao dịch.
     */
    public List<Transaction> findUserTransactionsByTimeRange(String userId, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findAllByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end);
    }

    /**
     * Tìm một giao dịch theo ID.
     *
     * @param transactionId ID của giao dịch.
     * @return Optional chứa Transaction nếu tìm thấy.
     */
    public Optional<Transaction> findTransactionById(String transactionId) {
        return transactionRepository.findById(transactionId);
    }

    /**
     * Tìm một giao dịch theo Mã tham chiếu (refCode).
     *
     * @param refCode Mã tham chiếu của giao dịch.
     * @return Optional chứa Transaction nếu tìm thấy.
     */
    public Optional<Transaction> findTransactionByRefCode(String refCode) {
        return transactionRepository.findByRefCode(refCode);
    }
// Trong com.xosotv.live.xosotv_backend.wallet_service.service.TransactionService
// ... (các hàm đã có)
// Thêm dependencies cần thiết cho hàm này (WalletService, OnePayService)
// (Giả sử bạn đã inject chúng, hoặc bạn có thể tạo một service mới là WithdrawalAdminService)
// *** Lưu ý: Bạn cần inject OnePayService vào TransactionService hoặc tạo một service khác.
// Vì PaymentController đã inject OnePayService, tôi sẽ tạo một hàm giả định trong đây.
// Nếu bạn muốn clean architecture hơn, hãy tạo một service layer mới cho logic duyệt.
// Giả định: OnePayService được inject vào TransactionService (cần sửa constructor)
/*
    private final OnePayService onePayService;
    public TransactionService(TransactionRepository transactionRepository, OnePayService onePayService) {
        this.transactionRepository = transactionRepository;
        this.onePayService = onePayService;
    }
*/
    /**
     * Hàm nghiệp vụ được gọi bởi Admin để duyệt và thực hiện lệnh rút tiền PENDING.
     * HÀM NÀY PHẢI ĐƯỢC GỌI SAU KHI ADMIN XÁC NHẬN.
     * @param transactionId ID của giao dịch cần duyệt.
     * @return Giao dịch đã được gửi đi OnePay và chuyển trạng thái (ví dụ: PROCESSING).
     * @throws RuntimeException nếu không tìm thấy giao dịch hoặc trạng thái không phải PENDING.
     */


    // Thêm các hàm nghiệp vụ khác (ví dụ: updateTransactionStatus, findTransactions...)

    /**
     * Lấy danh sách tất cả các giao dịch rút tiền đang chờ phê duyệt.
     * Sắp xếp theo thời gian tạo cũ nhất trước (để duyệt theo thứ tự).
     *
     * @return Danh sách các giao dịch rút tiền PENDING.
     */
    public List<Transaction> getPendingWithdrawalTransactions(LocalDateTime at, LocalDateTime end) {
        return transactionRepository.findAllByTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
                TransactionType.WITHDRAW,
                 at, end
        );
    }

    public List<Transaction> getSuccessWithdrawalTransactions(LocalDateTime at, LocalDateTime end) {
        return transactionRepository.findAllByTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
                TransactionType.WITHDRAW,
                 at,end
        );
    }



    public List<Transaction> findUserTransactionsByTypeAndDateRange(String userId, TransactionType type, LocalDateTime start, LocalDateTime end) {
        // Lưu ý: Bạn cần thêm method này vào TransactionRepository
        return transactionRepository.findAllByUserIdAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(userId, type, start, end);
    }


    // ✅ Lấy tất cả giao dịch trong ngày hôm nay (giờ Việt Nam)
    public List<Transaction> getTodayTransactions() {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDate today = LocalDate.now(zoneId);

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        return transactionRepository.findAllByCreatedAtBetweenOrderByCreatedAtDesc(startOfDay, endOfDay);
    }


    // ✅ Lấy danh sách yêu cầu rút tiền đang chờ xử lý trong ngày hôm nay
    public List<Transaction> getTodayPendingWithdrawals() {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDate today = LocalDate.now(zoneId);

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        return transactionRepository.findAllByTypeAndStatusAndCreatedAtBetweenOrderByCreatedAtDesc(
                TransactionType.WITHDRAW,
                TransactionStatus.PENDING,
                startOfDay,
                endOfDay
        );
    }

    /**
     * ✅ Lấy danh sách giao dịch rút tiền (WITHDRAW + PENDING)
     * trong khoảng ngày (theo giờ Việt Nam)
     */
    public List<Transaction> getPendingWithdrawalsByDateRange(String startDate, String endDate) {
        try {
            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
            LocalDate endLocalDate = LocalDate.parse(endDate, formatter);

            LocalDateTime start = startLocalDate.atStartOfDay();
            LocalDateTime end = endLocalDate.atTime(23, 59, 59);

            return transactionRepository.findAllByTypeAndStatusAndCreatedAtBetweenOrderByCreatedAtDesc(
                    TransactionType.WITHDRAW,
                    TransactionStatus.PENDING,
                    start,
                    end
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy dữ liệu rút tiền theo ngày: " + e.getMessage());
        }
    }




//    public List<Transaction> findUserTransactionsByTypeAndDateRange( TransactionType type, LocalDateTime start, LocalDateTime end) {
//        // Lưu ý: Bạn cần thêm method này vào TransactionRepository
//        return transactionRepository.findByUserIdOrderByCreatedAtDesc(type, start, end);
//    }
}
