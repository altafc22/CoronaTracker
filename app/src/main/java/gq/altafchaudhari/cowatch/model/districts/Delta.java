package gq.altafchaudhari.cowatch.model.districts;

import com.google.gson.annotations.SerializedName;

public class Delta
{
    @SerializedName("confirmed")
    private String confirmed;

    public String getConfirmed ()
    {
        return confirmed;
    }

    public void setConfirmed (String confirmed)
    {
        this.confirmed = confirmed;
    }
}
	