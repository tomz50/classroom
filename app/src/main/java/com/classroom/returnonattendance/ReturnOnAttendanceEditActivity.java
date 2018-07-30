package com.classroom.returnonattendance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.classroom.home.MainActivity;
import com.classroom.home.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReturnOnAttendanceEditActivity extends AppCompatActivity implements View.OnClickListener {
    private AttendanceMaintainData maintainData;
    private AttendanceDeleteData deleteData;
    private Bundle bData;
    private String s_login,s_groupid,s_userid,s_name;
    private String classid,attendancedate;
    private TextView tvhUserid,tvhName,tvhGroupid;
    private TextView tvUserid, tvName, tvAttendanceDate;
    private EditText edtReason;
    private Button btnConfirm,btn_back,btnSelect;
    private String type,logdata;
    private Intent intent;
    private String queryId,newReason;

    private int index;
    private boolean isEdit = false;
    private Boolean isDeleted = false;
    private static final int PICK_IMAGE = 1;
    private AttendanceLeaveLoading attendanceLeaveLoading;
    List<Attendance> data_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returnonattendance_edit);
        checkLogin();
        initView();
        if(type.equals("new")){
            tvUserid.setText(bData.getString("userid"));
            tvName.setText(bData.getString("name"));
            tvAttendanceDate.setText(bData.getString("attendancedate"));
        }
        else{
            queryId = bData.getString("id");
            Log.i("DBqueryId=",queryId);
            isEdit = true;
            attendanceLeaveLoading = new AttendanceLeaveLoading(this,queryId);
            try {
                data_list = attendanceLeaveLoading.execute("https://webhost2503.000webhostapp.com/classroom/returnonattendance/attendance_query_leave.php").get();
                tvUserid.setText(data_list.get(0).getUserid());
                tvName.setText(data_list.get(0).getName());
                tvAttendanceDate.setText(data_list.get(0).getAttendancedate());
                edtReason.setText(data_list.get(0).getReason());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
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
        type = bData.getString("type");
        s_login   = bData.getString("s_login");
        s_groupid = bData.getString("s_groupid");
        s_userid  = bData.getString("s_userid");
        s_name   = bData.getString("s_name");
        classid = bData.getString("classid");
        attendancedate = bData.getString("attendancedate");

        tvhUserid = findViewById(R.id.tvhUserid);
        tvhName = findViewById(R.id.tvhName);
        tvhGroupid = findViewById(R.id.tvhGroupid);
        tvhUserid.setText(s_userid);
        tvhName.setText(s_name);
        tvhGroupid.setText(s_groupid);
        tvUserid = findViewById(R.id.Userid);
        tvName = findViewById(R.id.tvName);
        tvAttendanceDate = findViewById(R.id.AttendanceDate);
        edtReason = (EditText) findViewById(R.id.edtReason);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
        btn_back = (Button) findViewById(R.id.btnBack);
        btn_back.setOnClickListener(this);
        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(this);

        this.setTitle(bData.getString("title"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfirm:
                newReason = edtReason.getText().toString();
                isEdit = false;
                if(type.equals("new")){
                    String[] datai = new String[]{"0",tvUserid.getText().toString(),s_userid,attendancedate,newReason,s_userid,s_login,s_groupid,s_name,"2",classid};
                    maintainData = new AttendanceMaintainData(ReturnOnAttendanceEditActivity.this,datai);
                    maintainData.execute("https://webhost2503.000webhostapp.com/classroom/returnonattendance/attendance_insert_leave.php");
                }else{
                    String[] datau = new String[]{queryId,tvUserid.getText().toString(),s_userid,attendancedate,newReason,s_userid,s_login,s_groupid,s_name,"2",classid};
//                    editData = new EditData(BulletinsEditActivity.this,datau);
                    maintainData = new AttendanceMaintainData(ReturnOnAttendanceEditActivity.this,datau);
                    maintainData.execute("https://webhost2503.000webhostapp.com/classroom/returnonattendance/attendance_update.php");
                }
                break;
            case R.id.btnBack:
                Intent i = new Intent(this, ReturnOnAttendanceMainActivity.class);
                intentPutExtraS();
                startActivity(i);
                break;
//            case R.id.btnSelect:
//                selectImageFromGallery();
//                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.returnonattendance_menu_edit, menu);
        MenuItem action_delete = menu.findItem(R.id.action_delete);
        if(type.equals("new"))
            action_delete.setVisible(false);
        else
            action_delete.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_back:
                intent = new Intent(ReturnOnAttendanceEditActivity.this, ReturnOnAttendanceMainActivity.class);
                intentPutExtraS();
                startActivity(intent);
                this.finish();
                break;
            case R.id.action_delete:
                AlertDialog dialog = null;
                AlertDialog.Builder builder = null;
                builder = new AlertDialog.Builder(this);
                builder.setTitle("刪除再次確認")
                        .setMessage(" 請確認是否刪除?")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] datad = new String[]{queryId,s_userid,s_login,s_groupid,s_name,"2",classid,attendancedate};
                                deleteData = new AttendanceDeleteData(ReturnOnAttendanceEditActivity.this,datad);
                                deleteData.execute("https://webhost2503.000webhostapp.com/classroom/returnonattendance/attendance_delete.php");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
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
