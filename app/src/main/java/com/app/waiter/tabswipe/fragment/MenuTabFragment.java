package com.app.waiter.tabswipe.fragment;

import android.app.ListFragment;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.waiter.Model.Adapter.MenuArrayAdapter;
import com.app.waiter.Model.DataModel.Product;
import com.app.waiter.Model.List.Content;
import com.app.waiter.Model.List.Header;
import com.app.waiter.R;
import com.google.gson.internal.LinkedTreeMap;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by javier.gomez on 13/05/2015.
 */
public class MenuTabFragment extends Fragment {
    private String tab;
    private List<Object> dataset;
    private TextView itemDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_waiter, null);

        itemDescription = (TextView) view.findViewById(R.id.menuItemDescription);

        // SQL query, then FOR state
        String[] headers = {"Primeros", "Segundos", "Bebidas"};
        dataset = fillData(headers);

        ListView listView = (ListView) view.findViewById(R.id.listViewMenu);
        listView.setAdapter(new MenuArrayAdapter(view.getContext(), dataset));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = dataset.get(position);
                if (item instanceof Content) {
                    itemDescription.setText(((Content) item).getDescription());
                }
            }
        });

        return view;
    }

    private List<Product> convertToContent(List<LinkedTreeMap> list) {
        List<Product> listProducts = new ArrayList<Product>();
        for (LinkedTreeMap l : list) {
            Product product = new Product();
            product.setName((String)l.get("name"));
            product.setDescription((String)l.get("description"));
            product.setImageName((String)l.get("image"));
            product.setMovieName((String)l.get("movie"));
            product.setPrice((Double)l.get("price"));
            product.setEmpty((Boolean)l.get("empty"));
            listProducts.add(product);
        }
        return listProducts;
    }

    private List<Object> fillData(String[] headers) {
        List<Object> list = new ArrayList<Object>();
        Content content = null;
        Header header = null;
        try {
            for (String headerName : headers) {
                header = new Header();
                header.setTitle(headerName);
                list.add(header);
                List<Product> listProducts = convertToContent(getProducts());
                for (Product product : listProducts) {
                    content = new Content();
                    content.setMainText(product.getName());
                    content.setSubText(String.valueOf(product.getPrice()));
                    content.setDescription(product.getDescription());
                    list.add(content);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<LinkedTreeMap> getProducts() throws ExecutionException, InterruptedException {
        return new HttpAsyncTask().execute("http://192.168.10.224:8080/products/getall").get();
        // TODO Put param type
    }

    public static List<LinkedTreeMap> getProductsWS(String... urls) {
        HttpAuthentication authHeader = new HttpBasicAuthentication("admin", "admin");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        /*List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("type", urls[1]));

        String paramString = URLEncodedUtils.format(params, "utf-8");
*/
        String url = urls[0];// + paramString;

        ResponseEntity<ArrayList> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
                requestEntity,ArrayList.class);
        return (List<LinkedTreeMap>) responseEntity.getBody();
    }

    private class HttpAsyncTask extends AsyncTask<String,Void,List<LinkedTreeMap>> {
        @Override
        protected List<LinkedTreeMap> doInBackground(String... urls) {
            String result = "";
            if (urls[0].contains("getall")) {
                try {
                    return getProductsWS(urls);
                } catch (Exception e) {
                    result = e.getMessage();
                }
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(List<LinkedTreeMap> result) {
            // To check the data
        }
    }



}
