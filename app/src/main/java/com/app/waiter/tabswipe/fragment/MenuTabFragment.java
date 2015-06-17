package com.app.waiter.tabswipe.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.waiter.Common.GlobalVars;
import com.app.waiter.Model.Adapter.ExpandableListAdapter;
import com.app.waiter.Model.DataModel.OrderJSON.InOrder;
import com.app.waiter.Model.DataModel.OrderJSON.InOrderType;
import com.app.waiter.Model.DataModel.OrderJSON.InProductInOrder;
import com.app.waiter.Model.DataModel.Product;
import com.app.waiter.Model.List.Content;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by javier.gomez on 13/05/2015.
 */
public class MenuTabFragment extends Fragment {
    private List<String> headers;
    private TextView itemDescription;
    private Button btnAddOrder;
    private ImageView itemImage;
    private ExpandableListView listView;
    private static GlobalVars globalVariables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVariables = (GlobalVars) getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_menu_waiter, null);

        itemDescription = (TextView) view.findViewById(R.id.menuItemDescription);
        itemImage = (ImageView) view.findViewById(R.id.menuItemImage);
        btnAddOrder = (Button) view.findViewById(R.id.btnAddOrder);
        headers = getTypesHeaders();

        listView = (ExpandableListView) view.findViewById(R.id.listViewMenu);
        listView.setAdapter(new ExpandableListAdapter(view.getContext(), headers, globalVariables.getDataset()));
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final Content content = globalVariables.getDataset().get(headers.get(groupPosition)).get(childPosition);
                itemDescription.setText(content.getDescription());
                if (content.getImageData() != null) {
                    Bitmap bitmap = decodeImage(content);
                    itemImage.setImageBitmap(bitmap);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.estandar);
                    itemImage.setImageDrawable(drawable);
                }
                btnAddOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InOrder order = globalVariables.getOrder();
                        boolean enc = false;
                        for (InOrderType orderType : order.getTypes()) {
                            if (orderType.getType().equals(content.getType())) {
                                for (InProductInOrder productInOrder : orderType.getProducts()) {
                                    if (productInOrder.getIdProduct() == content.getId()) {
                                        enc = true;
                                        productInOrder.increaseQuantity();
                                        order.setBill(order.getBill() + content.getPrice());
                                        break;
                                    }
                                }
                                if (!enc) {
                                    InProductInOrder productInOrder = new InProductInOrder(
                                            content.getId(), 1, content.getPrice());
                                    orderType.getProducts().add(productInOrder);
                                    enc = true;
                                } else {
                                    break;
                                }
                            }
                        }
                        if (!enc) {
                            InProductInOrder productInOrder = new InProductInOrder(
                                    content.getId(), 1, content.getPrice());
                            List<InProductInOrder> listProducts = new ArrayList<InProductInOrder>();
                            listProducts.add(productInOrder);
                            InOrderType orderType = new InOrderType(content.getType(), "Ordered",
                                    listProducts);
                            order.getTypes().add(orderType);
                            order.setBill(order.getBill() + content.getPrice());
                        }
                        Toast.makeText(view.getContext(), "Plato añadido a la orden", Toast.LENGTH_SHORT).show();
                    }
                });
                btnAddOrder.setEnabled(true);
                return false;
            }
        });
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                String header = headers.get(groupPosition);
                if (globalVariables.getDataset().get(header) == null) {
                    globalVariables.getDataset().put(header, getDataByHeader(header));
                }
                return false;
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

    private List<String> getTypesHeaders() {
        try {
            List<String> headers = new ArrayList<String>();
            List<LinkedTreeMap> listHeaders = getHeaders();
            for (LinkedTreeMap l : listHeaders) {
                String type = ((String) l.get("name"));
                headers.add(type);
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

    private List<Content> getDataByHeader(String header) {
        List<Content> list = new ArrayList<Content>();
        Content content = null;
        try {
            List<Product> listProducts = convertToContent(getProducts(header));
            for (Product product : listProducts) {
                content = new Content();
                content.setId(product.getId());
                content.setMainText(product.getName());
                content.setSubText(String.valueOf(product.getPrice()));
                content.setDescription(product.getDescription());
                content.setImageData(product.getImageData());
                content.setType(header);
                content.setPrice(product.getPrice());
                list.add(content);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
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

}
