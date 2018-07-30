package com.classroom.bulletins;

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

public class BulletinUpdateData extends AsyncTask<String, Void, String> {
    private ProgressDialog dialog;
    Context c;
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";
    String[] getData;
    public BulletinUpdateData(Context context, String[] data) {
        this.c = context;
        this.getData = data;
        dialog = new ProgressDialog(c);
    }
    @Override
    protected String doInBackground(String... strings) {
        URL u = null;


        try {
            u = new URL(strings[0]);
            //設定連線格式
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

            //????????????????????????????????????// ContactID

            //---------------ID------------------------
            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"id\"" + "\"" + this.crlf);
            request.writeBytes(this.crlf);
            request.writeBytes(getData[0]);
            request.writeBytes(this.crlf);
            //---------------subject------------------------
            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"Subject\"" + "\"" + this.crlf);
            request.writeBytes(this.crlf);
            request.writeBytes(URLEncoder.encode(getData[1], "UTF-8"));
            request.writeBytes(this.crlf);
            //---------------desciption------------------------
            request.writeBytes(this.twoHyphens + this.boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"Desciption\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(URLEncoder.encode(getData[2], "UTF-8"));
            request.writeBytes(crlf);
            //---------------implementationdate---------------------------
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"ImplementationDate\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(getData[3]);
            request.writeBytes(crlf);
            //---------------effectivedate---------------------------
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"EffectiveDate\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(getData[4]);
            request.writeBytes(crlf);
            //---------------UpdatedUserid---------------------------
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"UpdatedUserid\"" + "\"" + crlf);
            request.writeBytes(crlf);
            request.writeBytes(getData[5]);
            request.writeBytes(crlf);
            //-------------------------------------------------
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            request.flush();
            //關閉DataOutputStream
            Log.i("post-data=", getData[1]);
            request.close();
            conn.connect();
            InputStream is = conn.getInputStream();
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
        int isSuccess= 0;
        isSuccess = s.indexOf("Update Success");
        if(isSuccess != -1) Toast.makeText(c,"成功更新一筆資料", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(c, BulletinsMainActivity.class);
        intent.putExtra("s_userid",getData[5]);
        intent.putExtra("s_login",getData[6]);
        intent.putExtra("s_groupid",getData[7]);
        intent.putExtra("s_name",getData[8]);
        c.startActivity(intent);
    }
}
