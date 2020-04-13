
package gq.altafchaudhari.cowatch.model.states;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
@Entity(tableName = "time_series")
public class CasesTimeSeries {

    @SerializedName("dailyconfirmed")
    private String mDailyconfirmed;
    @SerializedName("dailydeceased")
    private String mDailydeceased;
    @SerializedName("dailyrecovered")
    private String mDailyrecovered;
    @NonNull
    @PrimaryKey
    @SerializedName("date")
    private String mDate;
    @SerializedName("totalconfirmed")
    private String mTotalconfirmed;
    @SerializedName("totaldeceased")
    private String mTotaldeceased;
    @SerializedName("totalrecovered")
    private String mTotalrecovered;

    public String getDailyconfirmed() {
        return mDailyconfirmed;
    }

    public void setDailyconfirmed(String dailyconfirmed) {
        mDailyconfirmed = dailyconfirmed;
    }

    public String getDailydeceased() {
        return mDailydeceased;
    }

    public void setDailydeceased(String dailydeceased) {
        mDailydeceased = dailydeceased;
    }

    public String getDailyrecovered() {
        return mDailyrecovered;
    }

    public void setDailyrecovered(String dailyrecovered) {
        mDailyrecovered = dailyrecovered;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTotalconfirmed() {
        return mTotalconfirmed;
    }

    public void setTotalconfirmed(String totalconfirmed) {
        mTotalconfirmed = totalconfirmed;
    }

    public String getTotaldeceased() {
        return mTotaldeceased;
    }

    public void setTotaldeceased(String totaldeceased) {
        mTotaldeceased = totaldeceased;
    }

    public String getTotalrecovered() {
        return mTotalrecovered;
    }

    public void setTotalrecovered(String totalrecovered) {
        mTotalrecovered = totalrecovered;
    }

}
