package com.classroom.communicationbook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.classroom.home.R;

import java.util.List;

public class CommunicationBookListAdapter extends BaseAdapter{
    private LayoutInflater myInflater;
    private List<CommunicationBookList> itemList;

    public CommunicationBookListAdapter(Context c, List<CommunicationBookList> itemList) {
        myInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommunicationBookList data = itemList.get(position);
        convertView = myInflater.inflate(R.layout.communicationbook_item_view,null);

        TextView id = convertView.findViewById(R.id.id);
        id.setText(data.getId());
        TextView userid = convertView.findViewById(R.id.Userid);
        userid.setText(data.getStuserid());

        TextView name = convertView.findViewById(R.id.ThContact);
        name.setText(data.getName());

        TextView thtime = convertView.findViewById(R.id.ThTime);
        if (data.getThtime().equals("null"))
            thtime.setText(null);
        else
            thtime.setText(data.getThtime());

        TextView sttime = convertView.findViewById(R.id.StTime);
        if (data.getSttime().equals("null"))
            sttime.setText(null);
        else
            sttime.setText(data.getSttime());
        Log.v("!!Sttime",">>"+data.getSttime());
        TextView prtime = convertView.findViewById(R.id.PrTime);
        if (data.getPrtime().equals("null"))
            prtime.setText(null);
        else
            prtime.setText(data.getPrtime());
        return convertView;
    }
}

