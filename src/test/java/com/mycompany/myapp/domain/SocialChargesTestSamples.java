package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SocialChargesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SocialCharges getSocialChargesSample1() {
        return new SocialCharges().id(1L).amount(1L).purchaseManager("purchaseManager1");
    }

    public static SocialCharges getSocialChargesSample2() {
        return new SocialCharges().id(2L).amount(2L).purchaseManager("purchaseManager2");
    }

    public static SocialCharges getSocialChargesRandomSampleGenerator() {
        return new SocialCharges()
            .id(longCount.incrementAndGet())
            .amount(longCount.incrementAndGet())
            .purchaseManager(UUID.randomUUID().toString());
    }
}
