package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PaySlipMapperTest {

    private PaySlipMapper paySlipMapper;

    @BeforeEach
    public void setUp() {
        paySlipMapper = new PaySlipMapperImpl();
    }
}
