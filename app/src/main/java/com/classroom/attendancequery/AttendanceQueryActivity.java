package com.classroom.attendancequery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.classroom.home.MainActivity;
import com.classroom.home.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AttendanceQueryActivity extends AppCompatActivity implements View.OnClickListener {
//    private AttendanceMaintainData maintainData;
//    private AttendanceDeleteData deleteData;
    private Bundle bData;
    private String s_login,s_groupid,s_userid,s_name;
    private String classid;
    private TextView tvhUserid,tvhName,tvhGroupid;
    private TextView tvUserid, tvName, tvAttendanceDate,tvAttendanceTime_I,tvAttendanceTime_O,tvTrTime,tvReason;
//    private EditText edtReason;
//    private Button btnConfirm,btn_back,btnSelect;
    private String type,attendancedate,userid;
    private Intent intent;
//    private String queryId;
//    private String queryId,newReason;

//    private int index;
//    private boolean isEdit = false;
//    private Boolean isDeleted = false;
    private static final int PICK_IMAGE = 1;
    private AttendanceLoading attendanceLoading;
    List<AttendanceDetails> data_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_query);
        checkLogin();
        initView();
        attendanceLoading = new AttendanceLoading(this,attendancedate,userid);
        try {
            data_list = attendanceLoading.execute("https://webhost2503.000webhostapp.com/classroom/attendancequery/attendance_query.php").get();
            tvUserid.setText(data_list.get(0).getUserid());
            tvName.setText(data_list.get(0).getName());
            tvAttendanceDate.setText(data_list.get(0).getAttendancedate());
            tvAttendanceTime_I.setText(data_list.get(0).getAttendancetime_i());
            if (tvAttendanceTime_I.getText().equals("null"))
                tvAttendanceTime_I.setText(null);
            tvAttendanceTime_O.setText(data_list.get(0).getAttendancetime_o());
            if (tvAttendanceTime_O.getText().equals("null"))
                tvAttendanceTime_O.setText(null);
            tvTrTime.setText(data_list.get(0).getTrtime());
            if (tvTrTime.getText().equals("null"))
                tvTrTime.setText(null);
            tvReason.setText(data_list.get(0).getReason());
            if (tvReason.getText().equals("null"))
                tvReason.setText(null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
        this.setTitle(bData.getString("title"));
        s_login   = bData.getString("s_login");
        s_groupid = bData.getString("s_groupid");
        s_userid  = bData.getString("s_userid");
        s_name    = bData.getString("s_name");
        classid = bData.getString("classid");
        attendancedate = bData.getString("attendancedate");

        userid = bData.getString("userid");

        tvhUserid = findViewById(R.id.tvhUserid);
        tvhName = findViewById(R.id.tvhName);
        tvhGroupid = findViewById(R.id.tvhGroupid);
        tvhUserid.setText(s_userid);
        tvhName.setText(s_name);
        tvhGroupid.setText(s_groupid);

        tvUserid = findViewById(R.id.Userid);
        tvName = findViewById(R.id.ThContact);
        tvAttendanceDate = findViewById(R.id.AttendanceDate);
        tvAttendanceTime_I = findViewById(R.id.AttendanceTime_I);
        tvAttendanceTime_O = findViewById(R.id.AttendanceTime_O);
        tvTrTime = findViewById(R.id.TrTime);
        tvReason = findViewById(R.id.Reason);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                Intent i = new Intent(this, AttendanceQueryMainActivity.class);
                intentPutExtraS();
                startActivity(i);
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bulletinquery_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_back:
                intent = new Intent(AttendanceQueryActivity.this, AttendanceQueryMainActivity.class);
                intentPutExtraS();
                startActivity(intent);
                this.finish();
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void intentPutExtraS(){
        intent.putExtra("s_login",s_login);
        intent.putExtra("s_groupid",s_groupid);
        intent.putExtra("s_userid",s_userid);
        intent.putExtra("s_name",s_name);
        intent.putExtra("s_level","2");
        intent.putExtra("classid",classid);
        intent.putExtra("attendancedate",attendancedate);
    }
}
