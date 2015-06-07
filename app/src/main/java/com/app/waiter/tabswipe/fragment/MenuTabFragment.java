package com.app.waiter.tabswipe.fragment;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.waiter.Common.GlobalVars;
import com.app.waiter.ConfigureTableActivity;
import com.app.waiter.Model.Adapter.MenuArrayAdapter;
import com.app.waiter.Model.DataModel.Product;
import com.app.waiter.Model.List.Content;
import com.app.waiter.Model.List.Header;
import com.app.waiter.R;
import com.google.gson.internal.LinkedTreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by javier.gomez on 13/05/2015.
 */
public class MenuTabFragment extends Fragment {
    private List<Object> dataset;
    private TextView itemDescription;
    private Button btnAddOrder;
    private ImageView itemImage;
    private static GlobalVars globalVariables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVariables = (GlobalVars) getActivity().getApplicationContext();
        Bundle bundle = getArguments();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_waiter, null);

        itemDescription = (TextView) view.findViewById(R.id.menuItemDescription);
        itemImage = (ImageView) view.findViewById(R.id.menuItemImage);
        btnAddOrder = (Button) view.findViewById(R.id.btnAddOrder);

        // SQL query, then FOR state
        String[] headers = getTypesHeaders();
        dataset = fillData(headers);

        ListView listView = (ListView) view.findViewById(R.id.listViewMenu);
        listView.setAdapter(new MenuArrayAdapter(view.getContext(), dataset));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = dataset.get(position);
                if (item instanceof Content) {
                    final Content content = (Content) item;
                    itemDescription.setText(content.getDescription());
                    if (content.getImageData() != null) {
                        Bitmap bitmap = decodeImage(content);
                        itemImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 254, 199, false));
                    } else {
                        Drawable drawable = getResources().getDrawable(R.drawable.estandar);
                        itemImage.setImageDrawable(drawable);
                    }
                    btnAddOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    btnAddOrder.setEnabled(true);
                }
            }
        });

        return view;
    }

    private Bitmap decodeImage(Content content) {
        String byteImage = content.getImageData();
        String encodedImage = byteImage.split("base64")[1];
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private String[] getTypesHeaders() {
        try {
            List<LinkedTreeMap> listHeaders = getHeaders();
            String[] headers = new String[listHeaders.size()];
            int i = 0;
            for (LinkedTreeMap l : listHeaders) {
                String type = ((String) l.get("name"));
                headers[i] = type;
                i++;
            }
            return headers;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Product> convertToContent(List<LinkedTreeMap> list) {
        List<Product> listProducts = new ArrayList<Product>();
        try {
            for (LinkedTreeMap l : list) {
                int id = ((Double) l.get("id")).intValue();
                Product product = getSingleProduct(id);
                listProducts.add(product);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
                List<Product> listProducts = convertToContent(getProducts(headerName));
                for (Product product : listProducts) {
                    content = new Content();
                    content.setMainText(product.getName());
                    content.setSubText(String.valueOf(product.getPrice()));
                    content.setDescription(product.getDescription());
                    content.setImageData(product.getImageData());
                    content.setType(headerName);
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

    private class HttpAsyncTask extends AsyncTask<String,Void,List<LinkedTreeMap>> {
        @Override
        protected List<LinkedTreeMap> doInBackground(String... urls) {
            if (urls[0].contains("getallproducts")) {
                try {
                    return getProductsWS(urls);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (urls[0].contains("getall")) {
                try {
                    return getTypesWS(urls);
                } catch (Exception e) {
                    e.printStackTrace();
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

    public List<LinkedTreeMap> getHeaders() throws ExecutionException, InterruptedException {
        String baseURL = "http://" + globalVariables.getServerIP() + ":" + globalVariables.getPort();
        return new HttpAsyncTask().execute(baseURL + "/types/getall").get();
    }

    public List<LinkedTreeMap> getProducts(String type) throws ExecutionException, InterruptedException {
        String baseURL = "http://" + globalVariables.getServerIP() + ":" + globalVariables.getPort();
        return new HttpAsyncTask().execute(baseURL + "/products/getallproducts?",
                type).get();
    }

    public static List<LinkedTreeMap> getProductsWS(String... urls) {
        HttpAuthentication authHeader = new HttpBasicAuthentication(globalVariables.getUserServer(),
                globalVariables.getPassServer());
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("type", urls[1]));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        String url = urls[0] + paramString;

        ResponseEntity<ArrayList> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
                requestEntity,ArrayList.class);
        return (List<LinkedTreeMap>) responseEntity.getBody();
    }

    public static List<LinkedTreeMap> getTypesWS(String... urls) {
        HttpAuthentication authHeader = new HttpBasicAuthentication(globalVariables.getUserServer(),
                globalVariables.getPassServer());
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        String url = urls[0];

        ResponseEntity<ArrayList> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
                requestEntity,ArrayList.class);
        return (List<LinkedTreeMap>) responseEntity.getBody();
    }

    private class ProductTask extends AsyncTask<String,Void,Product> {
        @Override
        protected Product doInBackground(String... urls) {
            try {
                return getSingleProductWS(urls);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Product result) {
            // To check the data
        }
    }

    public Product getSingleProduct(int id) throws ExecutionException, InterruptedException {
        String baseURL = "http://" + globalVariables.getServerIP() + ":" + globalVariables.getPort();
        return new ProductTask().execute(baseURL + "/products/getproduct?",
                String.valueOf(id)).get();
    }

    public static Product getSingleProductWS(String... urls) {
        HttpAuthentication authHeader = new HttpBasicAuthentication(globalVariables.getUserServer(),
                globalVariables.getPassServer());
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("id", urls[1]));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        String url = urls[0] + paramString;

        ResponseEntity<Product> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
                requestEntity,Product.class);
        return responseEntity.getBody();
    }

    /*private class OrderTask extends AsyncTask<String,Void,Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {
            if (urls[0].contains("addorder")) {
                try {
                    return getSingleProductWS(urls);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // To check the data
        }
    }*/

    public List<LinkedTreeMap> addOrder(String type) throws ExecutionException, InterruptedException {
        String baseURL = "http://" + globalVariables.getServerIP() + ":" + globalVariables.getPort();
        return new HttpAsyncTask().execute(baseURL + "/orders/insert?",
                type).get();
    }

    public static List<LinkedTreeMap> addOrderWS(String... urls) {
        HttpAuthentication authHeader = new HttpBasicAuthentication(globalVariables.getUserServer(),
                globalVariables.getPassServer());
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        JSONObject json = new JSONObject();
        JSONObject jsonOrder = new JSONObject();
        try {
            json.put("obj", jsonOrder);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("type", urls[1]));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        String url = urls[0] + paramString;

        ResponseEntity<ArrayList> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
                requestEntity,ArrayList.class);
        return (List<LinkedTreeMap>) responseEntity.getBody();
    }

    private class LoadImage extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            if (urls[0].contains("getall")) {

            }
            return null;
        }
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // To check the data
        }
    }

}
