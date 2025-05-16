package com.example;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.get("/api/users/reset");
    }

    private void printTestResult(String testName, int statusCode, String validation,
                            long manualDuration, long restAssuredDuration, 
                            String requestBody, String responseBody) {
        String currentDateTime = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        
        String logFileName = "test_results_log.txt";
        
        // Başarı durumunu belirle (her ikisi de 1000ms altında olmalı)
        boolean isSuccess = manualDuration < 1000 && restAssuredDuration < 1000;
        String resultStatus = isSuccess ? "Başarılı" : "Başarısız";
        
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("\n").append("=".repeat(60));
        logEntry.append("\nTEST TARİHİ: ").append(currentDateTime);
        logEntry.append("\nTEST: ").append(testName);
        logEntry.append("\n").append("=".repeat(60));

        if (requestBody != null) {
            logEntry.append("\nREQUEST BODY:");
            logEntry.append("\n").append(requestBody);
            logEntry.append("\n").append("-".repeat(60));
        }

        logEntry.append("\nTEST SONUÇLARI:");
        logEntry.append("\n").append("-".repeat(60));
        logEntry.append("\nSTATUS: Status Code: ").append(statusCode).append(" (OK)");
        logEntry.append("\nVALIDATION: ").append(validation);
        logEntry.append("\nTIME: ");
        logEntry.append("\n  - Manuel Ölçüm: ").append(manualDuration).append("ms");
        logEntry.append("\n  - RestAssured Ölçümü: ").append(restAssuredDuration).append("ms");
        logEntry.append("\n  - Beklenen: <1000ms");
        logEntry.append("\n  - Sonuç: (").append(resultStatus).append(")");
        logEntry.append("\n\nRESPONSE BODY:");
        logEntry.append("\n").append(responseBody);
        logEntry.append("\n").append("=".repeat(60)).append("\n");

        try {
            java.nio.file.Files.write(
                java.nio.file.Paths.get(logFileName),
                logEntry.toString().getBytes(),
                java.nio.file.StandardOpenOption.CREATE,
                java.nio.file.StandardOpenOption.APPEND
            );
            
            System.out.println(logEntry.toString());
            System.out.println("Test sonucu '" + logFileName + "' dosyasına kaydedildi.");

        } catch (java.io.IOException e) {
            System.err.println("Log dosyasına yazma hatası: " + e.getMessage());
            System.out.println(logEntry.toString());
        }
    }

    @Test
    @Order(1)
    void testGetAllUsers() {
        long startTime = System.currentTimeMillis();

        Response response = RestAssured.given()
                .when()
                    .get("/api/users")
                .then()
                    .statusCode(200)
                    .body("size()", equalTo(20))
                    .time(lessThan(1000L))
                    .extract().response();

        long endTime = System.currentTimeMillis();
        long manualDuration = endTime - startTime;
        long restAssuredDuration = response.time();

        printTestResult(
                "TEST 1: Tüm Kullanıcıları Getirme (GET /api/users)",
                200,
                "Response Body: 20 kullanıcı doğrulandı",
                manualDuration,
                restAssuredDuration,
                null,
                response.body().asPrettyString()
        );

        assertTrue(manualDuration < 1000, "Response süresi 1 saniyeden uzun: " + manualDuration + "ms");
    }

    @Test
    @Order(2)
    void testAddUser() {
        String userJson = """
        {
            "name": "Yeni Kullanıcı",
            "email": "yeni@example.com"
        }
        """;

        long startTime = System.currentTimeMillis();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                    .post("/api/users")
                .then()
                    .statusCode(200)
                    .body("name", equalTo("Yeni Kullanıcı"))
                    .time(lessThan(1000L))
                    .extract().response();

        long endTime = System.currentTimeMillis();
        long manualDuration = endTime - startTime;
        long restAssuredDuration = response.time();

        printTestResult(
                "TEST 2: Yeni Kullanıcı Ekleme (POST /api/users)",
                200,
                "Response Body: 'name' = Yeni Kullanıcı doğrulandı",
                manualDuration,
                restAssuredDuration,
                userJson,
                response.body().asPrettyString()
        );

        assertTrue(manualDuration < 1000, "Response süresi 1 saniyeden uzun: " + manualDuration + "ms");
    }

    @Test
    @Order(3)
    void testUpdateUser() {
        String updateJson = """
        {
            "name": "Güncel Ad",
            "email": "guncel@example.com"
        }
        """;

        long startTime = System.currentTimeMillis();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updateJson)
                .when()
                    .put("/api/users/1")
                .then()
                    .statusCode(200)
                    .body("name", equalTo("Güncel Ad"))
                    .time(lessThan(1000L))
                    .extract().response();

        long endTime = System.currentTimeMillis();
        long manualDuration = endTime - startTime;
        long restAssuredDuration = response.time();

        printTestResult(
                "TEST 3: Kullanıcı Güncelleme (PUT /api/users/1)",
                200,
                "Response Body: 'name' = Güncel Ad doğrulandı",
                manualDuration,
                restAssuredDuration,
                updateJson,
                response.body().asPrettyString()
        );

        assertTrue(manualDuration < 1000, "Response süresi 1 saniyeden uzun: " + manualDuration + "ms");
    }

    @Test
    @Order(4)
    void testDeleteUser() {
        long startTime = System.currentTimeMillis();

        Response response = RestAssured.given()
                .when()
                    .delete("/api/users/1")
                .then()
                    .statusCode(200)
                    .body(equalTo("Kullanıcı silindi: 1"))
                    .time(lessThan(1000L))
                    .extract().response();

        long endTime = System.currentTimeMillis();
        long manualDuration = endTime - startTime;
        long restAssuredDuration = response.time();

        printTestResult(
                "TEST 4: Kullanıcı Silme (DELETE /api/users/1)",
                200,
                "Response Body: Silme mesajı doğrulandı",
                manualDuration,
                restAssuredDuration,
                null,
                response.body().asString()
        );

        assertTrue(manualDuration < 1000, "Response süresi 1 saniyeden uzun: " + manualDuration + "ms");
    }
}