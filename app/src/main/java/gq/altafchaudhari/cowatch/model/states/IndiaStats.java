
package gq.altafchaudhari.cowatch.model.states;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class IndiaStats {

    @SerializedName("cases_time_series")
    private List<CasesTimeSeries> mCasesTimeSeries;
    @SerializedName("statewise")
    private List<Statewise> mStatewise;
    @SerializedName("tested")
    private List<Tested> mTested;

    public List<CasesTimeSeries> getCasesTimeSeries() {
        return mCasesTimeSeries;
    }

    public void setCasesTimeSeries(List<CasesTimeSeries> casesTimeSeries) {
        mCasesTimeSeries = casesTimeSeries;
    }

    public List<Statewise> getStatewise() {
        return mStatewise;
    }

    public void setStatewise(List<Statewise> statewise) {
        mStatewise = statewise;
    }

    public List<Tested> getTested() {
        return mTested;
    }

    public void setTested(List<Tested> tested) {
        mTested = tested;
    }

}
