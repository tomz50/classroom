package com.classroom.bulletins;

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

import com.classroom.home.MainActivity;
import com.classroom.home.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BulletinsMainActivity extends AppCompatActivity {
    private Bundle bData;
    private Intent intent;
    private String s_login,s_groupid,s_userid,s_name;
    private TextView tvhUserid,tvhName,tvhGroupid;
    private ListView bulletinList;
    private BulletinListAdapter adapter;
    private BulletinListLoading bulletinListLoading;
    List<BulletinList> data_list;

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletins_main);
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
                intent.putExtra("title","編輯公告");
                intent.putExtra("type","edit");
                intent.putExtra("id", data_list.get(position).getId());
                changeView(BulletinsMainActivity.this, BulletinsEditActivity.class);
//                intent.setClass(BulletinsMainActivity.this, BulletinsEditActivity.class);
//                Log.i("subject=",data_list.get(position).getSubject());
//                startActivity(intent);
            }
        });
    }

    private void initView(){
        this.setTitle("佈告欄維護");
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
            s_name   = bData.getString("s_name");

            tvhUserid = findViewById(R.id.tvhUserid);
            tvhName = findViewById(R.id.tvhName);
            tvhGroupid = findViewById(R.id.tvhGroupid);
            tvhUserid.setText(s_userid);
            tvhName.setText(s_name);
            tvhGroupid.setText(s_groupid);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bulletins_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        intent = new Intent();
        intentPutExtraS();
        switch(id){
            case R.id.action_back:
                changeView(this, MainActivity.class);
                break;
            case R.id.action_add:
                intent.putExtra("title","新增公告");
                intent.putExtra("type","new");
                changeView(this, BulletinsEditActivity.class);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void intentPutExtraS(){
        intent.putExtra("s_login",s_login);
        intent.putExtra("s_groupid",s_groupid);
        intent.putExtra("s_userid",s_userid);
        intent.putExtra("s_name",s_name);
    }
    //intent跳轉activity函式
    public void changeView(Context context, Class<?> cla){

        intent = intent.setClass(context, cla);
        startActivity(intent);
        this.finish();
    }
}
