package com.example.celikapp20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MapsDB {

    public static final String DB_NAME = "Maps.db";
    public static final int DB_VERSION = 6;

    public static final String LIST_TABLE = "product_position";

    public static final String PRODUCT_ID = "_id";
    public static final int PRODUCT_ID_COL = 0;

    public static final String PRODUCT_NAME = "product_position_name";
    public static final int PRODUCT_NAME_COL = 1;

    public static final String PRODUCT_LATITUDE = "product_position_latitude";
    public static final int PRODUCT_LATITUDE_COL = 2;

    public static final String PRODUCT_LONGITUDE = "product_position_longitude";
    public static final int PRODUCT_LONGITUDE_COL = 3;

    public static final String PRODUCT_LOCATION = "product_position_location";
    public static final int PRODUCT_LOCATION_COL= 4;

    public static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE "          +  LIST_TABLE + " (" +
                    PRODUCT_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PRODUCT_NAME     + " STRING , " +
                    PRODUCT_LATITUDE + " STRING, " +
                    PRODUCT_LONGITUDE + " STRING, " +
                    PRODUCT_LOCATION + " STRING );";

    public static final String DROP_LIST_TABLE =
            "DROP TABLE IF EXISTS " + LIST_TABLE;


    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_PRODUCT_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("product list", "upgrading db from" + oldVersion + "to" + newVersion);

            db.execSQL(DROP_LIST_TABLE);

            onCreate(db);
        }

    }


    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public MapsDB (Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    public void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    public void closeDB() {
        if (db != null)
            db.close();
    }

    public void insert(String name, String latitude, String longitude, String location){
        openWriteableDB();
        ContentValues content= new ContentValues();
        content.put(PRODUCT_NAME, name);
        content.put(PRODUCT_LATITUDE, latitude);
        content.put(PRODUCT_LONGITUDE, longitude);
        content.put(PRODUCT_LOCATION, location);
        db.insert(LIST_TABLE, null, content);
        closeDB();
    }

    public void clean(){
        openWriteableDB();
        db.delete(LIST_TABLE,null, null );
        closeDB();
    }
    public Cursor getProd(){
        openReadableDB();
        Cursor c=db.rawQuery("SELECT * FROM " + LIST_TABLE, null);
        return c;

    }

}
