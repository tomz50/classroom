package com.classroom.returnonattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.classroom.home.R;

import java.util.List;

public class AttendanceListAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<AttendanceList> itemList;

    public AttendanceListAdapter(Context c, List<AttendanceList> itemList) {
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
        AttendanceList data = itemList.get(position);
        convertView = myInflater.inflate(R.layout.returnonattendance_item_view, null);

        TextView id = convertView.findViewById(R.id.id);
        id.setText(data.getId());
        TextView userid = convertView.findViewById(R.id.Userid);
        userid.setText(data.getUserid());
        TextView name = convertView.findViewById(R.id.ThContact);
        name.setText(data.getName());

        TextView attendancetime_i = convertView.findViewById(R.id.AttendanceTime_i);
        attendancetime_i.setText(data.getAttendancetime_i());
        if (attendancetime_i.getText().equals("null"))
            attendancetime_i.setText(null);

        TextView attendancetime_o = convertView.findViewById(R.id.AttendanceTime_o);
        attendancetime_o.setText(data.getAttendancetime_o());
        if (attendancetime_o.getText().equals("null"))
            attendancetime_o.setText(null);

        TextView trtime = convertView.findViewById(R.id.TrTime);
        trtime.setText(data.getTrtime());
        if (trtime.getText().equals("null"))
            trtime.setText(null);
        return convertView;
    }
}
