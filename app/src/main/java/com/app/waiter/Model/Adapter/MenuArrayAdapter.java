package com.app.waiter.Model.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.waiter.Model.List.Content;
import com.app.waiter.Model.List.Header;
import com.app.waiter.R;

import java.util.List;

/**
 * Created by javier.gomez on 27/05/2015.
 */
public class MenuArrayAdapter extends ArrayAdapter<Object> {
    private LayoutInflater layoutInflater;

    public MenuArrayAdapter(Context context, List<Object> objects) {
        super(context, 0, objects);
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItem(position) instanceof Header) {
            if (convertView == null || convertView.findViewById(R.id.itemSeparator) == null) {
                convertView = layoutInflater.inflate(R.layout.list_menu_header_waiter, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.itemSeparator);
            Header header = (Header) getItem(position);
            textView.setText(header.getTitle());
        } else {
            Holder holder = null;
            if (convertView == null || convertView.findViewById(R.id.itemSeparator) != null ) {
                holder = new Holder();
                convertView = layoutInflater.inflate(R.layout.list_menu_waiter, null);
                holder.setTextMain((TextView) convertView.findViewById(R.id.foodTitle));
                holder.setTextSub((TextView) convertView.findViewById(R.id.foodPrice));
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            Content content = (Content) getItem(position);
            TextView textMain = (TextView) convertView.findViewById(R.id.foodTitle);
            TextView textSub = (TextView) convertView.findViewById(R.id.foodPrice);
            textMain.setText(content.getMainText());
            textSub.setText(content.getSubText());
        }
        return convertView;
    }
}

class Holder {
    private TextView textMain;

    private TextView textSub;

    public TextView getTextMain()
    {
        return textMain;
    }

    public void setTextMain(TextView textView)
    {
        this.textMain = textView;
    }

    public TextView getTextSub()
    {
        return textSub;
    }

    public void setTextSub(TextView textView)
    {
        this.textSub = textView;
    }
}
