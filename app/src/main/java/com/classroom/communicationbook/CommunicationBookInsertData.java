package com.classroom.communicationbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class CommunicationBookInsertData extends AsyncTask<String, Void, String> { private ProgressDialog dialog ;
    Context c;
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";
    String[] getData;

    //public BulletinInsertData(Context context) {
    public CommunicationBookInsertData(Context context, String[] data) {
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
    protected String doInBackground(String... strings) {
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
            request.writeBytes("Content-Disposition: form-data; name=\"ClassId\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(getData[0]);
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"AttendanceDate\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(getData[1]);
            request.writeBytes(crlf);
            //---------------HomeWork------------------------
            request.writeBytes(this.twoHyphens + this.boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"HomeWork\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(URLEncoder.encode(getData[2], "UTF-8"));
            request.writeBytes(crlf);
            //---------------ThContact------------------------
            request.writeBytes(this.twoHyphens + this.boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"ThContact\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(URLEncoder.encode(getData[3], "UTF-8"));
            request.writeBytes(crlf);
            //---------------ThContact------------------------
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"Userid\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(getData[4]);
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

            String response = new String(baos.toByteArray());
            Log.i("post-data=", response);
            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        //return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        int isSuccess;
        String msg;
        isSuccess =s.indexOf("Insert Success");
        msg = "新增成功";
        if (isSuccess != -1){
            Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(c,CommunicationBookTeacherMainActivity.class);
            intent.putExtra("s_userid",getData[4]);
            intent.putExtra("s_login",getData[5]);
            intent.putExtra("s_groupid",getData[6]);
            intent.putExtra("s_name",getData[7]);
            intent.putExtra("s_level",getData[8]);
            intent.putExtra("classid",getData[0]);
            intent.putExtra("attendancedate",getData[1]);
            c.startActivity(intent);
        }
    }
}
