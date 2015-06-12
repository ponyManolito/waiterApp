package com.app.waiter.tabswipe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.waiter.R;
import java.util.List;

/**
 * Created by javier.gomez on 13/05/2015.
 */
public class CheckOrderTabFragment extends Fragment {
    private List<Object> dataset;
    private TextView itemCheckPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_order_waiter, null);

        itemCheckPrice = (TextView) view.findViewById(R.id.checkPrice);
        itemCheckPrice.setText("Total: 0.00 €");

        return view;
    }

}
