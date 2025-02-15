package com.jonathan.modern_design._shared.country;

import java.util.List;

class TheCompaniesAPIWrapper {
    record Continent(
            String code,
            String latitude,
            String longitude,
            String name,
            String nameEs,
            String nameFr
    ) {
    }

    record Country(
            String code,
            Continent continent,
            String latitude,
            String longitude,
            String name,
            String nameEs,
            String nameFr,
            String nameNative,
            int population
    ) {
    }

    record Meta(
            int currentPage,
            int firstPage,
            int lastPage,
            int perPage,
            int total
    ) {
    }

    record TheCompaniesAPIResponse(
            List<Country> results,
            Meta meta
    ) {
    }
}
