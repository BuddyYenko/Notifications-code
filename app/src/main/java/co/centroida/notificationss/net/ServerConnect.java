package co.centroida.notificationss.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ServerConnect {

    private static final String TAG = "ServerConnect";

    public static final String NO_CONNECTION = "Unable to connect. Please check your connection";

    private static RequestQueue requestQueue = null;

    public static RequestQueue getRequestQueue(final Context context) {
        if (requestQueue == null) requestQueue = Volley.newRequestQueue(context);
        return requestQueue;
    }

    public static boolean isOnline(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } return false;
    }

    public static void connectPost(final Context context, final String url, final BundleServer bundleServer, final int queAt, final OnMultiReturnedResultsListener onMultiReturnedResultsListener) {

        final long start = System.currentTimeMillis();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        final long end = System.currentTimeMillis();

                        Log.d(TAG, "Connection time: " + (end - start) + " mills");

                        if (onMultiReturnedResultsListener != null) {
                            if (response != null)
                                Log.d("Results Connection", response);
                            onMultiReturnedResultsListener.onResultsReturned(response, queAt);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        final long end = System.currentTimeMillis();

                        Log.d(TAG, "Connection time: " + (end - start) + " mills");

                        try {
                            Log.d("ServerConnect", error.toString());
                            if (onMultiReturnedResultsListener != null) {
                                onMultiReturnedResultsListener.onResultsReturned(null, queAt);
                            }
                        } catch (NullPointerException n) {
                            Log.d("ServerConnect", n.toString());
                            onMultiReturnedResultsListener.onResultsReturned(null, queAt);
                        }
                    }
                }){
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                int size = bundleServer.postData.length;
                for (int i=0; i<size; i++) {
                    params.put(bundleServer.postId[i], bundleServer.postData[i]);
                }
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = getRequestQueue(context);// Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public interface OnMultiReturnedResultsListener {
        void onResultsReturned(@Nullable String results, int queAt);
    }
}
