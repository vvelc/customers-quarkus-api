package com.vvelc.customers.infrastructure.adapter.rest;

import com.vvelc.customers.application.port.outbound.CountryValidationPort;
import com.vvelc.customers.application.model.CountryInfo;
import com.vvelc.customers.domain.exception.CountryNotFoundException;
import com.vvelc.customers.domain.exception.CountryServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@ApplicationScoped
public class CountryValidationRestAdapter implements CountryValidationPort {

    @RestClient
    final CountryValidationApiClient countryApiClient;

    @Inject
    public CountryValidationRestAdapter(CountryValidationApiClient countryApiClient) {
        this.countryApiClient = countryApiClient;
    }

    @Override
    public CountryInfo findByIsoCode(String isoCode) throws CountryNotFoundException, CountryServiceException {
        try {
            Response response = countryApiClient.getCountryByCode(isoCode);

            if (response.getStatus() == 404) {
                throw new CountryNotFoundException(isoCode);
            }

            if (response.getStatus() != 200) {
                throw new CountryServiceException("Unexpected response from country API: " + response.getStatus());
            }

            var jsonArray = response.readEntity(JsonArray.class);
            if (jsonArray.isEmpty()) throw new CountryNotFoundException(isoCode);

            var json = jsonArray.getJsonObject(0);
            String name = json.getJsonObject("name").getString("common");
            String demonym = json.getJsonObject("demonyms")
                    .getJsonObject("eng")
                    .getString("m");

            return new CountryInfo(isoCode.toUpperCase(), name, demonym);
        } catch (CountryNotFoundException e) {
            throw new CountryNotFoundException("Country not found: " + e);
        } catch (Exception e) {
            throw new CountryServiceException("Error calling external country API: " + e);
        }
    }
}
