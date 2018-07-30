package com.classroom.attendancerecord;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InsertData extends AsyncTask<String, Void, List<AttendanceInsertFeeback>> {

    private ProgressDialog dialog ;
    Context c;
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";
    String[] getData;

    //public BulletinInsertData(Context context) {
    public InsertData(Context context, String[] data) {
        this.c = context;
        getData = data;
        this.dialog = new ProgressDialog(c);//
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("連線中...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<AttendanceInsertFeeback> attendanceInsertFeebacks) {
        super.onPostExecute(attendanceInsertFeebacks);
        dialog.dismiss();
//        int isSuccess;
//        isSuccess =s.indexOf("Insert Success");
//        if(isSuccess != -1)
//            Toast.makeText(c,"新增成功", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(c,AttendanceRecordMain.class);
//        intent.putExtra("s_userid",getData[2]);
//        intent.putExtra("s_login",getData[3]);
//        intent.putExtra("s_groupid",getData[4]);
//        intent.putExtra("s_name",getData[5]);
//        c.startActivity(intent);
    }

    @Override
    protected List<AttendanceInsertFeeback> doInBackground(String... strings) {
        List<AttendanceInsertFeeback> result = new ArrayList<AttendanceInsertFeeback>();
        URL u = null;
        try {
            //設定連線格式
            u = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //設定檔頭相關屬性
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);
            conn.setRequestProperty("Charset", "UTF-8");



            //宣告一個串流物件
            DataOutputStream request = new DataOutputStream(conn.getOutputStream());

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"Userid\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(getData[0]);
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"AttendanceId\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(getData[1]);
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

            request.flush();
            //close DataOutputStream
            Log.i("string====>", request.toString());
            request.close();
            //
            conn.connect();

            InputStream is = conn.getInputStream();
            // Read the stream
            byte[] b = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (is.read(b) != -1)
                baos.write(b);
            String JSONResp = new String(baos.toByteArray());
            Log.i("JSONResp=", JSONResp);
            JSONArray array = new JSONArray(JSONResp);
            for(int i = 0; i < array.length(); i++){
                if(array.getJSONObject(i) != null){
                    //將資料加入ArrayList
                    result.add(convertData(array.getJSONObject(i)));
                    Log.i("DBdatagetJSONObject=",convertData(array.getJSONObject(i)).toString());
                }
            }

//            String response = new String(baos.toByteArray());
//            Log.i("post-data=", response);
//            return response;

        }catch (Exception e){
            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
        }
//        return null;
        return result;
    }
    //將資料對應到Contact類別格式
    private AttendanceInsertFeeback convertData(JSONObject obj) throws JSONException {
        String name = obj.getString("Name");
        String attendancetime = obj.getString("AttendanceTime");
        Log.v("jsonObj=",obj.getString("Name").toString());

        return new AttendanceInsertFeeback(name, attendancetime);
    }
}




//
//            //讀取網頁上的資料
//            InputStream is = conn.getInputStream();
//            // Read the stream
//            byte[] b = new byte[1024];
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            while ( is.read(b) != -1)
//                baos.write(b);
//            String JSONResp = new String(baos.toByteArray());
//            Log.i("JSONResp=", JSONResp);
//            JSONArray array = new JSONArray(JSONResp);
//            for(int i = 0; i < array.length(); i++){
//                if(array.getJSONObject(i) != null){
//                    //將資料加入ArrayList
//                    result.add(convertContact(array.getJSONObject(i)));
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return result;
//    }
//    //將資料對應到Contact類別格式

