package com.app.waiter.tabswipe.fragment;

import android.app.ListFragment;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.waiter.Model.Adapter.MenuArrayAdapter;
import com.app.waiter.Model.List.Content;
import com.app.waiter.Model.List.Header;
import com.app.waiter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by javier.gomez on 13/05/2015.
 */
public class MenuTabFragment extends Fragment {
    private String tab;
    private List<Object> dataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        tab = bundle.getString("tab");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_waiter, null);

        TextView tv = (TextView) view.findViewById(R.id.textViewTitle);
        tv.setText(tab);

        // SQL query, then FOR state
        String[] headers = {"Primeros", "Segundos"};
        dataset = fillData(headers);

        ListView listView = (ListView) view.findViewById(R.id.listViewMenu);
        listView.setAdapter(new MenuArrayAdapter(view.getContext(), dataset));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = dataset.get(position);
                if (item instanceof Content) {
                    Toast.makeText(view.getContext(), ((Content) item).getMainText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private List<Object> fillData(String[] headers) {
        List<Object> list = new ArrayList<Object>();
        Content content = null;
        Header header = null;
        for (String headerName : headers) {
            header = new Header();
            header.setTitle(headerName);
            list.add(header);
            for (int i = 0; i < 11; i++) {
                content = new Content();
                content.setMainText("Pollo");
                content.setSubText("10.75");
                list.add(content);
            }
        }
        return list;
    }



}
