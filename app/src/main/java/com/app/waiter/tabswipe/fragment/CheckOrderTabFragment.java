package com.app.waiter.tabswipe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.waiter.Common.GlobalVars;
import com.app.waiter.R;
import java.util.List;

/**
 * Created by javier.gomez on 13/05/2015.
 */
public class CheckOrderTabFragment extends Fragment {
    private TextView itemCheckPrice;
    private static GlobalVars globalVariables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVariables = (GlobalVars) getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_order_waiter, null);

        itemCheckPrice = (TextView) view.findViewById(R.id.checkPrice);
        itemCheckPrice.setText("Total: "+ globalVariables.getOrder().getBill() + " €");

        return view;
    }

    public void updateBill() {
        itemCheckPrice.setText("Total: " + globalVariables.getOrder().getBill() + " €");
    }
}
