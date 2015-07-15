package com.app.waiter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.waiter.Common.GlobalVars;
import com.app.waiter.Model.DataModel.OrderJSON.InOrder;
import com.app.waiter.tabswipe.adapter.TabPagerAdapter;
import com.app.waiter.tabswipe.fragment.CheckOrderTabFragment;
import com.app.waiter.tabswipe.fragment.MenuTabFragment;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
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


public class MainWaiter extends ActionBarActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabPagerAdapter tAdapter;
    private ActionBar actionBar;
    ProgressDialog prgDialog;
    private GlobalVars globalVariables;

    private String[] tabTitles = {"Inicio", "Menu del dia", "Carta", "Pedido", "Contacto"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main_waiter);
        prgDialog = new ProgressDialog(this);
        globalVariables = (GlobalVars) getApplicationContext();

        globalVariables.setOrder(new InOrder());

        Intent intent = getIntent();
        globalVariables.setTable(intent.getStringExtra(ConfigureTableActivity.TABLE_NUMBER));

        // Tab initialization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        tAdapter = new TabPagerAdapter(getSupportFragmentManager());

        actionBar.setHomeButtonEnabled(false);
        viewPager.setAdapter(tAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tabName : tabTitles) {
            actionBar.addTab(actionBar.newTab().setText(tabName).setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * on swipe select the respective tab
             * */
            @Override
            public void onPageSelected(int position) {
                Fragment fragment = (Fragment) tAdapter.instantiateItem(viewPager, position);
                if (fragment instanceof CheckOrderTabFragment) {
                    ((CheckOrderTabFragment) fragment).updateBill();
                }
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            @Override
            public void onPageScrollStateChanged(int arg0) { }
        });
        viewPager.setOffscreenPageLimit(5);
    }

    public TabPagerAdapter gettAdapter() {
        return tAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_waiter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            unassignTable();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goToConfigurePage() {
        Intent intent = new Intent(this, ConfigureTableActivity.class);
        startActivity(intent);
    }

    private class HttpAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            if (urls[0].contains("unassignTable")) {
                try {
                    unassignTableWS(urls);
                    result = "Mesa liberada";
                    goToConfigurePage();
                    finish();
                } catch (Exception e) {
                    result = e.getMessage();
                }
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            prgDialog.setMessage("Liberando mesa...");
            prgDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // To check the data
            prgDialog.dismiss();
            Toast.makeText(getBaseContext(), "Received: " + result, Toast.LENGTH_LONG).show();
        }
    }

    public void unassignTable() {
        String baseURL = "http://" + globalVariables.getServerIP() + ":" + globalVariables.getPort();
        new HttpAsyncTask().execute(baseURL + "/tables/unassignTable?",
                globalVariables.getTable());
    }

    public boolean unassignTableWS(String... urls) {
        HttpAuthentication authHeader = new HttpBasicAuthentication(globalVariables.getUserServer(),
                globalVariables.getPassServer());
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("name", urls[1]));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        String url = urls[0] + paramString;

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.POST,
                requestEntity, Boolean.class);
        return responseEntity.getBody();
    }
}
