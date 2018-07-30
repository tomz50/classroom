package com.classroom.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.classroom.home.R;
import com.classroom.bulletinquery.BulletinQueryMainActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public DbAdapter dbAdapter;
    private String today;
    private TextView tvVisitdt,tvLogin,tvGroupid,tvUserid,tvName;
    private Bundle bData;
    private String s_login,s_groupid,s_userid,s_name;
    private String sl_visitdt,sl_login,sl_groupid,sl_userid,sl_name;
    private Intent intent;
    private LoginChkUsers loginChkUsers;
    List<Users> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){

        tvVisitdt = findViewById(R.id.tvVisitdt);
        tvLogin = findViewById(R.id.tvLogin);
        tvGroupid = findViewById(R.id.tvGroupid);
        tvUserid = findViewById(R.id.tvUserid);
        tvName = findViewById(R.id.tvName);
        dbAdapter = new DbAdapter(this);
        Log.v("DB_insert0","cursor");
        Date currentTime = Calendar.getInstance().getTime();
        today = DateFormat.format("yyyy-MM-dd", currentTime).toString();
        Cursor cursor = dbAdapter.queryByVisitdt(today);
        Log.v("DB_Count","DB_Count"+String.valueOf(cursor.getCount()));
//        index = cursor.getInt(0);
        if(cursor != null && cursor.getCount() > 0){
            Log.v("DB_insert1","cursorNotnull");
            sl_visitdt = cursor.getString(0);
            sl_login = cursor.getString(1);
            sl_groupid = cursor.getString(2);
            sl_userid = cursor.getString(3);
            sl_name = cursor.getString(4);
            tvVisitdt.setText(sl_visitdt);
            tvLogin.setText(sl_login);
            tvGroupid.setText(sl_groupid);
            tvUserid.setText(sl_userid);
            tvName.setText(sl_name);
            if(sl_login.equals("true")){
                intent = new Intent(MainActivity.this, MainMenuActivity.class);
                intent.putExtra("s_login", sl_login);
                intent.putExtra("s_groupid", sl_groupid);
                intent.putExtra("s_userid", sl_userid);
                intent.putExtra("s_name", sl_name);
                startActivity(intent);
                MainActivity.this.finish();
            }else{
                loginDialog();
            }
        }else{
            try{
                Log.v("DB_insert1","cursorNull");
                dbAdapter.insertDataNotLogin(today);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                //回到列表
                intent = new Intent(this, BulletinQueryMainActivity.class);
                intent.putExtra("s_login","false");
                startActivity(intent);
                this.finish();
            }
        }
    }

    public void loginDialog(){
        // Create Object of Dialog class
        final Dialog loginDialog = new Dialog(this);
        // Set GUI of login screen
        loginDialog.setContentView(R.layout.login_dialog);
        loginDialog.setTitle("Login Dialog");

        // Init button of login GUI
        Button btnLogin = loginDialog.findViewById(R.id.btnLogin);
        Button btnCancel = loginDialog.findViewById(R.id.btnCancel);
        final EditText edtUserid = loginDialog.findViewById(R.id.edtUserid);
        final EditText edtPassword = loginDialog.findViewById(R.id.edtPassword);

        // Attached listener for login GUI button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUserid.getText().toString().trim().length() > 0 && edtPassword.getText().toString().trim().length() > 0) {
                    // Validate Your login credential here than display message
                    loginChkUsers = new LoginChkUsers(MainActivity.this,edtUserid.getText().toString().trim(),edtPassword.getText().toString().trim());
//                    loginChkUsers = new LoginChkUsers(MainActivity.this,"S00001","1234");
                    try {
                        data_list = loginChkUsers.execute("https://webhost2503.000webhostapp.com/classroom/home/users_login.php").get();
                        Log.v("!!","==>"+data_list+"<==");
                        if(data_list.toString() == "[]" ){
                            Toast.makeText(MainActivity.this,"登入失敗!!", Toast.LENGTH_LONG).show();
                        }else {
                            Log.v("!!edtuserid","==>"+edtUserid.getText().toString().trim()+"<==");
                            Log.v("!!userid","==>"+data_list.get(0).getUserid().toString()+"<==");
                            if (edtUserid.getText().toString().trim().equals(data_list.get(0).getUserid().toString()) ) {
//                                Toast.makeText(MainActivity.this, "登入成功!!", Toast.LENGTH_LONG).show();
                                loginDialog.dismiss();
                                dbAdapter = new DbAdapter(MainActivity.this);
                                try{
                                    Log.i("DB_EditActivity","onClick1");
                                    s_login = "true";
                                    s_groupid = data_list.get(0).getGroupid();
                                    s_userid = data_list.get(0).getUserid();
                                    s_name =  data_list.get(0).getName();
                                    long rowsAffected = dbAdapter.updateDataLogin(today, s_login, s_groupid, s_userid, s_name);
                                    if (rowsAffected == 1) {
                                        tvVisitdt.setText(today);
                                        tvLogin.setText(s_login);
                                        tvGroupid.setText(s_groupid);
                                        tvUserid.setText(s_userid);
                                        tvName.setText(s_name);
                                        intent = new Intent(MainActivity.this, MainMenuActivity.class);
                                        intent.putExtra("s_login", s_login);
                                        intent.putExtra("s_groupid", s_groupid);
                                        intent.putExtra("s_userid", s_userid);
                                        intent.putExtra("s_name", s_name);
                                        Toast.makeText(MainActivity.this, "登入成功!", Toast.LENGTH_SHORT).show();
                                        Log.v("DB_MainActivity","LoginOk");
                                        startActivity(intent);
                                        MainActivity.this.finish();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "登入失敗!SL", Toast.LENGTH_SHORT).show();
                                        s_login = "false";;
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }finally {
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    // Redirect to dashboard / home screen.
//                    intent = new Intent(MainActivity.this, MainMenuActivity.class);
//                    intent.putExtra("s_login","true");
//                    intent.putExtra("s_groupid","M");
//                    intent.putExtra("s_userid","M0099");
//                    intent.putExtra("s_name","管理者");
//                    startActivity(intent);
//                    loginDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this,
                            "請輸入 帳號 及 密碼 !!", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
                MainActivity.this.finish();
            }
        });
        // Make dialog box visible.
        loginDialog.show();
    }

    //intent跳轉activity函式
    public void changeView(Context context, Class<?> cla){
        intent = intent.setClass(context, cla);
        startActivity(intent);
        this.finish();
    }
}
