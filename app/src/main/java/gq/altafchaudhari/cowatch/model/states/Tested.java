
package gq.altafchaudhari.cowatch.model.states;

import com.google.gson.annotations.SerializedName;

public class Tested {

    @SerializedName("source")
    private String mSource;
    @SerializedName("testsconductedbyprivatelabs")
    private String mTestsconductedbyprivatelabs;
    @SerializedName("totalindividualstested")
    private String mTotalindividualstested;
    @SerializedName("totalpositivecases")
    private String mTotalpositivecases;
    @SerializedName("totalsamplestested")
    private String mTotalsamplestested;
    @SerializedName("updatetimestamp")
    private String mUpdatetimestamp;
    @SerializedName("_ckd7g")
    private String m_ckd7g;

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public String getTestsconductedbyprivatelabs() {
        return mTestsconductedbyprivatelabs;
    }

    public void setTestsconductedbyprivatelabs(String testsconductedbyprivatelabs) {
        mTestsconductedbyprivatelabs = testsconductedbyprivatelabs;
    }

    public String getTotalindividualstested() {
        return mTotalindividualstested;
    }

    public void setTotalindividualstested(String totalindividualstested) {
        mTotalindividualstested = totalindividualstested;
    }

    public String getTotalpositivecases() {
        return mTotalpositivecases;
    }

    public void setTotalpositivecases(String totalpositivecases) {
        mTotalpositivecases = totalpositivecases;
    }

    public String getTotalsamplestested() {
        return mTotalsamplestested;
    }

    public void setTotalsamplestested(String totalsamplestested) {
        mTotalsamplestested = totalsamplestested;
    }

    public String getUpdatetimestamp() {
        return mUpdatetimestamp;
    }

    public void setUpdatetimestamp(String updatetimestamp) {
        mUpdatetimestamp = updatetimestamp;
    }

    public String get_ckd7g() {
        return m_ckd7g;
    }

    public void set_ckd7g(String _ckd7g) {
        m_ckd7g = _ckd7g;
    }

}
