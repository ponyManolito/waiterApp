package com.app.waiter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by javier.gomez on 22/04/2015.
 */
public class ConfigureTableActivity extends Activity {
    Button button;
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_table);

        prgDialog = new ProgressDialog(this);
    }

    public void getTable(View view) {
        EditText numTable = (EditText) findViewById(R.id.numTable);
        String table = numTable.getText().toString();
        RequestParams params = new RequestParams();
        if (isNotNull(table)) {
            // Validate fields
            params.put("numTable", table);
            getTableWS(params);
        } else {
            Toast.makeText(getApplicationContext(), "Please fill the form", Toast.LENGTH_LONG).show();
        }
    }

    public void getTableWS(RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("admin","admin");
        client.get("http://192.168.10.224:8080/tables/gettable", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(responseBody.toString());
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {
                        Toast.makeText(getApplicationContext(), obj.toString(), Toast.LENGTH_LONG).show();
                    }
                    // Else display error message
                    else {
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static boolean isNotNull(String txt) {
        return txt != null && txt.trim().length() > 0 ? true : false;
    }

}