package com.app.waiter.Model.Adapter;

/**
 * Created by javier.gomez on 09/06/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.waiter.Model.DataModel.OrderJSON.InOrder;
import com.app.waiter.Model.List.Content;
import com.app.waiter.R;

import java.util.List;

public class CheckOrderListAdapter extends ArrayAdapter<Object> {
    private LayoutInflater layoutInflater;

    public CheckOrderListAdapter(Context context, List<Object> objects) {
        super(context, 0, objects);
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.list_check_item, null);
            holder.setTextMain((TextView) convertView.findViewById(R.id.itemCheckTitle));
            holder.setTextSub((TextView) convertView.findViewById(R.id.itemCheckPrice));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Content content = (Content) getItem(position);
        TextView textMain = (TextView) convertView.findViewById(R.id.itemCheckTitle);
        TextView textSub = (TextView) convertView.findViewById(R.id.itemCheckPrice);
        textMain.setText(content.getMainText());
        textSub.setText(content.getSubText());
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