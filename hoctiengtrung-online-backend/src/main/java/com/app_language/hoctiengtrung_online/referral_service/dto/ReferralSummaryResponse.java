package com.app_language.hoctiengtrung_online.referral_service.dto;

import java.math.BigDecimal;

public class ReferralSummaryResponse {
    private BigDecimal totalCommission;
    private long invitedCount;

    public ReferralSummaryResponse(BigDecimal total, long count) {
        this.totalCommission = total;
        this.invitedCount = count;
    }

    // getters


    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    public long getInvitedCount() {
        return invitedCount;
    }

    public void setInvitedCount(long invitedCount) {
        this.invitedCount = invitedCount;
    }
}

