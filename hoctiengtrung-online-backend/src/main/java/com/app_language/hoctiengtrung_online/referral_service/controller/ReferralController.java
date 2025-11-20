package com.app_language.hoctiengtrung_online.referral_service.controller;


import com.app_language.hoctiengtrung_online.auth.model.User;
import com.app_language.hoctiengtrung_online.auth.repository.UserRepository;
import com.app_language.hoctiengtrung_online.referral_service.dto.InvitedUserDTO;
import com.app_language.hoctiengtrung_online.referral_service.dto.ReferralSummaryResponse;
import com.app_language.hoctiengtrung_online.referral_service.model.Referral;
import com.app_language.hoctiengtrung_online.referral_service.repository.ReferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/referral")
public class ReferralController {

    @Autowired
    private ReferralRepository referralRepository;

    @GetMapping("/summary")
    public ReferralSummaryResponse getReferralSummary(@RequestParam String userId) {
        List<Referral> referrals = referralRepository.findByReferrerId(userId);

        BigDecimal total = referrals.stream()
                .map(r -> r.getTotalCommission() != null ? r.getTotalCommission() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long invitedCount = referrals.size();

        return new ReferralSummaryResponse(total, invitedCount);
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/invited")
    public List<InvitedUserDTO> getInvitedUsers(@RequestParam String refCode) {
        List<User> invited = userRepository.findByReferrerCode(refCode);

        return invited.stream()
                .map(user -> new InvitedUserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }
}

