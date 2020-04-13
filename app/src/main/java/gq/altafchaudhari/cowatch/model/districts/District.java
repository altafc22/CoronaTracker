package gq.altafchaudhari.cowatch.model.districts;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "district_table")
public class District {
    @NonNull
    @PrimaryKey
    @SerializedName("district")
    private String district;
    @SerializedName("delta")
    private String delta_confirmed;
    @SerializedName("confirmed")
    private String confirmed;
    @SerializedName("lastupdatedtime")
    private String last_updated_time;


    public District(@NonNull String district, String delta_confirmed, String confirmed, String last_updated_time) {
        this.district = district;
        this.delta_confirmed = delta_confirmed;
        this.confirmed = confirmed;
        this.last_updated_time = last_updated_time;
    }

    @NonNull
    public String getDistrict() {
        return district;
    }

    public void setDistrict(@NonNull String district) {
        this.district = district;
    }

    public String getDelta_confirmed() {
        return delta_confirmed;
    }

    public void setDelta_confirmed(String delta_confirmed) {
        this.delta_confirmed = delta_confirmed;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getLast_updated_time() {
        return last_updated_time;
    }

    public void setLast_updated_time(String last_updated_time) {
        this.last_updated_time = last_updated_time;
    }
}
