package com.classroom.bulletinquery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import com.classroom.bulletins.BulletinLoading;
import com.classroom.home.MainActivity;
import com.classroom.home.MainMenuActivity;
import com.classroom.home.R;

public class BulletinQueryActivity extends AppCompatActivity {

    private Bundle bData;
    private String s_login,s_groupid,s_userid,s_name;
    private Button btnBack;
    private TextView tvhUserid,tvhName,tvhGroupid;
    private TextView tvSubject, tvDesciption, tvImplementationDate, tvEffectiveDate;
    private Intent intent;
    private String type, queryId;
    private BulletinLoading bulletinLoading;
    //private ChangeData changeData;  //新增與編輯送出資料合併類別
    private String[] old_data = new String[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_query);
        initView();

        bulletinLoading = new BulletinLoading(this , queryId);
        try {
            old_data = bulletinLoading.execute("https://webhost2503.000webhostapp.com/classroom/bulletins/bulletin_query.php").get();
            tvSubject.setText(old_data[1]);
            tvDesciption.setText(old_data[2]);
            tvImplementationDate.setText(old_data[3]);
            tvEffectiveDate.setText(old_data[4]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        bData = this.getIntent().getExtras();
        this.setTitle(bData.getString("title"));
        queryId   = bData.getString("id");
        if(bData == null || bData.getString("s_login").equals("false")){
            s_login = "false";
        }
        else {
            s_login   = bData.getString("s_login");
            s_groupid = bData.getString("s_groupid");
            s_userid  = bData.getString("s_userid");
            s_name   = bData.getString("s_name");
        }

        tvSubject = findViewById(R.id.tvSubject);
        tvDesciption = findViewById(R.id.tvDesciption);
        tvImplementationDate = findViewById(R.id.tvImplementationDate);
        tvEffectiveDate = findViewById(R.id.tvEffectiveDate);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMain();
            }
        });

        tvhUserid = findViewById(R.id.tvhUserid);
        tvhName = findViewById(R.id.tvhName);
        tvhGroupid = findViewById(R.id.tvhGroupid);
        tvhUserid.setText(s_userid);
        tvhName.setText(s_name);
        tvhGroupid.setText(s_groupid);
    }

    @Override
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
                backMain();
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void backMain(){
        intent = new Intent(BulletinQueryActivity.this, BulletinQueryMainActivity.class);
        intentPutExtraS();
        startActivity(intent);

        this.finish();
    }

    private void intentPutExtraS(){
        intent.putExtra("s_login",s_login);
        intent.putExtra("s_groupid",s_groupid);
        intent.putExtra("s_userid",s_userid);
        intent.putExtra("s_name",s_name);
    }
}

