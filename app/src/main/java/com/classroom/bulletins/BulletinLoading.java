package com.classroom.bulletins;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class BulletinLoading extends AsyncTask<String, Void, String[]> {
    private ProgressDialog dialog;
    Context c;
    String crlf = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";
    String queryId;
    public BulletinLoading(Context context, String queryId) {
        this.c = context;
        this.queryId = queryId;
        dialog = new ProgressDialog(c);
    }
    @Override
    protected String[] doInBackground(String... strings) {
        try {
            URL u = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //將資料傳送到遠端


            String id = "id=" + queryId;
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(id);
            writer.flush();
            writer.close();
            os.close();
            Log.i("postString=", id);

            InputStream is = conn.getInputStream();
            // Read the stream
            byte[] b = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (is.read(b) != -1)
                baos.write(b);

            String response = new String(baos.toByteArray());
            Log.i("JSONResp=", response);
            conn.connect();
            JSONObject arr = new JSONObject(response);
            String[] old_data;
            old_data = loadContact(arr);
            Log.v("data=", old_data[0]);

            return old_data;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
    protected void onPostExecute(String[] s) {
        super.onPostExecute(s);
        dialog.dismiss();
    }
    private String[] loadContact(JSONObject obj) throws JSONException {
        String id = obj.getString("id");  //?????????????/
        String subject = obj.getString("Subject");
        String desciption = obj.getString("Desciption");
        String implementationdate = obj.getString("ImplementationDate");
        String effectivedate = obj.getString("EffectiveDate");
        Log.v("jsonObj=",obj.getString("Subject").toString());
        String[] old_data = new String[] {id, subject, desciption, implementationdate, effectivedate };
        return old_data;

    }
}
