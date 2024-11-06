package ca.gbc.orderservice;

import ca.gbc.orderservice.client.InventoryClient;
import ca.gbc.orderservice.stub.InventoryClientStub;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;


import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

    @ServiceConnection
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:15-apline");

    private Integer port;

    void setUp(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        postgresSQLContainer.start();
    }

    void shouldSubmitOrder() {
        String submitOrderJson = """
                {
                "skuCode" : "samsung_tv_2024"
                "price" : 5000,
                "quantity" : 10
                }                
                """;

        //mock a call to inventory-service
        InventoryClientStub.stubInventoryCall("samsung_tv_2024", 10);

        var responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(submitOrderJson)
                .when()
                .post("/order")
                .then()
                .statusCode(201)
                .extract()
                .body().asString();

        assertThat(responseBodyString, Matchers.is("Order Placed Successfully"));

    }

}
