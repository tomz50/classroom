package com.classroom.bulletinquery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.classroom.bulletins.BulletinList;
import com.classroom.bulletins.BulletinListAdapter;
import com.classroom.bulletins.BulletinListLoading;
import com.classroom.bulletins.BulletinsEditActivity;
import com.classroom.bulletins.BulletinsMainActivity;
import com.classroom.home.MainActivity;
import com.classroom.home.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BulletinQueryMainActivity extends AppCompatActivity {
//    public static boolean login = false;
    private Bundle bData;
    private String s_login,s_groupid,s_userid,s_name;
    private TextView tvhUserid,tvhName,tvhGroupid;
    private Intent intent;
    private ListView bulletinList;
    private BulletinListAdapter adapter;
    private BulletinListLoading bulletinListLoading;
    List<BulletinList> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_query_main);
//        login = true;
        initView();
        bulletinList = findViewById(R.id.listview);
        bulletinListLoading = new BulletinListLoading(this);

        try {
            data_list = bulletinListLoading.execute("https://webhost2503.000webhostapp.com/classroom/bulletins/bulletin_querylist.php").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter = new BulletinListAdapter(this,data_list);
        bulletinList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bulletinList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent();
                intentPutExtraS();
                intent.putExtra("id", data_list.get(position).getId());
                intent.putExtra("title", "公告查詢");
                changeView(BulletinQueryMainActivity.this, BulletinQueryActivity.class);
//                intent.setClass(BulletinQueryMainActivity.this, BulletinQueryActivity.class);
//                Log.i("subject=",data_list.get(position).getSubject());
//                startActivity(intent);
            }
        });
    }

    private void initView(){
        this.setTitle("佈告欄查詢");
        bData = this.getIntent().getExtras();
        if(bData == null || bData.getString("s_login").equals("false")){
            s_login = "false";
        }
        else {
            s_login   = bData.getString("s_login");
            s_groupid = bData.getString("s_groupid");
            s_userid  = bData.getString("s_userid");
            s_name   = bData.getString("s_name");

            tvhUserid = findViewById(R.id.tvhUserid);
            tvhName = findViewById(R.id.tvhName);
            tvhGroupid = findViewById(R.id.tvhGroupid);
            tvhUserid.setText(s_userid);
            tvhName.setText(s_name);
            tvhGroupid.setText(s_groupid);
        }
    }

    private void intentPutExtraS(){
        if(bData == null || bData.getString("s_login").equals("false")){
            intent.putExtra("s_login","false");
        }
        else{
            intent.putExtra("s_login",s_login);
            intent.putExtra("s_groupid",s_groupid);
            intent.putExtra("s_userid",s_userid);
            intent.putExtra("s_name",s_name);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch(id){
            case R.id.action_back:
                intent = new Intent(this, MainActivity.class);
                intentPutExtraS();
                startActivity(intent);
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //intent跳轉activity函式
    public void changeView(Context context, Class<?> cla){

        intent = intent.setClass(context, cla);
        startActivity(intent);
        this.finish();
    }
}
