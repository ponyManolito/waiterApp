package com.app.waiter.Model.Adapter;

/**
 * Created by javier.gomez on 09/06/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.waiter.MainWaiter;
import com.app.waiter.Model.DataModel.OrderJSON.InOrder;
import com.app.waiter.Model.List.Content;
import com.app.waiter.R;
import com.app.waiter.tabswipe.fragment.CheckOrderTabFragment;

import java.util.List;

public class CheckOrderListAdapter extends ArrayAdapter<Object> {
    private LayoutInflater layoutInflater;
    private Context context;

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
        final Content content = (Content) getItem(position);
        TextView textMain = (TextView) convertView.findViewById(R.id.itemCheckTitle);
        TextView textSub = (TextView) convertView.findViewById(R.id.itemCheckPrice);
        textMain.setText(content.getMainText());
        textSub.setText(content.getSubText());
        Button btnRmvOrder = (Button) convertView.findViewById(R.id.btnRmvOrder);
        btnRmvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Remove " + content.getMainText(), Toast.LENGTH_SHORT).show();
                CheckOrderTabFragment fragment = (CheckOrderTabFragment) ((MainWaiter) v.getContext()).gettAdapter().getItem(3);
                fragment.removeProduct(content.getId());
                fragment.updateBill();
            }
        });
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