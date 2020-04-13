package gq.altafchaudhari.cowatch.database.dboperation;

import java.util.List;

import gq.altafchaudhari.cowatch.model.districts.District;

public interface GetDistrictListener {
    void onDistrictsFound(List<District> districtList);
}
