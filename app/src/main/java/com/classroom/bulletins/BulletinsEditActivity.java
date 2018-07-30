package com.classroom.bulletins;

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
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import com.classroom.home.MainActivity;
import com.classroom.home.R;

public class BulletinsEditActivity extends AppCompatActivity {

    private TextView tvhUserid,tvhName,tvhGroupid;
    private EditText edtid, edtSubject, edtDesciption, edtImplementationDate, edtEffectiveDate;
    private String newSubject, newDesciption, newImplementationDate, newEffectiveDate;
    private Button btnOK;
    private Intent intent;
    private Bundle bData;
    private String s_login,s_groupid,s_userid,s_name;
    private String type, queryId;
    private BulletinInsertData insertData;
    private BulletinLoading loadEditData;
    private BulletinUpdateData updateData;
    private BulletinDeleteData deleteData;
    //private ChangeData changeData;  //新增與編輯送出資料合併類別
    private String[] old_data = new String[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletins_edit);
        initView();

        if(type.equals("edit")){
            queryId = bData.getString("id");
            Log.i("id=",queryId);
            loadEditData = new BulletinLoading(this , queryId);
            try {
                old_data = loadEditData.execute("https://webhost2503.000webhostapp.com/classroom/bulletins/bulletin_query.php").get();
                edtSubject.setText(old_data[1]);
                edtDesciption.setText(old_data[2]);
                edtImplementationDate.setText(old_data[3]);
                edtEffectiveDate.setText(old_data[4]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    private void initView(){
        bData = this.getIntent().getExtras();
        if(bData == null || bData.getString("s_login").equals("false")){
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("s_login","false");
            startActivity(intent);
            this.finish();
        }
        s_login   = bData.getString("s_login");
        s_groupid = bData.getString("s_groupid");
        s_userid  = bData.getString("s_userid");
        s_name   = bData.getString("s_name");

        this.setTitle(bData.getString("title"));
        type = bData.getString("type");

        tvhUserid = findViewById(R.id.tvhUserid);
        tvhName = findViewById(R.id.tvhName);
        tvhGroupid = findViewById(R.id.tvhGroupid);
        tvhUserid.setText(s_userid);
        tvhName.setText(s_name);
        tvhGroupid.setText(s_groupid);

        edtid = findViewById(R.id.id);
        edtSubject = findViewById(R.id.edtSubject);
        edtDesciption = findViewById(R.id.edtDesciption);
        edtImplementationDate = findViewById(R.id.edtImplementationDate);
        edtEffectiveDate = findViewById(R.id.edtEffectiveDate);
        btnOK = findViewById(R.id.btnConfirm);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newSubject = edtSubject.getText().toString();
                newDesciption = edtDesciption.getText().toString();
                newImplementationDate = edtImplementationDate.getText().toString();
                newEffectiveDate = edtEffectiveDate.getText().toString();
                if(type.equals("new")) {
                    Log.i("subject=",newSubject);
                    String[] datai = new String[]{newSubject, newDesciption, newImplementationDate, newEffectiveDate,s_userid,s_login,s_groupid,s_name};
                    insertData = new BulletinInsertData(BulletinsEditActivity.this, datai);
                    insertData.execute("https://webhost2503.000webhostapp.com/classroom/bulletins/bulletin_insert.php");
                    //changeData = new ChangeData(BulletinsEditActivity.this, data, type);
                    //changeData.execute("https://tomz.000webhostapp.com/bulletin1/insert.php");
                }else{
                    String[] datau = new String[]{queryId,newSubject, newDesciption, newImplementationDate, newEffectiveDate,s_userid,s_login,s_groupid,s_name};
                    updateData = new BulletinUpdateData(BulletinsEditActivity.this, datau);

                    updateData.execute("https://webhost2503.000webhostapp.com/classroom/bulletins/bulletin_update.php");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bulletins_menu_edit, menu);
        MenuItem action_delete = menu.findItem(R.id.action_delete);
        if(type.equals("new"))
            action_delete.setVisible(false);
        else
            action_delete.setVisible(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.action_back:
                intent = new Intent(BulletinsEditActivity.this, BulletinsMainActivity.class);
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
                                String[] datad = new String[]{queryId,s_userid,s_login,s_groupid,s_name};
                                deleteData = new BulletinDeleteData(BulletinsEditActivity.this, datad);
                                deleteData.execute("https://webhost2503.000webhostapp.com/classroom/bulletins/bulletin_delete.php");
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
}

