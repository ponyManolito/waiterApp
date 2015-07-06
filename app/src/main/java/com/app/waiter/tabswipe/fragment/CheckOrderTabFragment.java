package com.app.waiter.tabswipe.fragment;

import android.content.Context;
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
import com.app.waiter.Model.List.Content;
import com.app.waiter.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by javier.gomez on 13/05/2015.
 */
public class CheckOrderTabFragment extends Fragment {
    private List<Object> listObjects;
    private TextView itemCheckPrice;
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

}
