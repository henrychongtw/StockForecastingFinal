package tw.edu.ntust.stockoraclet.firebasepackage;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by user on 2016/7/23.
 */
public class InstanceIDService  extends FirebaseInstanceIdService {
    protected String token;

    @Override
    public void onTokenRefresh() {
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM", "Token: " + token);
    }
}
