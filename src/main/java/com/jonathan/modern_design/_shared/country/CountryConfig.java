package com.jonathan.modern_design._shared.country;

import com.jonathan.modern_design._internal.config.annotations.Stub;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackageClasses = {CountryConfig.class},
        includeFilters = {
                //Using Stub on Preproduction until we finish beta testing
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})
        }
        //excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {CountriesInventoryStub.class})
)
class CountryConfig {
}
