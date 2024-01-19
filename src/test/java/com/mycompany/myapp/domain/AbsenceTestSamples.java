package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AbsenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Absence getAbsenceSample1() {
        return new Absence().id(1L).numberDayAbsence(1L).congeRestant(1L);
    }

    public static Absence getAbsenceSample2() {
        return new Absence().id(2L).numberDayAbsence(2L).congeRestant(2L);
    }

    public static Absence getAbsenceRandomSampleGenerator() {
        return new Absence()
            .id(longCount.incrementAndGet())
            .numberDayAbsence(longCount.incrementAndGet())
            .congeRestant(longCount.incrementAndGet());
    }
}
