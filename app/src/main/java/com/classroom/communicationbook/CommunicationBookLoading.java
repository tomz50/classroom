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

public class CommunicationBookLoading extends AsyncTask<String, Void, List<CommunicationBook>>  {
    Context c;
    private String id;
    private ProgressDialog dialog ;
    public CommunicationBookLoading(Context context, String id) {
        this.c = context;
        this.id = id;
        dialog = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("資料下載中...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<CommunicationBook> attendances) {
        super.onPostExecute(attendances);
        dialog.dismiss();
    }

    @Override
    protected List<CommunicationBook> doInBackground(String... strings) {
        List<CommunicationBook> result = new ArrayList<CommunicationBook>();
        URL u = null;
        try{
            u = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            String data = "id=" + URLEncoder.encode(id, "UTF-8");
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
    //將資料對應到CommunicationBook類別格式
    private CommunicationBook convertCommunicationBook(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String classid = obj.getString("ClassId");
        String attendancedate = obj.getString("AttendanceDate");
        String stuserid = obj.getString("StUserid");
        String name = obj.getString("Name");
        String homework = obj.getString("HomeWork");
        String thcontact = obj.getString("ThContact");
        String prcontact = obj.getString("PrContact");
        String thtime = obj.getString("ThTime");
        String sttime = obj.getString("StTime");
        String prtime = obj.getString("PrTime");
        String thuserid = obj.getString("ThUserid");
        String pruserid = obj.getString("PrUserid");
        Log.v("jsonObj=",obj.getString("Name").toString());

        return new CommunicationBook(id,classid,attendancedate,stuserid, name,homework, thcontact, prcontact, thtime,sttime,prtime,thuserid,pruserid);
    }
}

