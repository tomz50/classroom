package com.classroom.bulletins;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BulletinListLoading extends AsyncTask<String, Void, List<BulletinList>> {
    Context c;
    private ProgressDialog dialog ;
    public BulletinListLoading(Context context) {
        this.c = context;
        dialog = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("資料下載中...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<BulletinList> contacts) {
        super.onPostExecute(contacts);
        dialog.dismiss();
    }

    @Override
    protected List<BulletinList> doInBackground(String... strings) {
        List<BulletinList> result = new ArrayList<BulletinList>();
        URL u = null;
        try{
            u = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            //讀取網頁上的資料
            InputStream is = conn.getInputStream();
            // Read the stream
            byte[] b = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ( is.read(b) != -1)
                baos.write(b);
            String JSONResp = new String(baos.toByteArray());
            Log.i("JSONResp=", JSONResp);
            JSONArray array = new JSONArray(JSONResp);
            for(int i = 0; i < array.length(); i++){
                if(array.getJSONObject(i) != null){
                    //將資料加入ArrayList
                    result.add(convertContact(array.getJSONObject(i)));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    //將資料對應到Contact類別格式
    private BulletinList convertContact(JSONObject obj) throws JSONException {

        String id = obj.getString("id");
        String subject = obj.getString("Subject");
        String implementationdate = obj.getString("ImplementationDate");
        String effectivedate = obj.getString("EffectiveDate");
        Log.v("jsonObj=",obj.getString("Subject").toString());

        return new BulletinList( id, subject, implementationdate, effectivedate);

    }
}
