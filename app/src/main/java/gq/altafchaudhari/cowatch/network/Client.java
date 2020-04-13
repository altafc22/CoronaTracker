package gq.altafchaudhari.cowatch.network;

import gq.altafchaudhari.cowatch.utilities.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Retrofit retrofit;
    private static Retrofit indian_retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

   public static Retrofit getIndianRetrofitInstance() {
        if (indian_retrofit == null) {
            indian_retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return indian_retrofit;
    }
}