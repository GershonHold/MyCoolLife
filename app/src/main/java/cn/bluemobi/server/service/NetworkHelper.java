package cn.bluemobi.server.service;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkHelper {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {

            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            boolean info = cm.getActiveNetworkInfo().isAvailable();
            return info;
//            if (info != null) {
//                for (int i = 0; i < info.length; i++) {
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }
//            }
        }
        return false;
    }
}
