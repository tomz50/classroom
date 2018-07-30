package com.classroom.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.classroom.returnonattendance.ReturnOnAttendanceMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LoginChkUsers extends AsyncTask<String, Void, List<Users>> {
    Context c;
    private String userid;
    private String password;
    private ProgressDialog dialog ;
    public LoginChkUsers(Context context, String userid,String password) {
        this.c = context;
        this.userid = userid;
        this.password = password;
        dialog = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("資料下載中...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<Users> users) {
        super.onPostExecute(users);
        dialog.dismiss();
    }

    @Override
    protected List<Users> doInBackground(String... strings) {
        List<Users> result = new ArrayList<Users>();
        URL u = null;
        try{
            u = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            String data = "Userid=" + URLEncoder.encode(userid, "UTF-8");
            data += "&Password=" + URLEncoder.encode(password, "UTF-8");
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            os.close();
//            conn.setRequestMethod("GET");
            conn.connect();
            //讀取網頁上的資料
            InputStream is = conn.getInputStream();
            // Read the stream
            byte[] b = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ( is.read(b) != -1)
                baos.write(b);
            String JSONResp = new String(baos.toByteArray());
            Log.v("JSONResp=", JSONResp);
            JSONArray array = new JSONArray(JSONResp);
            for(int i = 0; i < array.length(); i++){
                if(array.getJSONObject(i) != null){
                    //將資料加入ArrayList
                    result.add(convertUsers(array.getJSONObject(i)));
                    Log.v("DBdatagetJSONObject=",convertUsers(array.getJSONObject(i)).toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    //將資料對應到Users類別格式
    private Users convertUsers(JSONObject obj) throws JSONException {
//        String id = obj.getString("id");
        String Groupid = obj.getString("Groupid");
        String Userid = obj.getString("Userid");
//        String Password = obj.getString("Password");
        String Name = obj.getString("Name");
//        String EffectivedateS = obj.getString("EffectivedateS");
//        String EffectivedateE = obj.getString("EffectivedateE");
        Log.v("!!jsonObj=",obj.getString("Name").toString());

        return new Users(Groupid, Userid,Name);
    }
}

