package com.app_language.hoctiengtrung_online.wallet.service;

import com.app_language.hoctiengtrung_online.auth.model.User;
import com.app_language.hoctiengtrung_online.auth.repository.UserRepository;
import com.app_language.hoctiengtrung_online.wallet.model.Transaction;
import com.app_language.hoctiengtrung_online.wallet.model.TransactionStatus;
import com.app_language.hoctiengtrung_online.wallet.model.TransactionType;
import com.app_language.hoctiengtrung_online.wallet.model.Wallet;
import com.app_language.hoctiengtrung_online.wallet.repository.TransactionRepository;
import com.app_language.hoctiengtrung_online.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepo;


    @Autowired
    TransactionService transactionService;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepo;

    public Wallet saveWallet(Wallet wallet) {
        return walletRepo.save(wallet);
    }

    public Wallet createWallet(String userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setUpdatedAt(LocalDateTime.now());
        return walletRepo.save(wallet);
    }

    public Wallet getWalletByUser(String userId) {
        System.out.println("Wallet user:"+ userId);

        Optional<Wallet> optionalWallet =  walletRepo.findByUserId(userId);
        if(optionalWallet.isEmpty()){
            createWallet(userId);
        };
        return optionalWallet.get();
    }

    public Transaction deposit(String userId, BigDecimal amount, String method, String refCode) {
        Optional<User> userOption = userRepository.findByUsernameOrEmail(userId, userId);
        if(!userOption.isEmpty()){

            User user = userOption.get();
            Wallet wallet = getWalletByUser(user.getEmail());
            if(wallet != null){
                BigDecimal balance = wallet.getBalance().add(amount);
                wallet.setUserId(userId);
                if(!userOption.isEmpty())
                {
                    User found = userOption.get();
                    found.setBalance(balance);
                    userRepository.save(found);

                    System.out.println("Wallet:"+ wallet.toString());
                    wallet.setBalance(wallet.getBalance().add(amount));
                    wallet.setUpdatedAt(LocalDateTime.now());
                    walletRepo.save(wallet);
                    Optional<Transaction> txOptional = transactionRepo.findByRefCode(refCode);
                    if(txOptional.isPresent()){
                        Transaction tx = txOptional.get();
                        tx.setWalletId(wallet.getId());
                        tx.setUserId(userId);
                        tx.setType(TransactionType.DEPOSIT);
                        tx.setAmount(amount);
                        tx.setDescription("Nạp tiền qua " + method);
                        tx.setStatus(TransactionStatus.SUCCESS);
                        tx.setRefCode(refCode);

                        return transactionRepo.save(tx);
                    }
                }

            }
        }

        return null;

    }



    public void deleteAll(){

        transactionRepo.deleteAll();;
    }

    public Transaction depositPending(String userId, BigDecimal amount, String method, String refCode) {
        Wallet wallet = getWalletByUser(userId);
        Transaction tx = new Transaction();
        tx.setWalletId(wallet.getId());
        tx.setUserId(userId);
        tx.setType(TransactionType.DEPOSIT);
        tx.setAmount(amount);
        tx.setDescription("Nạp tiền qua " + method);
        tx.setStatus(TransactionStatus.PENDING);
        tx.setRefCode(refCode);
        return transactionRepo.save(tx);
    }


    public Transaction depositWinner(String userId, BigDecimal amount, String method) {
        Wallet wallet = getWalletByUser(userId);
       if(wallet != null){
           wallet.setUserId(userId);
            Optional<User> userOption = userRepository.findByEmail(userId);
            BigDecimal balance = wallet.getBalance().add(amount);
            if(!userOption.isEmpty())
            {
                User found = userOption.get();
                found.setBalance(balance);
                userRepository.save(found);

                System.out.println("Wallet:"+ wallet.toString());
                wallet.setBalance(wallet.getBalance().add(amount));
                wallet.setUpdatedAt(LocalDateTime.now());
                walletRepo.save(wallet);
                Transaction tx = new Transaction();
                tx.setWalletId(wallet.getId());
                tx.setUserId(found.getEmail());
                tx.setType(TransactionType.REWARD);
                tx.setAmount(amount);
                tx.setDescription("Nạp tiền qua " + method);
                tx.setStatus(TransactionStatus.SUCCESS);
                tx.setRefCode("winner");
                return transactionRepo.save(tx);
            }

        }
       return null;

    }

    public Transaction depositAdmin(String userId, BigDecimal amount, String method) {

        Optional<User> userOption = userRepository.findByUsernameOrEmail(userId,userId);
        if(!userOption.isEmpty())
        {
            User user = userOption.get();
            Wallet wallet = getWalletByUser(user.getEmail());
            if(wallet != null){
                wallet.setUserId(user.getEmail());
                if(!userOption.isEmpty())
                {
                    user.setBalance(BigDecimal.valueOf(user.getBalance().longValue() + amount.longValue()));
                    userRepository.save(user);
                    wallet.setBalance(wallet.getBalance().add(amount));
                    wallet.setUpdatedAt(LocalDateTime.now());
                    walletRepo.save(wallet);
                    Transaction tx = new Transaction();
                    tx.setWalletId(wallet.getId());
                    tx.setUserId(user.getEmail());
                    tx.setType(TransactionType.DEPOSIT);
                    tx.setAmount(amount);
                    tx.setDescription(method);
                    tx.setStatus(TransactionStatus.SUCCESS);
                    tx.setRefCode("Nạp tiền bởi admin");
                    return transactionRepo.save(tx);
                }
            }
        }
        return null;
    }


    public Transaction withdrawAdmin(String userId, BigDecimal amount, String method) {

        Optional<User> userOption = userRepository.findByUsernameOrEmail(userId,userId);
        if(!userOption.isEmpty())
        {
            User user = userOption.get();
            Wallet wallet = getWalletByUser(user.getEmail());
            if(wallet != null){
                wallet.setUserId(user.getEmail());
                if(!userOption.isEmpty() && amount.longValue() <= user.getBalance().longValue())
                {

                    BigDecimal money = BigDecimal.valueOf(user.getBalance().longValue() - amount.longValue());
                    user.setBalance(money);
                    userRepository.save(user);
                    wallet.setBalance(money);
                    wallet.setUpdatedAt(LocalDateTime.now());
                    walletRepo.save(wallet);
                    Transaction tx = new Transaction();
                    tx.setWalletId(wallet.getId());
                    tx.setUserId(user.getEmail());
                    tx.setType(TransactionType.WITHDRAW);
                    tx.setAmount(amount);
                    tx.setDescription(method);
                    tx.setStatus(TransactionStatus.SUCCESS);
                    tx.setRefCode("Từ tiền bởi admin");
                    return transactionRepo.save(tx);
                }
            }
        }
        return null;
    }

    public Transaction updateStatusTransactionSuccess(String userId,BigDecimal amount, String transactionId) {
        Wallet wallet = getWalletByUser(userId);
        if(wallet != null){
            wallet.setUserId(userId);
            Optional<User> userOption = userRepository.findByEmail(userId);
            BigDecimal balance = wallet.getBalance().add(amount);
            if(!userOption.isEmpty())
            {
                User found = userOption.get();
                found.setBalance(balance);
                userRepository.save(found);
                System.out.println("Wallet:"+ wallet.toString());
                wallet.setBalance(wallet.getBalance().add(amount));
                wallet.setUpdatedAt(LocalDateTime.now());
                walletRepo.save(wallet);
            }

        }
        Optional<Transaction> txOption = transactionRepo.findById(transactionId);
        if(!txOption.isEmpty()){
            Transaction tx = txOption.get();
            tx.setStatus(TransactionStatus.SUCCESS);
            return transactionRepo.save(tx);
        }
        return  null;

    }

    public Optional<Transaction> findTransactionByRefCode(String refCode){
        return transactionRepo.findByRefCode(refCode);
    }

    public Transaction resetDeposite(String userId) {
        Wallet wallet = getWalletByUser(userId);
        if(wallet != null){
            wallet.setUserId(userId);
            Optional<User> userOption = userRepository.findByEmail(userId);
            BigDecimal balance = BigDecimal.valueOf(0);
                    //wallet.getBalance().add(amount);
            if(!userOption.isEmpty())
            {
                User found = userOption.get();
                found.setBalance(balance);
                userRepository.save(found);
                System.out.println("Wallet:"+ wallet.toString());
                // wallet.setBalance(wallet.getBalance().add(amount));
                wallet.setBalance(BigDecimal.valueOf(0));
                wallet.setUpdatedAt(LocalDateTime.now());
                walletRepo.save(wallet);
                Transaction tx = new Transaction();
                tx.setWalletId(wallet.getId());
                tx.setUserId(userId);
                tx.setType(TransactionType.DEPOSIT);
                tx.setAmount(BigDecimal.valueOf(0));
                tx.setDescription("reset account ");
                tx.setStatus(TransactionStatus.SUCCESS);
                return transactionRepo.save(tx);
            }

        }
        return null;

    }

    public Transaction withdraw(String userId, BigDecimal amount) {
        Wallet wallet = getWalletByUser(userId);
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Không đủ số dư");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(wallet);
        Transaction tx = new Transaction();
        tx.setWalletId(wallet.getId());
        tx.setUserId(userId);
        tx.setType(TransactionType.WITHDRAW);
        tx.setAmount(amount);
        tx.setDescription("Rút tiền về tài khoản");
        tx.setStatus(TransactionStatus.SUCCESS);
        return transactionRepo.save(tx);
    }


    // =============================================================
    // ====> CÁC PHƯƠNG THỨC MỚI ĐỂ LẤY LỊCH SỬ GIAO DỊCH <====
    // =============================================================

    /**
     * Lấy lịch sử giao dịch của một người dùng cụ thể.
     * @param userId ID của người dùng.
     * @return Danh sách các giao dịch, sắp xếp theo thời gian mới nhất.
     */
    public List<Transaction> getTransactionsByUserId(String userId) {
        return transactionRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * [ADMIN] Lấy tất cả các giao dịch trong hệ thống theo một khoảng thời gian.
     * @param start Thời gian bắt đầu (UTC).
     * @param end Thời gian kết thúc (UTC).
     * @return Danh sách các giao dịch.
     */
    public List<Transaction> getAllTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepo.findAllByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
    }

    public List<Transaction> getTransactionDepositFull(String userId, LocalDateTime start, LocalDateTime end){

        List<Transaction> transactionList =   transactionRepo.findAllByUserIdAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(userId, TransactionType.DEPOSIT, start, end);


        return transactionList;
    }

    public List<Transaction> getTransactionDepositSuccess(LocalDateTime start, LocalDateTime end){

        return  transactionRepo.findAllByTypeAndStatusAndCreatedAtBetweenOrderByCreatedAtDesc(TransactionType.DEPOSIT, TransactionStatus.SUCCESS, start, end);
    }

    public List<Transaction> getTransactionDepositFailed(LocalDateTime start, LocalDateTime end){

        return  transactionRepo.findAllByTypeAndStatusAndCreatedAtBetweenOrderByCreatedAtDesc(TransactionType.DEPOSIT, TransactionStatus.FAILED, start, end);
    }

    public List<Transaction> getTransactionWithdrawSuccess(LocalDateTime start, LocalDateTime end){

        return  transactionRepo.findAllByTypeAndStatusAndCreatedAtBetweenOrderByCreatedAtDesc(TransactionType.WITHDRAW, TransactionStatus.SUCCESS, start, end);
    }


    public List<Transaction> getTransactionWithdrawFullByUserID(String userId, LocalDateTime start, LocalDateTime end){

        return  transactionRepo.findAllByUserIdAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(userId,TransactionType.WITHDRAW,  start, end);
    }

    public List<Transaction> getTransactionWithdrawPending(LocalDateTime start, LocalDateTime end){

        return  transactionRepo.findAllByTypeAndStatusAndCreatedAtBetweenOrderByCreatedAtDesc(TransactionType.WITHDRAW, TransactionStatus.PENDING, start, end);
    }


    /**
     * Lấy lịch sử giao dịch của một người dùng trong một khoảng thời gian.
     * @param userId ID của người dùng.
     * @param start Thời gian bắt đầu (UTC).
     * @param end Thời gian kết thúc (UTC).
     * @return Danh sách các giao dịch.
     */
    public List<Transaction> getUserTransactionsByDateRange(String userId, LocalDateTime start, LocalDateTime end) {
        return transactionRepo.findAllByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end);
    }


    // Trong com.xosotv.live.xosotv_backend.wallet_service.service.WalletService

