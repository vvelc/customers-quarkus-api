package com.vvelc.customers.application.port.outbound;

import com.vvelc.customers.application.model.CountryInfo;
import com.vvelc.customers.domain.exception.CountryNotFoundException;
import com.vvelc.customers.domain.exception.CountryServiceException;

public interface CountryValidationPort {
    CountryInfo findByIsoCode(String isoCode) throws CountryNotFoundException, CountryServiceException;
}
