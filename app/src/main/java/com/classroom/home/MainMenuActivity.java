package com.classroom.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.classroom.bulletinquery.BulletinQueryMainActivity;
import com.classroom.bulletins.BulletinsMainActivity;
import com.classroom.attendancerecord.AttendanceRecordMain;
import com.classroom.returnonattendance.ReturnOnAttendanceMainActivity;
import com.classroom.attendancequery.AttendanceQueryMainActivity;
import com.classroom.communicationbook.CommunicationBookTeacherMainActivity;
import com.classroom.communicationbook.CommunicationBookStudentMainActivity;
import com.classroom.communicationbook.CommunicationBookParentMainActivity;

import java.util.Calendar;
import java.util.Date;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{
    private Bundle bData;
    public DbAdapter dbAdapter;
    private String today;
    private Intent intent;
    private String s_login,s_groupid,s_userid,s_name;
    private String type;
    private TextView tvhUserid,tvhName,tvhGroupid;
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initView();
    }

    private void initView(){
        Date currentTime = Calendar.getInstance().getTime();
        //"yyyy-MM-dd hh:mm:ss"
        today = DateFormat.format("yyyy-MM-dd", currentTime).toString();
        dbAdapter = new DbAdapter(this);
        Button[] btns = {
                btn0 = findViewById(R.id.btn0),
                btn1 = findViewById(R.id.btn1),
                btn2 = findViewById(R.id.btn2),
                btn3 = findViewById(R.id.btn3),
                btn4 = findViewById(R.id.btn4),
                btn5 = findViewById(R.id.btn5),
                btn6 = findViewById(R.id.btn6),
                btn7 = findViewById(R.id.btn7),
                btn8 = findViewById(R.id.btn8),
        };
        for (Button btn : btns)
            btn.setOnClickListener(this);
        bData = this.getIntent().getExtras();
        if(bData == null || bData.getString("s_login").equals("false")){
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("s_login","false");
            startActivity(intent);
            this.finish();
        }
        else {
            s_login   = bData.getString("s_login");
            s_groupid = bData.getString("s_groupid");
            s_userid  = bData.getString("s_userid");
            s_name    = bData.getString("s_name");

            tvhUserid = findViewById(R.id.tvhUserid);
            tvhName = findViewById(R.id.tvhName);
            tvhGroupid = findViewById(R.id.tvhGroupid);
            tvhUserid.setText(s_userid);
            tvhName.setText(s_name);
            tvhGroupid.setText(s_groupid);
            if (s_groupid.equals("S") ){
                btn4.setVisibility(View.VISIBLE);
                btn6.setVisibility(View.VISIBLE);
            }
            if (s_groupid.equals("P") ){
                btn4.setVisibility(View.VISIBLE);
                btn7.setVisibility(View.VISIBLE);
            }
            if (s_groupid.equals("T") ){
                btn3.setVisibility(View.VISIBLE);
                btn4.setVisibility(View.VISIBLE);
                btn5.setVisibility(View.VISIBLE);
            }
            if (s_groupid.equals("C") ){
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.VISIBLE);
                btn4.setVisibility(View.VISIBLE);
            }
            if (s_groupid.equals("M") ){
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.VISIBLE);
                btn4.setVisibility(View.VISIBLE);
                btn5.setVisibility(View.VISIBLE);
                btn6.setVisibility(View.VISIBLE);
                btn7.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Log.i("!!MainActivity==>","onClick");
        intent = new Intent();
        if (s_login.equals("true")) {
            intent.putExtra("s_login",s_login);
            intent.putExtra("s_groupid",s_groupid);
            intent.putExtra("s_userid",s_userid);
            intent.putExtra("s_name",s_name);
            intent.putExtra("s_level","0");
        }
        switch(v.getId()){
            case R.id.btn0:
                changeView(this, BulletinQueryMainActivity.class);
                break;
            case R.id.btn1:
                changeView(this, BulletinsMainActivity.class);
                break;
            case R.id.btn2:
                changeView(this, AttendanceRecordMain.class);
                break;
            case R.id.btn3:
                changeView(this, ReturnOnAttendanceMainActivity.class);
                break;
            case R.id.btn4:
                changeView(this, AttendanceQueryMainActivity.class);
                break;
            case R.id.btn5:
                changeView(this, CommunicationBookTeacherMainActivity.class);
                break;
            case R.id.btn6:
                changeView(this, CommunicationBookStudentMainActivity.class);
                break;
            case R.id.btn7:
                changeView(this, CommunicationBookParentMainActivity.class);
                break;
            case R.id.btn8:
//                changeView(this, AttendanceQueryMainActivity.class);
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_logout:
                Log.v("DB_logout","logoi");
                s_login = "false";
//                s_groupid ="" ;
//                s_userid = "";
//                s_name =  "";
                long rowsAffected = dbAdapter.updateDataLogin(today, s_login, s_groupid, s_userid, s_name);
                if (rowsAffected == 1) {
                    Toast.makeText(this, "登出成功!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(this, MainActivity.class);
                    intent.putExtra("s_login","false");
                    startActivity(intent);
                    this.finish();
                }else
                    Toast.makeText(this, "登出失敗!", Toast.LENGTH_SHORT).show();
//                boolean returncd = dbAdapter.deleteDataLogout(today);
                Log.v("DB_logout","logoo");
//                if (returncd == true) {
//                    intent = new Intent(this, MainActivity.class);
//                    intent.putExtra("s_login","false");
//                    startActivity(intent);
//                    this.finish();
//                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //intent跳轉activity函式
    public void changeView(Context context, Class<?> cla){
        intent = intent.setClass(context, cla);
        startActivity(intent);
//        this.finish();
    }
}
