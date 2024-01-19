package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnterpriseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Enterprise getEnterpriseSample1() {
        return new Enterprise()
            .id(1L)
            .companyName("companyName1")
            .businessRegisterNumber("businessRegisterNumber1")
            .uniqueIdentificationNumber("uniqueIdentificationNumber1")
            .businessDomicile("businessDomicile1")
            .email("email1")
            .businessPhone("businessPhone1")
            .city("city1");
    }

    public static Enterprise getEnterpriseSample2() {
        return new Enterprise()
            .id(2L)
            .companyName("companyName2")
            .businessRegisterNumber("businessRegisterNumber2")
            .uniqueIdentificationNumber("uniqueIdentificationNumber2")
            .businessDomicile("businessDomicile2")
            .email("email2")
            .businessPhone("businessPhone2")
            .city("city2");
    }

    public static Enterprise getEnterpriseRandomSampleGenerator() {
        return new Enterprise()
            .id(longCount.incrementAndGet())
            .companyName(UUID.randomUUID().toString())
            .businessRegisterNumber(UUID.randomUUID().toString())
            .uniqueIdentificationNumber(UUID.randomUUID().toString())
            .businessDomicile(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .businessPhone(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString());
    }
}
