package com.app_language.hoctiengtrung_online.wallet.repository;


import com.app_language.hoctiengtrung_online.wallet.model.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WalletRepository extends MongoRepository<Wallet, String> {
    Optional<Wallet> findByUserId(String userId);
}
