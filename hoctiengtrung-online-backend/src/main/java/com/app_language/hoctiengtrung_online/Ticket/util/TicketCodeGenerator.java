package com.app_language.hoctiengtrung_online.Ticket.util;



import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TicketCodeGenerator {

    private static final String PREFIX = "TICK";
    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    public String generate() {
        // Format: TICK{timestamp}{counter}{random}
        long timestamp = System.currentTimeMillis();
        long count = counter.incrementAndGet();
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);

        return String.format("%s%d%d%04d", PREFIX, timestamp, count % 10000, random);
    }
}