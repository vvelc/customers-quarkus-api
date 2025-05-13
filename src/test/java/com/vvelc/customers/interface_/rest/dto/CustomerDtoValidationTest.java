package com.vvelc.customers.interface_.rest.dto;

import com.vvelc.customers.interface_.rest.controller.query.CustomerQueryParams;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    class CustomerCreateRequestValidation {

        @Test
        void should_pass_when_all_fields_are_valid() {
            var req = new CustomerCreateRequest("John", "A", "Doe", "B", "john@test.com", "Street 123", "8091231234", "US");
            Set<ConstraintViolation<CustomerCreateRequest>> violations = validator.validate(req);
            assertThat(violations).isEmpty();
        }

        @Test
        void should_fail_when_mandatory_fields_are_blank() {
            var req = new CustomerCreateRequest(" ", null, " ", null, "invalid", " ", "", "us");
            Set<ConstraintViolation<CustomerCreateRequest>> violations = validator.validate(req);
            assertThat(violations).extracting(ConstraintViolation::getPropertyPath).map(Object::toString).contains(
                    "firstName", "firstLastName", "email", "address", "phone", "country"
            );
        }

        @Test
        void should_fail_with_invalid_email() {
            var req = new CustomerCreateRequest("John", null, "Doe", null, "not-an-email", "Street", "123", "US");
            Set<ConstraintViolation<CustomerCreateRequest>> violations = validator.validate(req);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        }

        @Test
        void should_fail_with_too_long_email() {
            String longEmail = "a".repeat(101) + "@test.com";
            var req = new CustomerCreateRequest("John", null, "Doe", null, longEmail, "Street", "123", "US");
            Set<ConstraintViolation<CustomerCreateRequest>> violations = validator.validate(req);
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        }

        @Test
        void should_fail_with_too_long_address_and_phone() {
            String longAddress = "a".repeat(256);
            String longPhone = "1".repeat(21);
            var req = new CustomerCreateRequest("John", null, "Doe", null, "john@test.com", longAddress, longPhone, "US");
            Set<ConstraintViolation<CustomerCreateRequest>> violations = validator.validate(req);
            assertThat(violations).extracting(v -> v.getPropertyPath().toString()).contains("address", "phone");
        }

        @Test
        void should_fail_with_invalid_country_format() {
            var req = new CustomerCreateRequest("John", null, "Doe", null, "john@test.com", "Street", "123", "usa");
            Set<ConstraintViolation<CustomerCreateRequest>> violations = validator.validate(req);
            assertThat(violations).extracting(v -> v.getPropertyPath().toString()).contains("country");
        }
    }

    @Nested
    class CustomerUpdateRequestValidation {

        @Test
        void should_pass_when_all_fields_are_valid() {
            var req = new CustomerUpdateRequest("john@test.com", "Street 1", "123", "US");
            Set<ConstraintViolation<CustomerUpdateRequest>> violations = validator.validate(req);
            assertThat(violations).isEmpty();
        }

        @Test
        void should_pass_when_all_fields_are_null() {
            var req = new CustomerUpdateRequest(null, null, null, null);
            Set<ConstraintViolation<CustomerUpdateRequest>> violations = validator.validate(req);
            assertThat(violations).isEmpty();
        }

        @Test
        void should_fail_when_email_or_country_invalid() {
            var req = new CustomerUpdateRequest("not-email", null, null, "us");
            Set<ConstraintViolation<CustomerUpdateRequest>> violations = validator.validate(req);
            assertThat(violations).extracting(v -> v.getPropertyPath().toString()).contains("email", "country");
        }

        @Test
        void should_fail_when_phone_too_long() {
            var req = new CustomerUpdateRequest(null, null, "1".repeat(21), "US");
            Set<ConstraintViolation<CustomerUpdateRequest>> violations = validator.validate(req);
            assertThat(violations).extracting(v -> v.getPropertyPath().toString()).contains("phone");
        }
    }

    @Nested
    class CustomerQueryParamsValidation {

        @Test
        void should_pass_with_valid_values() {
            var query = new CustomerQueryParams("US", 0, 10);
            Set<ConstraintViolation<CustomerQueryParams>> violations = validator.validate(query);
            assertThat(violations).isEmpty();
        }

        @Test
        void should_fail_with_invalid_country_and_page_and_size() {
            var query = new CustomerQueryParams("usa", -1, 101);
            Set<ConstraintViolation<CustomerQueryParams>> violations = validator.validate(query);
            assertThat(violations).extracting(v -> v.getPropertyPath().toString()).contains("country", "page", "size");
        }
    }
}
