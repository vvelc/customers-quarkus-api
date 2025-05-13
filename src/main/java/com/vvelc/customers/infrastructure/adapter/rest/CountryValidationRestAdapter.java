package com.vvelc.customers.infrastructure.adapter.rest;

import com.vvelc.customers.application.port.outbound.CountryValidationPort;
import com.vvelc.customers.application.model.CountryInfo;
import com.vvelc.customers.domain.exception.CountryNotFoundException;
import com.vvelc.customers.domain.exception.CountryServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * This class is responsible for validating country information by calling an external REST API.
 * It implements the CountryValidationPort interface.
 */
@ApplicationScoped
public class CountryValidationRestAdapter implements CountryValidationPort {

    @Inject
    @RestClient
    private CountryValidationApiClient countryApiClient;

    @Override
    public CountryInfo findByIsoCode(String isoCode) throws CountryNotFoundException, CountryServiceException {
        try (Response response = countryApiClient.getCountryByCode(isoCode)) {
            JsonArray jsonArray = response.readEntity(JsonArray.class);
            if (jsonArray.isEmpty()) throw new CountryNotFoundException(isoCode);

            JsonObject json = jsonArray.getJsonObject(0);

            String name = json.getJsonObject("name").getString("common", "Unknown");
            String demonym = json
                    .getJsonObject("demonyms")
                    .getJsonObject("eng")
                    .getString("m", "N/A");


            return new CountryInfo(isoCode.toUpperCase(), name, demonym);
        } catch (WebApplicationException e) {
            int status = e.getResponse().getStatus();

            if (status == Response.Status.NOT_FOUND.getStatusCode())
                throw new CountryNotFoundException("Country not found: " + isoCode);

            throw new CountryServiceException("Unexpected response from country API: " + e);
        } catch (CountryNotFoundException e) {
            throw new CountryNotFoundException("Country not found: " + isoCode);
        } catch (Exception e) {
            throw new CountryServiceException("Error calling external country API: " + e);
        }
    }
}
