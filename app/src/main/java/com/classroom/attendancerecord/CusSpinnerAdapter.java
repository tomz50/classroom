package com.classroom.attendancerecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.classroom.home.R;

import java.util.ArrayList;

public class CusSpinnerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<SpinnerItem> attendanceid_lists;

    public CusSpinnerAdapter(Context c, ArrayList<SpinnerItem> attendanceid_lists) {
        inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
        this.attendanceid_lists = attendanceid_lists;
    }

    @Override
    public int getCount() {
        return attendanceid_lists.size();
    }

    @Override
    public Object getItem(int position) {
        return attendanceid_lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return attendanceid_lists.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpinnerItem attendanceIdItem = (SpinnerItem) getItem(position);
        View v = inflater.inflate(R.layout.spinner_view,null);
        TextView attendanceid_code = v.findViewById(R.id.spnCode);
        TextView attendanceid_name = v.findViewById(R.id.spnName);
        attendanceid_code.setText(attendanceIdItem.code);
        attendanceid_name.setText(attendanceIdItem.name);
        return v;
    }
}
