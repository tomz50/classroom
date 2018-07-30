package com.classroom.communicationbook;

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

public class CommunicationBookListLoading extends AsyncTask<String, Void, List<CommunicationBookList>>  {
    Context c;
    private String classId,attendanceDate,userid;
    private ProgressDialog dialog ;
    public CommunicationBookListLoading(Context context, String classId, String attendanceDate,String userid) {
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
    protected void onPostExecute(List<CommunicationBookList> attendances) {
        super.onPostExecute(attendances);
        dialog.dismiss();
    }

    @Override
    protected List<CommunicationBookList> doInBackground(String... strings) {
        List<CommunicationBookList> result = new ArrayList<CommunicationBookList>();
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
                    result.add(convertCommunicationBook(array.getJSONObject(i)));
                    Log.i("DBdatagetJSONObject=",convertCommunicationBook(array.getJSONObject(i)).toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    //將資料對應到Contact類別格式
    private CommunicationBookList convertCommunicationBook(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String stuserid = obj.getString("StUserid");
        String name = obj.getString("Name");
        String thtime = obj.getString("ThTime");
        String sttime = obj.getString("StTime");
        String prtime = obj.getString("PrTime");
        Log.v("jsonObj=",obj.getString("Name").toString());

        return new CommunicationBookList(id,stuserid, name, thtime, sttime, prtime);
    }
}
