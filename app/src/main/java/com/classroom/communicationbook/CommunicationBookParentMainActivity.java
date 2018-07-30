package com.classroom.communicationbook;

import com.classroom.attendancerecord.CusSpinnerAdapter;
import com.classroom.attendancerecord.SpinnerItem;
import com.classroom.home.MainActivity;
import com.classroom.home.R;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CommunicationBookParentMainActivity extends AppCompatActivity implements View.OnClickListener {
    private Bundle bData;
    private String s_login,s_groupid,s_userid,s_name,s_level;
    private String classid,attendancedate;
    private TextView tvhUserid,tvhName,tvhGroupid;
    private Intent intent;
    private ListView listview;
    private CommunicationBookListAdapter adapter;
    private CommunicationBookListLoading communicationBookListLoading;
    private TextView tvClassId, tvAttendanceDate;
    Button btnDatePicker;
    CusSpinnerAdapter cusSpinnerAdapter;
    Spinner spinner;
    ArrayList<SpinnerItem> classCodeItems;
    List<CommunicationBookList> data_list;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_book_parent_main);
        checkLogin();
        initView();
        classcodeSpinner();
        loadListViewData();
    }

    private void checkLogin(){
        bData = this.getIntent().getExtras();
        if(bData == null || bData.getString("s_login").equals("false")){
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("s_login","false");
            startActivity(intent);
            this.finish();
        }
    }

    private void initView(){
        this.setTitle("聯絡簿(家長)");
            s_login   = bData.getString("s_login");
            s_groupid = bData.getString("s_groupid");
            s_userid  = bData.getString("s_userid");
            s_name   = bData.getString("s_name");
            s_level   = bData.getString("s_level");
            if (s_level.equals("2")){
                classid = bData.getString("classid");
                attendancedate = bData.getString("attendancedate");
            }
            else{
                classid = "A1";
                Date currentTime = Calendar.getInstance().getTime();
                attendancedate = DateFormat.format("yyyy-MM-dd", currentTime).toString();
            }
        tvhUserid = findViewById(R.id.tvhUserid);
        tvhName = findViewById(R.id.tvhName);
        tvhGroupid = findViewById(R.id.tvhGroupid);
        tvhUserid.setText(s_userid);
        tvhName.setText(s_name);
        tvhGroupid.setText(s_groupid);

        listview = findViewById(R.id.listview);
        listview.setEmptyView(findViewById(R.id.no_contact));
        tvClassId =findViewById(R.id.tvClassId);
        tvClassId.setText(classid);
        tvAttendanceDate = findViewById(R.id.tvAttendanceDate);
        tvAttendanceDate.setText(attendancedate);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        btnDatePicker.setOnClickListener(this);
    }

    private void classcodeSpinner(){
        spinner = findViewById(R.id.spnClassCode);

        classCodeItems = new ArrayList<SpinnerItem>();
        classCodeItems.add(new SpinnerItem("A1班","A1"));
        classCodeItems.add(new SpinnerItem("A2班","A2"));
        classCodeItems.add(new SpinnerItem("A3班","A3"));
//        String ww = tvClassId.getText().toString();
//        Log.i("DB_edt","ww"+tvClassId.getText().toString());
        cusSpinnerAdapter = new CusSpinnerAdapter(this,classCodeItems);
        spinner.setAdapter(cusSpinnerAdapter);
        spinner.setSelection(getPostiton(tvClassId.getText().toString(),classCodeItems));
//        spinner.setSelection(getPostiton(tvClassId.getText().toString(),classCodeItems));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem classCodeItemItem = classCodeItems.get(position);
                tvClassId.setText(classCodeItemItem.getCode());
                loadListViewData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int getPostiton(String findString,ArrayList<SpinnerItem> classCodeItems) {
        Log.i("DB_findString",findString.toString());
        int i;
        for (i = 0; i < classCodeItems.size(); i++) {
            SpinnerItem classCodeItemItem = classCodeItems.get(i);
            if (findString.equals(classCodeItemItem.getCode()))
                break;
        }
        if (i == classCodeItems.size())
            return 0;
        else
            return i;
    }

    private void loadListViewData(){

        communicationBookListLoading = new CommunicationBookListLoading(this,tvClassId.getText().toString(),tvAttendanceDate.getText().toString(),s_userid);

        try {
            data_list = communicationBookListLoading.execute("https://webhost2503.000webhostapp.com/classroom/communicationbook/communicationbooklist_query.php").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        adapter = new CommunicationBookListAdapter(this,data_list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //點選列表上的資料就跳往編輯好友頁面
                intent = new Intent();
                intentPutExtraS();
                String type;
                Log.i("DBuserid=","xx2"+data_list.get(position).getId());
                if (data_list.get(position).getSttime().equals("null") || data_list.get(position).getSttime().equals(null))
                    Toast.makeText(getApplicationContext(), "請先提醒學生未完成功課回報", Toast.LENGTH_LONG).show();
                intent.putExtra("id", data_list.get(position).getId());
                intent.putExtra("title","聯絡簿回覆(家長)");
                intent.putExtra("type","edit");
                intent.putExtra("attendancedate", tvAttendanceDate.getText());
                intent.putExtra("classid", tvClassId.getText());
//                Log.i("name=",data_list.get(position).getId());
                changeView(CommunicationBookParentMainActivity.this, CommunicationBookParentEditActivity.class);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDatePicker:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String mm = String.valueOf(monthOfYear + 101);
                                tvAttendanceDate.setText(year + "-" + mm.substring(1) + "-" + dayOfMonth);
                                loadListViewData();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.communicationbook_menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        intent = new Intent();
        intentPutExtraS();
        switch(id){
            case R.id.action_back:
                changeView(this, MainActivity.class);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void intentPutExtraS(){
        intent.putExtra("s_login",s_login);
        intent.putExtra("s_groupid",s_groupid);
        intent.putExtra("s_userid",s_userid);
        intent.putExtra("s_name",s_name);
        intent.putExtra("s_level","1");
    }
    //intent跳轉activity函式
    public void changeView(Context context, Class<?> cla){

        intent = intent.setClass(context, cla);
        startActivity(intent);
        this.finish();
    }
}