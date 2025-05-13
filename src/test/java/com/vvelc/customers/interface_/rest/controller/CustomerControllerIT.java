package com.vvelc.customers.interface_.rest.controller;

import com.vvelc.customers.domain.model.Customer;
import com.vvelc.customers.domain.repository.CustomerRepository;
import com.vvelc.customers.interface_.rest.dto.CustomerCreateRequest;
import com.vvelc.customers.interface_.rest.dto.CustomerUpdateRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(CustomerController.class)
class CustomerControllerIT {

    @Inject
    CustomerRepository repository;

    Long c1Id;
    Long c2Id;

    @BeforeEach
    @Transactional
    void setup() {
        Customer c1 = new Customer(null, "John", null, "Doe", null, "john@test.com", "Address1", "123", "US", "American");
        Customer c2 = new Customer(null, "Jane", null, "Smith", null, "jane@test.com", "Address2", "456", "US", "American");

        c1Id = repository.save(c1).getId();
        c2Id = repository.save(c2).getId();
    }

    @AfterEach
    @Transactional
    void clean() {
        repository.deleteById(c1Id);
        repository.deleteById(c2Id);
    }

    @Test
    void should_create_customer_successfully() {
        CustomerCreateRequest request = new CustomerCreateRequest("Mark", "", "Wayne", "", "mark@test.com", "Street 1", "8091231234", "US");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("firstName", equalTo("Mark"))
                .body("email", equalTo("mark@test.com"))
                .body("demonym", equalTo("American"))
                .header("Location", containsString("/customers/"));
    }

    @Test
    void should_fail_to_create_customer_with_invalid_email() {
        CustomerCreateRequest request = new CustomerCreateRequest("Mark", "", "Wayne", "", "invalid-email", "Street 1", "8091231234", "USI");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("messages", hasItem("Email must be a valid email address"));
    }

    @Test
    void should_fail_to_create_customer_with_invalid_country_code() {
        CustomerCreateRequest request = new CustomerCreateRequest("Mark", "", "Wayne", "", "mark@test.com", "Street 1", "8091231234", "us");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("messages", hasItem("Country must be in ISO 3166-1 alpha-2 format (uppercase letters)"));
    }

    @Test
    void should_fail_to_create_customer_with_nonexistent_country() {
        CustomerCreateRequest request = new CustomerCreateRequest("Mark", "", "Wayne", "", "mark@test.com", "Street 1", "8091231234", "XX");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("message", equalTo("Country not found: XX"));
    }

    @Test
    void should_fail_to_create_customer_with_duplicate_email() {
        CustomerCreateRequest request = new CustomerCreateRequest("Dup", "", "Mail", "", "john@test.com", "Street 2", "999", "US");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    void should_get_customer_by_id() {
        given()
                .when()
                .get("/{id}", c1Id)
                .then()
                .statusCode(200)
                .body("id", equalTo(c1Id.intValue()));
    }

    @Test
    void should_return_404_when_customer_not_found() {
        given()
                .when()
                .get("/{id}", 9999)
                .then()
                .statusCode(404);
    }

    @Test
    void should_get_all_customers() {
        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("items", hasSize(greaterThanOrEqualTo(2)));
    }

    @Test
    void should_get_customers_by_country() {
        given()
                .queryParam("country", "US")
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("items", everyItem(hasEntry("country", "US")));
    }

    @Test
    void should_get_customers_with_pagination() {
        given()
                .queryParam("page", 0)
                .queryParam("size", 1)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("items", hasSize(1))
                .body("page", equalTo(0))
                .body("size", equalTo(1))
                .body("total", greaterThanOrEqualTo(2));

    }

    @Test
    void should_fail_with_invalid_country_query_param() {
        given()
                .queryParam("country", "usa")
                .when()
                .get()
                .then()
                .statusCode(400);
    }

    @Test
    void should_update_customer_successfully() {
        CustomerUpdateRequest update = new CustomerUpdateRequest("updated@test.com", "Updated Address", "987", "US");

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .when()
                .put("/{id}", c1Id)
                .then()
                .statusCode(200)
                .body("email", equalTo("updated@test.com"));
    }

    @Test
    void should_return_404_when_updating_nonexistent_customer() {
        CustomerUpdateRequest update = new CustomerUpdateRequest("x@test.com", "Addr", "321", "US");

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .when()
                .put("/{id}", 9999)
                .then()
                .statusCode(404);
    }

    @Test
    void should_return_400_when_updating_with_duplicate_email() {
        CustomerUpdateRequest update = new CustomerUpdateRequest("jane@test.com", null, null, "US");

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .when()
                .put("/{id}", c1Id)
                .then()
                .statusCode(409);
    }

    @Test
    void should_return_400_when_updating_with_invalid_country_code() {
        CustomerUpdateRequest update = new CustomerUpdateRequest("jane@test.com", null, null, "USA");

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .when()
                .put("/{id}", c1Id)
                .then()
                .statusCode(400)
                .body("messages", hasItem("Country must be in ISO 3166-1 alpha-2 format (uppercase letters)"));
    }

    @Test
    void should_return_400_when_updating_with_nonexistent_country() {
        CustomerUpdateRequest update = new CustomerUpdateRequest("jane@test.com", null, null, "XX");

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .when()
                .put("/{id}", c1Id)
                .then()
                .statusCode(400)
                .body("message", equalTo("Country not found: XX"));
    }

    @Test
    void should_delete_customer_successfully() {
        given()
                .when()
                .delete("/{id}", c1Id)
                .then()
                .statusCode(204)
                .body(is(emptyOrNullString()));
    }

    @Test
    void should_return_404_when_deleting_nonexistent_customer() {
        given()
                .when()
                .delete("/{id}", 9999)
                .then()
                .statusCode(404);
    }
}
