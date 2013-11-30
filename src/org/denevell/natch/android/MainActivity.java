package org.denevell.natch.android;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.button1);
        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = getString(R.string.url_baseurl) + getString(R.string.url_threads);

                JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                setProgressBarIndeterminateVisibility(false);
                                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG) .show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                setProgressBarIndeterminateVisibility(false);
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG) .show();
                            }
                        });
                queue.add(jsObjRequest);
                setProgressBarIndeterminateVisibility(true);
            }
        });
    }

}
