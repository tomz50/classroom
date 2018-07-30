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

public class AttendanceListLoading extends AsyncTask<String, Void, List<AttendanceList>> {
    Context c;
    private String classId,attendanceDate,userid;
    private ProgressDialog dialog ;
    public AttendanceListLoading(Context context, String classId, String attendanceDate,String userid) {
        this.c = context;
        this.classId = classId;
        this.attendanceDate = attendanceDate;
        this.userid = userid;
        dialog = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("資料下載中...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<AttendanceList> attendances) {
        super.onPostExecute(attendances);
        dialog.dismiss();
    }

    @Override
    protected List<AttendanceList> doInBackground(String... strings) {
        List<AttendanceList> result = new ArrayList<AttendanceList>();
        URL u = null;
        try{
            u = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            String data = "ClassId=" + URLEncoder.encode(classId, "UTF-8");
            data += "&AttendanceDate=" + URLEncoder.encode(attendanceDate, "UTF-8");
            data += "&Userid=" + URLEncoder.encode(userid, "UTF-8");
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
            byte[] b = new byte[4096];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ( is.read(b) != -1)
                baos.write(b);
            String JSONResp = new String(baos.toByteArray());
            Log.v("!!JSONResp=", JSONResp);
            JSONArray array = new JSONArray(JSONResp);
            Log.v("!!array.length","==>"+String.valueOf(array.length()));
            for(int i = 0; i < array.length(); i++){
                if(array.getJSONObject(i) != null){
                    //將資料加入ArrayList
                    result.add(convertAttendance(array.getJSONObject(i)));
                    Log.v("!!DBdatagetJSONObject=",convertAttendance(array.getJSONObject(i)).toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    //將資料對應到Contact類別格式
    private AttendanceList convertAttendance(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String userid = obj.getString("Userid");
        String name = obj.getString("Name");
        String attendancetime_i = obj.getString("AttendanceTime_i");
        String attendanceTime_o = obj.getString("AttendanceTime_o");
        String trtime = obj.getString("TrTime");
        String attendancedate = obj.getString("AttendanceDate");
        String reason = obj.getString("Reason");
        Log.v("jsonObj=",obj.getString("Name").toString());

        return new AttendanceList(id,userid, name, attendancetime_i, attendanceTime_o, trtime,attendancedate,reason);
    }
}
