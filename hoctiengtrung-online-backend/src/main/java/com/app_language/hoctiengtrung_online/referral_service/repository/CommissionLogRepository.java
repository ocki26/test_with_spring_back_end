package com.app_language.hoctiengtrung_online.referral_service.repository;


import com.app_language.hoctiengtrung_online.referral_service.model.CommissionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommissionLogRepository extends MongoRepository<CommissionLog, String> {}

