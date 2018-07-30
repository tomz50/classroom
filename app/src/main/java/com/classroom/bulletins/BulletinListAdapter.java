package com.classroom.bulletins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.classroom.home.R;

import java.util.List;

public class BulletinListAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<BulletinList> itemList;

    public BulletinListAdapter(Context c, List<BulletinList> itemList) {
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
        BulletinList data = itemList.get(position);
        convertView = myInflater.inflate(R.layout.bulletins_item_view,null);

        TextView id = convertView.findViewById(R.id.id);
        id.setText(data.getId());

        TextView subject = convertView.findViewById(R.id.Subject);
        subject.setText(data.getSubject());

        TextView implementationdate =  convertView.findViewById(R.id.ImplementationDate);
        implementationdate.setText(data.getImplementationdate());

        TextView effectivedate = convertView.findViewById(R.id.EffectiveDate);
        effectivedate.setText(data.getEffectivedate());
        return convertView;
    }
}
