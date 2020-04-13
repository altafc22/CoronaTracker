
package gq.altafchaudhari.cowatch.model.districts;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DistrictStats {

    @SerializedName("state")
    private String state;
    @SerializedName("districtData")
    private ArrayList<DistrictV2> districtData;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<DistrictV2> getDistrictData() {
        return districtData;
    }

    public void setDistrictData(ArrayList<DistrictV2> districtData) {
        this.districtData = districtData;
    }
}
