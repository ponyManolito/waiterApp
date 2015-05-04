package com.app.waiter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by javier.gomez on 22/04/2015.
 */
public class ConfigureTableActivity extends Activity {
    public final static String TABLE_NUMBER = "com.app.waiter.TABLE_NUMBER";
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_table);

        prgDialog = new ProgressDialog(this);
    }

    public void getTable(View view) {
        prgDialog.show();
        new HttpAsyncTask().execute("http://192.168.10.224:8080/tables/gettable?id=1");
    }

    public void assignTable(View view) {
        prgDialog.show();
        EditText numTable = (EditText) findViewById(R.id.numTable);
        EditText description = (EditText) findViewById(R.id.descriptionTable);
        new HttpAsyncTask().execute("http://192.168.10.224:8080/tables/assignTable?",
                                    numTable.getText().toString(),
                                    description.getText().toString());
    }

    public static boolean assignTableWS(String... urls) {
        //JSONObject obj = null;
        HttpAuthentication authHeader = new HttpBasicAuthentication("admin", "admin");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("name", urls[1]));
        params.add(new BasicNameValuePair("description", urls[2]));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        String url = urls[0] + paramString;

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.POST,
                requestEntity, Boolean.class);
        return responseEntity.getBody();
    }

    public static JSONObject getTableWS(String url) {
        JSONObject obj = null;
        HttpAuthentication authHeader = new HttpBasicAuthentication("admin", "admin");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
                                                                requestEntity, String.class);
        String res = responseEntity.getBody();
        try {
            obj = new JSONObject(res);
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return obj;
    }

    public void goToHomePage() {
        Intent intent = new Intent(this, MainWaiter.class);
        EditText numTable = (EditText) findViewById(R.id.numTable);
        String table = numTable.getText().toString();
        intent.putExtra(TABLE_NUMBER, table);
        startActivity(intent);
    }

    public static boolean isNotNull(String txt) {
        return txt != null && txt.trim().length() > 0 ? true : false;
    }

    private class HttpAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            if (urls[0].contains("assignTable")) {
                try {
                    assignTableWS(urls);
                    result = "Mesa asignada";
                    goToHomePage();
                    finish();
                } catch (Exception e) {
                    result = e.getMessage();
                }
            }
            // Just for testing
            if (urls[0].contains("getTable")) {
                JSONObject obj = getTableWS(urls[0]);
                try {
                    result = obj.getString("name") + " - " + obj.getString("description");
                } catch (Exception e) {
                    Log.d("HttpAsyncTask", e.getLocalizedMessage());
                }
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            // To check the data
            prgDialog.hide();
            Toast.makeText(getBaseContext(), "Received: " + result, Toast.LENGTH_LONG).show();
        }
    }

}