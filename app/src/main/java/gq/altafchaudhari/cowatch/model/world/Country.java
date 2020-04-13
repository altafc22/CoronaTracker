
package gq.altafchaudhari.cowatch.model.world;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Entity(tableName = "country_table")
//public class Country  implements Parcelable {
public class Country implements Serializable {

    @NonNull
    @PrimaryKey
    @SerializedName("Country")
    private String mCountry;
    @SerializedName("NewConfirmed")
    private Long mNewConfirmed;
    @SerializedName("NewDeaths")
    private Long mNewDeaths;
    @SerializedName("NewRecovered")
    private Long mNewRecovered;
    @SerializedName("Slug")
    private String mSlug;
    @SerializedName("TotalConfirmed")
    private Long mTotalConfirmed;
    @SerializedName("TotalDeaths")
    private Long mTotalDeaths;
    @SerializedName("TotalRecovered")
    private Long mTotalRecovered;

    public Country(@NonNull String mCountry, Long mNewConfirmed, Long mTotalConfirmed, Long mTotalDeaths, Long mTotalRecovered) {
        this.mCountry = mCountry;
        this.mNewConfirmed = mNewConfirmed;
        this.mTotalConfirmed = mTotalConfirmed;
        this.mTotalDeaths = mTotalDeaths;
        this.mTotalRecovered = mTotalRecovered;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public Long getNewConfirmed() {
        return mNewConfirmed;
    }

    public void setNewConfirmed(Long newConfirmed) {
        mNewConfirmed = newConfirmed;
    }

    public Long getNewDeaths() {
        return mNewDeaths;
    }

    public void setNewDeaths(Long newDeaths) {
        mNewDeaths = newDeaths;
    }

    public Long getNewRecovered() {
        return mNewRecovered;
    }

    public void setNewRecovered(Long newRecovered) {
        mNewRecovered = newRecovered;
    }

    public String getSlug() {
        return mSlug;
    }

    public void setSlug(String slug) {
        mSlug = slug;
    }

    public Long getTotalConfirmed() {
        return mTotalConfirmed;
    }

    public void setTotalConfirmed(Long totalConfirmed) {
        mTotalConfirmed = totalConfirmed;
    }

    public Long getTotalDeaths() {
        return mTotalDeaths;
    }

    public void setTotalDeaths(Long totalDeaths) {
        mTotalDeaths = totalDeaths;
    }

    public Long getTotalRecovered() {
        return mTotalRecovered;
    }

    public void setTotalRecovered(Long totalRecovered) {
        mTotalRecovered = totalRecovered;
    }



/*    protected Country(Parcel parcel) {
        mCountry = parcel.readString();
        mNewConfirmed = parcel.readLong();
        mTotalConfirmed =  parcel.readLong();
        mTotalDeaths =  parcel.readLong();
        mTotalRecovered =  parcel.readLong();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(Long size) {
            return new Country[size];
        }
    };

    @Override
    public Long describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, Long flags) {
        dest.writeString(mCountry);
        dest.writeLong(mNewConfirmed);
        dest.writeLong(mTotalConfirmed);
        dest.writeLong(mTotalRecovered);
        dest.writeLong(mTotalDeaths);
    }*/
}

