package gq.altafchaudhari.cowatch.utilities;


import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import gq.altafchaudhari.cowatch.model.world.Global;
import kotlin.Triple;

public class Utils {

    public static Triple<Float, Float, Float> provideBarWeights(Global response)
    {
        try {
            float yellowVal = response.getTotalConfirmed();
            float greenVal = response.getTotalRecovered();
            float redVal = response.getTotalDeaths();

            while (yellowVal > 1f && greenVal > 1f && redVal > 1f) {
                yellowVal /= 2f;
                greenVal /= 2f;
                redVal /= 2f;
            }
            return new Triple(yellowVal, greenVal, redVal);
        } catch (Exception e){ // If API response is incorrect
        e.printStackTrace();
        return new Triple(1f, 1f, 1f);
    }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

/*    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMetered = cm.isActiveNetworkMetered();
        return isMetered;
       *//* try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }*//*
    }*/

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}