// ... (các dependency và hàm đã có)


    // Hàm nghiệp vụ rút tiền
    public List<Transaction> getWithdrawalHistory(String userId, LocalDateTime start, LocalDateTime end) {
        return transactionService.findUserTransactionsByTypeAndDateRange(
                userId,
                TransactionType.WITHDRAW,
                start,
                end
        );
    }

    // Hàm nghiệp vụ nạp tiền
    public List<Transaction> getDepositHistory(String userId, LocalDateTime start, LocalDateTime end) {
        return transactionService.findUserTransactionsByTypeAndDateRange(
                userId,
                TransactionType.DEPOSIT,
                start,
                end
        );
    }


    // Hàm nghiệp vụ nạp tiền
    public List<Transaction> getDepositHistoryAll(String userId, LocalDateTime start, LocalDateTime end) {
        return transactionService.findUserTransactionsByTypeAndDateRange(
                userId,
                TransactionType.DEPOSIT,
                start,
                end
        );
    }

//    public void withdrawPending(String userId, BigDecimal amount) {
//    }

    /**
     * ✅ Trừ tiền (đóng băng) khỏi ví cho yêu cầu rút tiền đang chờ xử lý.
     * @param userId ID của người dùng.
     * @param amount Số tiền rút.
     * @throws RuntimeException nếu số dư không đủ.
     */
    @Transactional // Đảm bảo việc kiểm tra và cập nhật là atomic
    public void withdrawPending(String userId, BigDecimal amount) {
        // 1. Lấy Wallet (Đảm bảo có cơ chế Lock nếu chạy đa luồng)
        // Trong MongoDB/Spring Data, cần phải có cơ chế Pessimistic Lock hoặc Optimistic Lock
        // (Ví dụ: dùng Version field) nếu đây là môi trường đa luồng.
        Wallet wallet = getWalletByUser(userId);

        // 2. Kiểm tra số dư (Concurrent check)
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Không đủ số dư để thực hiện giao dịch rút tiền.");
        }

        // 3. Thực hiện trừ tiền
        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepo.save(wallet);

        // 4. Cập nhật số dư trong User (cho mục đích hiển thị nhanh)
        Optional<User> userOption = userRepository.findByEmail(userId);
        userOption.ifPresent(user -> {
            user.setBalance(newBalance);
            userRepository.save(user);
        });
    }
}

