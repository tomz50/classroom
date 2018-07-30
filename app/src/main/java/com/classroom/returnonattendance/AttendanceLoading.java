package com.classroom.returnonattendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

public class AttendanceLoading extends AsyncTask<String, Void, List<Attendance>> {
    Context c;
    private String whereKey;
    private ProgressDialog dialog ;
    public AttendanceLoading(Context context, String whereKey) {
        this.c = context;
        this.whereKey = whereKey;
        dialog = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("資料下載中...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<Attendance> attendances) {
        super.onPostExecute(attendances);
        dialog.dismiss();
    }

    @Override
    protected List<Attendance> doInBackground(String... strings) {
        List<Attendance> result = new ArrayList<Attendance>();
        URL u = null;
        try{
            u = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            String data = "id=" + URLEncoder.encode(whereKey, "UTF-8");
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
            Log.i("JSONResp=", JSONResp);
            JSONArray array = new JSONArray(JSONResp);
            for(int i = 0; i < array.length(); i++){
                if(array.getJSONObject(i) != null){
                    //將資料加入ArrayList
                    result.add(convertAttendance(array.getJSONObject(i)));
                    Log.i("DBdatagetJSONObject=",convertAttendance(array.getJSONObject(i)).toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    //將資料對應到Contact類別格式
    private Attendance convertAttendance(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String userid = obj.getString("Userid");
        String name = obj.getString("Name");
        String attendanceid = obj.getString("AttendanceId");
        String attendancedate = obj.getString("AttendanceDate");
        String attendancetime = obj.getString("AttendanceTime");
        String truserid = obj.getString("TrUserid");
        String trtime = obj.getString("TrTime");
        String parentreply = obj.getString("ParentReply");
        String reason = obj.getString("Reason");
        Log.v("jsonObj=",obj.getString("Name").toString());

        return new Attendance(id,userid, name,attendanceid,attendancedate, attendancetime, truserid, trtime,parentreply,reason);
    }
}

