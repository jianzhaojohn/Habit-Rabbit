package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class VolleySingleton {

    private static final String PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100d2d5578512e9eaf0a9a6a993a67c4c0c310f17c81a27e9d637269921ac53ef075f29705045303b10a266f4fb64b0ed850b45d394c8e760fade9fc8b605dba70f3c3d86cf9a5b1cbe78cf8a47fe5dce296cf765eade76fd54ac19b3a26715ef1c2e5f416be083395f7eff9dc5c05e444e4d45f62fb3ac935226f18af3ba0925a09a7a7d44913770868bbb53d1e89e2e3d7ff18a5fde56de0bb9fe78d958b4b905df181e6f0dafca62c20ead1aa6c507dff01f79d3e4d87ad04236a1ab20ca99591326b7e2406ba9c2c3f20e08a85a4a655b34d26167cfbddd1b9d40fa38cbf4016b4dcae3df588a6172fe1ed7f7fbc9a7b61d1b77c86a216ed1914252d19a0baf0203010001";
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context) {
        mRequestQueue = getRequestQueue(context);
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            TrustManager tm[] = {new PubKeyManager(PUBLIC_KEY)};
            SSLSocketFactory pinnedSSLSocketFactory = null;
            try {
                SSLContext TLSContext = SSLContext.getInstance("TLS");
                TLSContext.init(null, tm, null);
                pinnedSSLSocketFactory = TLSContext.getSocketFactory();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext(), new HurlStack(null, pinnedSSLSocketFactory));
        }
        return mRequestQueue;
    }
}
