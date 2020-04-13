
package gq.altafchaudhari.cowatch.model.states;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "state_table")
public class Statewise {

    @SerializedName("active")
    private String mActive;
    @SerializedName("confirmed")
    private String mConfirmed;
    @SerializedName("deaths")
    private String mDeaths;
    @SerializedName("deltaconfirmed")
    private String mDeltaconfirmed;
    @SerializedName("deltadeaths")
    private String mDeltadeaths;
    @SerializedName("deltarecovered")
    private String mDeltarecovered;
    @SerializedName("lastupdatedtime")
    private String mLastupdatedtime;
    @SerializedName("recovered")
    private String mRecovered;

    @NonNull
    @PrimaryKey
    @SerializedName("state")
    private String mState;
    @SerializedName("statecode")
    private String mStatecode;

    public String getActive() {
        return mActive;
    }

    public void setActive(String active) {
        mActive = active;
    }

    public String getConfirmed() {
        return mConfirmed;
    }

    public void setConfirmed(String confirmed) {
        mConfirmed = confirmed;
    }

    public String getDeaths() {
        return mDeaths;
    }

    public void setDeaths(String deaths) {
        mDeaths = deaths;
    }

    public String getDeltaconfirmed() {
        return mDeltaconfirmed;
    }

    public void setDeltaconfirmed(String deltaconfirmed) {
        mDeltaconfirmed = deltaconfirmed;
    }

    public String getDeltadeaths() {
        return mDeltadeaths;
    }

    public void setDeltadeaths(String deltadeaths) {
        mDeltadeaths = deltadeaths;
    }

    public String getDeltarecovered() {
        return mDeltarecovered;
    }

    public void setDeltarecovered(String deltarecovered) {
        mDeltarecovered = deltarecovered;
    }

    public String getLastupdatedtime() {
        return mLastupdatedtime;
    }

    public void setLastupdatedtime(String lastupdatedtime) {
        mLastupdatedtime = lastupdatedtime;
    }

    public String getRecovered() {
        return mRecovered;
    }

    public void setRecovered(String recovered) {
        mRecovered = recovered;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getStatecode() {
        return mStatecode;
    }

    public void setStatecode(String statecode) {
        mStatecode = statecode;
    }

}
