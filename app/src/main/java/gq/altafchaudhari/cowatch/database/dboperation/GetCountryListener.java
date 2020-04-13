package gq.altafchaudhari.cowatch.database.dboperation;

import java.util.List;

import gq.altafchaudhari.cowatch.model.world.Country;

public interface GetCountryListener {
    void onCountriesFound(List<Country> countries);
}
