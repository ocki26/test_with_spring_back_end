package com.app_language.hoctiengtrung_online.wallet.repository;


import com.app_language.hoctiengtrung_online.wallet.model.Transaction;
import com.app_language.hoctiengtrung_online.wallet.model.TransactionStatus;
import com.app_language.hoctiengtrung_online.wallet.model.TransactionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByUserId(String userId);

    List<Transaction> findByUserIdOrderByCreatedAtDesc(String userId);


    Optional<Transaction> findByRefCode(String refCode);

    List<Transaction> findAllByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    List<Transaction> findByStatusOrderByCreatedAtAsc(TransactionStatus status);
    List<Transaction> findAllByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(String userId, LocalDateTime start, LocalDateTime end);

    List<Transaction> findByTypeAndStatusOrderByCreatedAtAsc(TransactionType type, TransactionStatus status);
    List<Transaction> findAllByTypeAndCreatedAtBetweenOrderByCreatedAtDesc(TransactionType type, LocalDateTime start, LocalDateTime end);

     List<Transaction> findAllByUserIdAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(String userId, TransactionType type, LocalDateTime start, LocalDateTime end);

    List<Transaction> findAllByTypeAndStatusAndCreatedAtBetweenOrderByCreatedAtDesc(
            TransactionType type,
            TransactionStatus status,
            LocalDateTime start,
            LocalDateTime end
    );





    // ✅ Lọc theo status
    List<Transaction> findAllByStatusOrderByCreatedAtDesc(
            TransactionStatus status
    );

    // ✅ Lọc theo ngày + status (thường dùng cho thống kê hoặc lọc giao dịch rút tiền)
    List<Transaction> findAllByStatusAndCreatedAtBetweenOrderByCreatedAtDesc(
            TransactionStatus status,
            LocalDateTime start,
            LocalDateTime end
    );

    // ================================
    // ✅ Lọc theo status + type + ngày  (FULL FILTER)
    // ================================
    List<Transaction> findAllByStatusAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(
            TransactionStatus status,
            TransactionType type,
            LocalDateTime start,
            LocalDateTime end
    );

}
