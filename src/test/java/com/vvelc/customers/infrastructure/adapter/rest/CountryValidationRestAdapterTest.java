package com.vvelc.customers.infrastructure.adapter.rest;

import com.vvelc.customers.application.model.CountryInfo;
import com.vvelc.customers.domain.exception.CountryNotFoundException;
import com.vvelc.customers.domain.exception.CountryServiceException;
import jakarta.json.*;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryValidationRestAdapterTest {

    @Mock
    private CountryValidationApiClient apiClient;

    @InjectMocks
    private CountryValidationRestAdapter adapter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_return_country_info_successfully() {
        JsonObject name = Json.createObjectBuilder().add("common", "United States").build();
        JsonObject demonyms = Json.createObjectBuilder()
                .add("eng", Json.createObjectBuilder().add("m", "American")).build();
        JsonObject countryJson = Json.createObjectBuilder()
                .add("name", name)
                .add("demonyms", demonyms)
                .build();
        JsonArray jsonArray = Json.createArrayBuilder().add(countryJson).build();

        Response response = mock(Response.class);
        when(response.readEntity(JsonArray.class)).thenReturn(jsonArray);
        when(apiClient.getCountryByCode("US")).thenReturn(response);

        CountryInfo result = adapter.findByIsoCode("US");

        assertThat(result.isoCode()).isEqualTo("US");
        assertThat(result.name()).isEqualTo("United States");
        assertThat(result.demonym()).isEqualTo("American");
    }

    @Test
    void should_throw_when_country_not_found_in_empty_array() {
        JsonArray emptyArray = Json.createArrayBuilder().build();

        Response response = mock(Response.class);
        when(response.readEntity(JsonArray.class)).thenReturn(emptyArray);
        when(apiClient.getCountryByCode("XX")).thenReturn(response);

        assertThatThrownBy(() -> adapter.findByIsoCode("XX"))
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessageContaining("XX");
    }

    @Test
    void should_throw_when_api_returns_404() {
        WebApplicationException exception = new WebApplicationException(
                "Not Found",
                Response.status(Response.Status.NOT_FOUND).build()
        );

        when(apiClient.getCountryByCode("YY")).thenThrow(exception);

        assertThatThrownBy(() -> adapter.findByIsoCode("YY"))
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessageContaining("Country not found");
    }

    @Test
    void should_throw_when_api_returns_unexpected_status() {
        Response mockResponse = mock(Response.class);
        when(mockResponse.getStatus()).thenReturn(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        WebApplicationException exception = new WebApplicationException("Server error", mockResponse);
        when(apiClient.getCountryByCode("DE")).thenThrow(exception);

        assertThatThrownBy(() -> adapter.findByIsoCode("DE"))
                .isInstanceOf(CountryServiceException.class)
                .hasMessageContaining("Unexpected response");
    }

    @Test
    void should_throw_when_generic_exception_occurs() {
        when(apiClient.getCountryByCode("BR")).thenThrow(new RuntimeException("Timeout"));

        assertThatThrownBy(() -> adapter.findByIsoCode("BR"))
                .isInstanceOf(CountryServiceException.class)
                .hasMessageContaining("Timeout");
    }
}
