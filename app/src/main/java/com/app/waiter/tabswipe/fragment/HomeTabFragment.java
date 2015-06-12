package com.app.waiter.tabswipe.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.waiter.R;
import java.util.List;

/**
 * Created by javier.gomez on 13/05/2015.
 */
public class HomeTabFragment extends Fragment {
    private ViewGroup layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_waiter, null);
        Drawable homeImage = getResources().getDrawable(R.drawable.sample_home_img);
        Drawable btn1 = getResources().getDrawable(R.drawable.menu);
        Drawable btn2 = getResources().getDrawable(R.drawable.menu_dia);
        Drawable btn3 = getResources().getDrawable(R.drawable.pedidos);
        Drawable btn4 = getResources().getDrawable(R.drawable.contacto);
        view.setBackground(homeImage);

        /* Not working
        GridLayout gl = (GridLayout) view;

        int height = view.getHeight();
        int width = view.getWidth();

        ImageButton img1 = new ImageButton(view.getContext());
        img1.setImageDrawable(btn1);
        img1.setMaxHeight(height/4);
        img1.setMaxWidth(width / 4);
        img1.setPadding(40,40,40,40);

        ImageButton img2 = new ImageButton(view.getContext());
        img1.setImageDrawable(btn2);
        img1.setMaxHeight(height/4);
        img1.setMaxWidth(width / 4);
        img1.setPadding(40,40,40,40);

        gl.addView(img1);
        gl.addView(img2);
        */
        return view;
    }


}
