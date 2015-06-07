package com.app.waiter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.app.waiter.Common.GlobalVars;

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
    private static GlobalVars globalVariables;
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_configure_table);

        configureGlobalVars();

        prgDialog = new ProgressDialog(this);
    }

    public void configureGlobalVars() {
        globalVariables = (GlobalVars) getApplicationContext();
        globalVariables.setServerIP("192.168.1.39");
        globalVariables.setPort("8080");
        globalVariables.setUserServer("admin");
        globalVariables.setPassServer("admin");
    }

    public void assignTable(View view) {
        EditText numTable = (EditText) findViewById(R.id.numTable);
        EditText description = (EditText) findViewById(R.id.descriptionTable);
        String baseURL = "http://" + globalVariables.getServerIP() + ":" + globalVariables.getPort();
        new HttpAsyncTask().execute(baseURL + "/tables/assignTable?",
                                    numTable.getText().toString(),
                                    description.getText().toString());
    }

    public static boolean assignTableWS(String... urls) {
        HttpAuthentication authHeader = new HttpBasicAuthentication(globalVariables.getUserServer(),
                globalVariables.getPassServer());
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
            return result;
        }
        @Override
        protected void onPreExecute() {
            prgDialog.setMessage("Asignando mesa...");
            prgDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // To check the data
            prgDialog.dismiss();
            Toast.makeText(getBaseContext(), "Received: " + result, Toast.LENGTH_LONG).show();
        }
    }

}