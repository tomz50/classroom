package com.classroom.communicationbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.classroom.home.MainActivity;
import com.classroom.home.R;

public class CommunicationBookTeacherInsertActivity extends AppCompatActivity implements View.OnClickListener {
    private CommunicationBookInsertData insertData;
    private Bundle bData;
    private String s_login,s_groupid,s_userid,s_name;
    private String classid,attendancedate;
    private TextView tvhUserid,tvhName,tvhGroupid;
    private TextView tvClassId, tvAttendanceDate;
    private EditText edtHomeWork,edtThContact;
    private Button btnConfirm,btn_back,btnSelect;
    private Intent intent;
    private String queryId,newHomeWork,newThContact;

    private int index;
    private boolean isEdit = false;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_book_teacher_insert);
        checkLogin();
        initView();
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
        s_login   = bData.getString("s_login");
        s_groupid = bData.getString("s_groupid");
        s_userid  = bData.getString("s_userid");
        s_name    = bData.getString("s_name");
        this.setTitle(bData.getString("title"));
        classid = bData.getString("classid");
        attendancedate = bData.getString("attendancedate");

        tvhUserid = findViewById(R.id.tvhUserid);
        tvhName = findViewById(R.id.tvhName);
        tvhGroupid = findViewById(R.id.tvhGroupid);
        tvhUserid.setText(s_userid);
        tvhName.setText(s_name);
        tvhGroupid.setText(s_groupid);

        tvClassId = findViewById(R.id.tvClassId);
        tvAttendanceDate = findViewById(R.id.tvAttendanceDate);
        tvClassId.setText(classid);
        tvAttendanceDate.setText(attendancedate);
        edtHomeWork = findViewById(R.id.edtHomeWork);
        edtThContact = findViewById(R.id.edtThContact);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfirm:
                newHomeWork = edtHomeWork.getText().toString();
                newThContact = edtThContact.getText().toString();
                isEdit = false;
                String[] datai = new String[]{classid,attendancedate,newHomeWork,newThContact,s_userid,s_login,s_groupid,s_name,"2"};
                insertData = new CommunicationBookInsertData(CommunicationBookTeacherInsertActivity.this,datai);
                insertData.execute("https://webhost2503.000webhostapp.com/classroom/communicationbook/communicationbook_insert.php");

                break;
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.returnonattendance_menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_back:
                intent = new Intent(this, CommunicationBookTeacherMainActivity.class);
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