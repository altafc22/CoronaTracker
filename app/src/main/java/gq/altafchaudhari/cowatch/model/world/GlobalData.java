
package gq.altafchaudhari.cowatch.model.world;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GlobalData {

    @SerializedName("Global")
    private Global global;
    @SerializedName("Countries")
    private List<Country> countries;
    @SerializedName("Date")
    private String date;

    public Global getGlobal() {
        return global;
    }

    public void setGlobal(Global global) {
        this.global = global;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
