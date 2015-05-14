package com.app.waiter.tabswipe.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.waiter.R;

/**
 * Created by javier.gomez on 13/05/2015.
 */
public class SwipeTabFragment extends Fragment {
    private String tab;
    private int color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        tab = bundle.getString("tab");
        color = bundle.getInt("color");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_waiter, null);
        TextView tv = (TextView) view.findViewById(R.id.textViewTitle);
        tv.setText(tab);
        view.setBackgroundResource(color);
        return view;
    }

}
