package com.app_language.hoctiengtrung_online.referral_service.repository;

import com.app_language.hoctiengtrung_online.referral_service.model.Referral;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReferralRepository extends MongoRepository<Referral, String> {
    Optional<Referral> findByUserId(String userId);
    List<Referral> findByReferrerId(String referrerId);
}



