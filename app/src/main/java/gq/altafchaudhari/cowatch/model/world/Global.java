package gq.altafchaudhari.cowatch.model.world;

import com.google.gson.annotations.SerializedName;

public class Global {

    @SerializedName("NewConfirmed")
    private Long newConfirmed;
    @SerializedName("TotalConfirmed")
    private Long totalConfirmed;
    @SerializedName("NewDeaths")
    private Long newDeaths;
    @SerializedName("TotalDeaths")
    private Long totalDeaths;
    @SerializedName("NewRecovered")
    private Long newRecovered;
    @SerializedName("TotalRecovered")
    private Long totalRecovered;

    public Global(Long totalConfirmed,  Long totalRecovered, Long totalDeaths) {
        this.totalConfirmed = totalConfirmed;
        this.totalDeaths = totalDeaths;
        this.totalRecovered = totalRecovered;
    }

    public Long getNewConfirmed() {
        return newConfirmed;
    }

    public void setNewConfirmed(Long newConfirmed) {
        this.newConfirmed = newConfirmed;
    }

    public Long getTotalConfirmed() {
        return totalConfirmed;
    }

    public void setTotalConfirmed(Long totalConfirmed) {
        this.totalConfirmed = totalConfirmed;
    }

    public Long getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(Long newDeaths) {
        this.newDeaths = newDeaths;
    }

    public Long getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(Long totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public Long getNewRecovered() {
        return newRecovered;
    }

    public void setNewRecovered(Long newRecovered) {
        this.newRecovered = newRecovered;
    }

    public Long getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(Long totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

}
