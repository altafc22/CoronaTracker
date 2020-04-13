package gq.altafchaudhari.cowatch.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class GlobalData {


    public static boolean isdebug = true;

    public enum ScannerAction {
        Capture, Verify
    }

    public static boolean isConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        return intent.getExtras().getBoolean("connected");
    }
}
