package com.app.waiter.tabswipe.fragment;

import android.app.ListFragment;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.waiter.R;

/**
 * Created by javier.gomez on 13/05/2015.
 */
public class MenuTabFragment extends Fragment {
    private String tab;

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
        View insideListView = inflater.inflate(R.layout.list_menu_waiter, null);
        View headerListView = inflater.inflate(R.layout.list_menu_header_waiter, null);
        TextView tv = (TextView) view.findViewById(R.id.textViewTitle);
        tv.setText(tab);

        TextView textViewSeparator = (TextView) headerListView.findViewById(R.id.itemSeparator);
        textViewSeparator.setText("Primeros");

        String[] columns = new String[] {"_id", "name", "price"};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[] {"0","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"1","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"2","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"3","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"4","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"5","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"6","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"7","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"8","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"9","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"10","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"11","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"12","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"13","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"14","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"15","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"16","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"17","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"18","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"19","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"20","Pollo","10.50 €"});
        cursor.addRow(new Object[] {"21","Pollo","10.50 €"});

        String[] fromColumns = {"name", "price"};
        int[] toViews = {R.id.foodTitle, R.id.foodPrice};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(insideListView.getContext(),
                R.layout.list_menu_waiter, cursor, fromColumns, toViews, 0);

        ListView listView = (ListView) view.findViewById(R.id.listViewMenu);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                TextView title = (TextView) view.findViewById(R.id.foodTitle);

                CharSequence text = "Selected: " + title.getText();
                Toast.makeText(view.getContext(), text, Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


}
