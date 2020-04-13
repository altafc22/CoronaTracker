package gq.altafchaudhari.cowatch.model.districts;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "district_table")
public class DistrictV2 {
    @NonNull
    @PrimaryKey
    @SerializedName("district")
    private String district;
    @SerializedName("delta")
    private List<Delta> delta;
    @SerializedName("confirmed")
    private String confirmed;
    @SerializedName("lastupdatedtime")
    private String lastUpdatedTime;

    @NonNull
    public String getDistrict() {
        return district;
    }

    public void setDistrict(@NonNull String district) {
        this.district = district;
    }

    public List<Delta> getDelta() {
        return delta;
    }

    public void setDelta(List<Delta> delta) {
        this.delta = delta;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
