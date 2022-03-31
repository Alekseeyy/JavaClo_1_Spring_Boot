package ru.netology.spring_boot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    private static GenericContainer<?> devapp = new GenericContainer<>("devapp:latest")
            .withExposedPorts(8080);
    private static GenericContainer<?> prodapp = new GenericContainer<>("prodapp:latest")
            .withExposedPorts(8081);

    @BeforeAll
    public static void setUp() {
        devapp.start();
        prodapp.start();
    }

    @Test
    void contextLoadsDevapp() {
        final String expected = "Current profile is dev";
        String url = String.format("http://localhost:%d/profile", devapp.getMappedPort(8080));
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(expected, forEntity.getBody());
    }

    @Test
    void contextLoadsProdapp() {
        final String expected = "Current profile is production";
        String url = String.format("http://localhost:%d/profile", prodapp.getMappedPort(8081));
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(expected, forEntity.getBody());
    }
}
