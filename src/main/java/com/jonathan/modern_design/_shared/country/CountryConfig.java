package com.jonathan.modern_design._shared.country;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CountryConfig {

    //Using Stub on Preproduction until we finish beta testing
    @Bean
    public CountriesInventory countriesInventory() {
        return new CountriesInventoryStub();
    }
}
