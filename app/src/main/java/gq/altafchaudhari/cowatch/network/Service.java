package gq.altafchaudhari.cowatch.network;

import java.util.List;

import gq.altafchaudhari.cowatch.model.districts.DistrictStats;
import gq.altafchaudhari.cowatch.model.states.IndiaStats;
import gq.altafchaudhari.cowatch.model.world.GlobalData;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET("/summary")
    Call<GlobalData> getCountryWiseStats();
    
    @GET("/data.json")
    Call<IndiaStats> getStateWiseStats();

    @GET("v2/state_district_wise.json")
    Call<List<DistrictStats>> getDistrictWiseStats();

  /*  @GET()
    Call<StatewiseStat> getStateWiseStats(@Url String url);

    @GET()
    Call<StatewiseStat> getDistrictWiseStats(@Url String url);*/

}