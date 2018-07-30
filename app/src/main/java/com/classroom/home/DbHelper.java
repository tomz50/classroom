package com.classroom.home;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    public static final String KEY_ID = "_id";
    public static final String KEY_VISITDT = "visitdt";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_GROUPID = "groupid";
    public static final String KEY_USERID = "userid";
    public static final String KEY_NAME = "name";
//    public static final String KEY_FIRSTLOGINTM = "FirstLoginTm";
//    public static final String KEY_IMEI = "Imei";
//    public static final String KEY_LASTLOGINTM = "LastLoginTm";
//    public static final String KEY_LASTLOGOUTTM = "LastLogoutTm";
    private static final String DATABASE_NAME = "classroom";
    private static final String TABLE_NAME = "logininfo";
    private static final int DATABASE_VERSION = 7;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                KEY_ID + " integer PRIMARY KEY autoincrement," +
                KEY_VISITDT + " DATE NOT NULL UNIQUE," +
                KEY_LOGIN + " VARCHAR(10) ," +
                KEY_GROUPID + " VARCHAR(10) ," +
                KEY_USERID + " VARCHAR(20) ," +
                KEY_NAME + " VARCHAR(50) );";

        Log.i("DB_CREATE",DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
