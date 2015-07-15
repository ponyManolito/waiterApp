package com.app.waiter.tabswipe.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.waiter.Common.GlobalVars;
import com.app.waiter.Model.Adapter.CheckOrderListAdapter;
import com.app.waiter.Model.Adapter.ExpandableListAdapter;
import com.app.waiter.Model.DataModel.OrderJSON.InOrder;
import com.app.waiter.Model.DataModel.OrderJSON.InOrderType;
import com.app.waiter.Model.DataModel.OrderJSON.InProductInOrder;
import com.app.waiter.Model.DataModel.Product;
import com.app.waiter.Model.List.Content;
import com.app.waiter.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by javier.gomez on 13/05/2015.
 */
public class CheckOrderTabFragment extends Fragment {
    private List<Object> listObjects;
    private TextView itemCheckPrice;
    private Button btnCheckOrder;
    private GlobalVars globalVariables;
    private View view;
    private DecimalFormat df;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVariables = (GlobalVars) getActivity().getApplicationContext();
        df = new DecimalFormat("#.##");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_check_order_waiter, null);
        listView = (ListView) view.findViewById(R.id.listViewCheckOrder);

        btnCheckOrder = (Button) view.findViewById(R.id.btnCheckOrder);
        /*btnCheckOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    insertOrder();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/
        itemCheckPrice = (TextView) view.findViewById(R.id.checkPrice);
        itemCheckPrice.setText("Total: "+ df.format(globalVariables.getOrder().getBill()) + " €");

        return view;
    }

    private List<Object> getObjectsFromOrder(InOrder order) {
        List<Object> list = new ArrayList<Object>();
        for (InOrderType type : order.getTypes()) {
            for (InProductInOrder product : type.getProducts()) {
                Content content = new Content();
                content.setId(product.getIdProduct());
                content.setMainText(product.getName());
                String totalPrice = String.valueOf(df.format(product.getPrice() * product.getQuantity()));
                String subText = String.valueOf(product.getPrice()) + " € x " + product.getQuantity() +
                        " = " + totalPrice + " €";
                content.setSubText(subText);
                list.add(content);
            }
        }
        return list;
    }

    public void updateBill() {
        listObjects = getObjectsFromOrder(globalVariables.getOrder());
        listView.setAdapter(new CheckOrderListAdapter(view.getContext(), listObjects));
        itemCheckPrice.setText("Total: " + df.format(globalVariables.getOrder().getBill()) + " €");
        if (globalVariables.getOrder().getBill() == 0) {
            btnCheckOrder.setEnabled(false);
        } else {
            btnCheckOrder.setEnabled(true);
        }
    }

    public void removeProduct(int id) {
        InOrder order = globalVariables.getOrder();
        for (InOrderType orderType : order.getTypes()) {
            for (InProductInOrder product : orderType.getProducts()) {
                if (product.getIdProduct() == id) {
                    if (product.getQuantity() > 1) {
                        product.decreaseQuantity();
                    } else {
                        orderType.getProducts().remove(product);
                    }
                    order.setBill(order.getBill()-product.getPrice());
                    break;
                }
            }
        }
    }

    private class OrderTask extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... urls) {
            try {
                insertOrderWS(urls);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void v) {
            // To check the data
        }
    }

    public void insertOrder() throws ExecutionException, InterruptedException {
        String baseURL = "http://" + globalVariables.getServerIP() + ":" + globalVariables.getPort();
        new OrderTask().execute(baseURL + "/orders/insertorder?");
    }

    public Object insertOrderWS(String... urls) throws JSONException {
        HttpAuthentication authHeader = new HttpBasicAuthentication(globalVariables.getUserServer(),
                globalVariables.getPassServer());
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        JSONObject order = orderToJSON(globalVariables.getOrder());

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("order", ""));//order.toString()));
        params.add(new BasicNameValuePair("error", ""));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        String url = urls[0] + paramString;

        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.POST,
                requestEntity,Object.class);
        return responseEntity.getBody();
    }

    private JSONObject orderToJSON(InOrder order) throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray orderTypeArray = new JSONArray();
        JSONArray productArray;
        for (InOrderType orderType : order.getTypes()) {
            productArray = new JSONArray();
            for (InProductInOrder productOrder : orderType.getProducts()) {
                JSONObject productObject = new JSONObject();
                productObject.put("idProduct", productOrder.getIdProduct());
                productObject.put("quantity", productOrder.getQuantity());
                productArray.put(productObject);
            }
            JSONObject orderTypeObject = new JSONObject();
            orderTypeObject.put("type", orderType.getType());
            orderTypeObject.put("status", orderType.getStatus());
            orderTypeObject.put("products", productArray);
            orderTypeArray.put(orderTypeObject);
        }
        json.put("idTable", globalVariables.getTable());
        json.put("description", order.getDescription());
        json.put("types", orderTypeArray);
        return json;
    }

}
