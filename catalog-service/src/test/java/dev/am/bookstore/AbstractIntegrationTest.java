package dev.am.bookstore;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class AbstractIntegrationTest {
    @Autowired
    WebApplicationContext webApplicationContext;

    protected RestTestClient restTestClient;

    @BeforeEach
    public void setup() {
        restTestClient =
                RestTestClient.bindToApplicationContext(webApplicationContext).build();
    }
}
