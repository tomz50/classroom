package com.classroom.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DbAdapter {
    public static final String KEY_ID = "_id";
    public static final String KEY_VISITDT = "visitdt";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_GROUPID = "groupid";
    public static final String KEY_USERID = "userid";
    public static final String KEY_NAME = "name";
    private static final String TABLE_NAME = "logininfo";
    private DbHelper mDbHelper;
    private SQLiteDatabase mdb;
    private final Context mCtx;
    private ContentValues values;

    public DbAdapter(Context mCtx) {
        this.mCtx = mCtx;
        open();
    }

    public void open() {
        mDbHelper = new DbHelper(mCtx);
        mdb = mDbHelper.getWritableDatabase();
        Log.v("DB_open",mdb.toString());
    }

    public void close(){
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
    public Cursor listData(){
        Cursor mCursor = mdb.query(TABLE_NAME, new String[] {KEY_ID, KEY_VISITDT, KEY_LOGIN, KEY_GROUPID, KEY_USERID,KEY_NAME},null,null,null,null,null,null);
        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long insertDataNotLogin(String visitdt) {
        long rowsAffected = -1;
        try{
            values = new ContentValues();
            values.put(KEY_VISITDT, visitdt);
            values.put(KEY_LOGIN, "false");
            values.put(KEY_GROUPID, "");
            values.put(KEY_USERID, "");
            values.put(KEY_NAME, "");
            rowsAffected = mdb.insert(TABLE_NAME,null,values);
//            rowsAffected = mdb.insertOrThrow(TABLE_NAME,null,values);
            Log.v("DB_Insert_rowsAffected", Long.toString(rowsAffected));
            return rowsAffected;
//        } catch (SQLException e) {
        } catch (Exception e) {
            Log.v("DB_新增失敗", e.toString());
   //         Toast.makeText(mCtx,"新增失敗!", Toast.LENGTH_SHORT).show();
            return rowsAffected;
          }
        finally {
     //       if (rowsAffected == -1)
     //           Toast.makeText(mCtx,"新增失敗!", Toast.LENGTH_SHORT).show();
     //       else
      //          Toast.makeText(mCtx,"新增成功!", Toast.LENGTH_SHORT).show();
        }
    }

    public long updateDataLogin(String visitdt, String login, String groupid, String userid, String name){
        long rowsAffected = 0;
        try{
             values = new ContentValues();
             values.put(KEY_LOGIN, login);
             values.put(KEY_GROUPID, groupid);
             values.put(KEY_USERID, userid);
             values.put(KEY_NAME, name);
             rowsAffected = mdb.update(TABLE_NAME, values, "visitdt='" + visitdt+"'",null);
             Log.v("DB_Update_rowsAffected", Long.toString(rowsAffected));
//             long rowsAffected = mdb.update(TABLE_NAME, values, "_id=9999",null);
             return rowsAffected;
        }catch(SQLException e){
              Log.v("DB_修改失敗", e.toString());
  //            Toast.makeText(mCtx,"修改失敗!", Toast.LENGTH_SHORT).show();
              return -1;
        }finally {
      //      if (rowsAffected == 1)
     //           Toast.makeText(mCtx,"修改成功!", Toast.LENGTH_SHORT).show();
       //     else
      //          Toast.makeText(mCtx,"修改失敗!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean deleteDataLogout(String today){
        String[] args = {today};
  //      String[] args = {Integer.toString(999)};
        long rowsAffected = 0;
        Log.v("DB_Delete_rowsAffected","==>"+today);
        rowsAffected = mdb.delete(TABLE_NAME, KEY_VISITDT + " = '?'",args);
        Log.v("DB_Delete_rowsAffected", Long.toString(rowsAffected));
  //      if (rowsAffected == 1)
 //           Toast.makeText(mCtx,"刪除成功!", Toast.LENGTH_SHORT).show();
 //       else
 //           Toast.makeText(mCtx,"刪除失敗!", Toast.LENGTH_SHORT).show();
        return true;
    }

    public Cursor queryByVisitdt(String k_visitdt){
        Cursor mCursor = null;
//        Log.v("DB_DbAdapter_queryByVisitdt",k_visitdt);
        mCursor = mdb.query(true, TABLE_NAME, new String[] {KEY_VISITDT, KEY_LOGIN, KEY_GROUPID, KEY_USERID, KEY_NAME},
                KEY_VISITDT + " = '" +k_visitdt+"'", null, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

}
