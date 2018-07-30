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

public class CommunicationBookDeleteData extends AsyncTask<String, Void, String> {
    private ProgressDialog dialog ;
    Context c;
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";
    String[] getData;

    public CommunicationBookDeleteData(Context context, String[] data) {
        this.c = context;
        getData = data;
        this.dialog = new ProgressDialog(c);
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
            request.writeBytes("Content-Disposition: form-data; name=\"id\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(getData[0]);
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
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("連線中...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        int isSuccess;
        isSuccess =s.indexOf("Delete Success");
        Log.i("DBisSuccess1=","s="+s+"=s");
        Log.i("DBisSuccess2=","DD"+ String.valueOf(isSuccess));
        if(isSuccess != -1) {
            Toast.makeText(c, "刪除成功", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(c,CommunicationBookTeacherMainActivity.class);
            intent.putExtra("s_userid",getData[1]);
            intent.putExtra("s_login",getData[2]);
            intent.putExtra("s_groupid",getData[3]);
            intent.putExtra("s_name",getData[4]);
            c.startActivity(intent);
        }
    }
}
