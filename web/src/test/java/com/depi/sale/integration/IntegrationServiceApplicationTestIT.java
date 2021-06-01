package com.depi.sale.integration;

import com.depi.sale.SaleApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test"})
@SpringBootTest(classes = {SaleApplication.class,}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationServiceApplicationTestIT {

    @Test
    void contextLoads() {
    }
}
