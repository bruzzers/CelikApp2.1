package com.example.celikapp20;


import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.text.Editable;
import android.util.Log;

import org.w3c.dom.Text;

public class DB {

    public static final String DB_NAME = "date.db";
    public static final int DB_VERSION = 8;

    public static final String LIST_TABLE = "product";

    public static final String PRODUCT_ID = "_id";
    public static final int PRODUCT_ID_COL = 0;

    public static final String PRODUCT_NAME = "product_name";
    public static final int PRODUCT_NAME_COL = 1;

    public static final String PRODUCT_PRICE = "product_price";
    public static final int PRODUCT_PRICE_COL = 2;

    public static final String PRODUCT_LOCATION = "product_location";
    public static final int PRODUCT_LOCATION_COL = 3;

    public static final String PRODUCT_BRAND = "product_brand";
    public static final int PRODUCT_BRAND_COL = 4;

    public static final String PRODUCT_CODE = "product_code";
    public static final int PRODUCT_CODE_COL = 5;

    public static final String PRODUCT_LATITUDE = "product_lat";
    public static final int PRODUCT_LATITUDE_COL= 6;

    public static final String PRODUCT_LONGITUDE = "product_long";
    public static final int PRODUCT_LONGITUDE_COL= 7;


    public static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE "          +  LIST_TABLE + " (" +
                    PRODUCT_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PRODUCT_NAME     + " STRING , " +
                    PRODUCT_PRICE    + " STRING , " +
                    PRODUCT_LOCATION + " STRING, " +
                    PRODUCT_BRAND    + " STRING, " +
                    PRODUCT_CODE     + " STRING, " +
                    PRODUCT_LATITUDE + " STRING, " +
                    PRODUCT_LONGITUDE + " STRING );";

    public static final String DROP_LIST_TABLE =
            "DROP TABLE IF EXISTS " + LIST_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        CursorFactory factory, int version) {
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

    public DB (Context context) {
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

    public Cursor getList(){
        return db.query(LIST_TABLE, new String[]{PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_LOCATION, PRODUCT_BRAND},
                null, null, null, null, null);
    }

    public Cursor getProd(){
        Cursor c=db.rawQuery("SELECT * FROM product ", null);
        return c;
    }

    public Cursor getNProd(String name, String price, String location, String brand, String code, String latitude, String longitude){
        Cursor c=db.rawQuery("SELECT * FROM product WHERE " + PRODUCT_NAME + "=? AND " + PRODUCT_PRICE +"=? AND "
                        + PRODUCT_LOCATION + "=? AND " + PRODUCT_BRAND + "=? AND " + PRODUCT_CODE + "=? AND " +
                PRODUCT_LATITUDE + "=? AND " + PRODUCT_LONGITUDE + "=?"
                , new String[]{name, price, location, brand, code, latitude, longitude } );
        return c;
    }

    public void Insert(String name, String price, String location, String brand, String code, String latitude, String longitude){
        openWriteableDB();
        Cursor c= getNProd(name, price, location, brand, code, latitude, longitude);
        if(c.moveToFirst()){}
        else {
            ContentValues content = new ContentValues();
            content.put(PRODUCT_NAME, name);
            content.put(PRODUCT_PRICE, price);
            content.put(PRODUCT_LOCATION, location);
            content.put(PRODUCT_BRAND, brand);
            content.put(PRODUCT_CODE, code);
            content.put(PRODUCT_LATITUDE, latitude);
            content.put(PRODUCT_LONGITUDE, longitude);
            db.insert(LIST_TABLE, null, content);
        }
        closeDB();
    }

    public void Delete(String an, String al, String ap, String am, String ac){
        openWriteableDB();
        db.delete(LIST_TABLE, PRODUCT_NAME + " =? AND " + PRODUCT_LOCATION + " =? AND " + PRODUCT_PRICE + " =? AND " + PRODUCT_BRAND + " =? AND " + PRODUCT_CODE + " =?", new String[] {an, al, ap, am, ac} );
        closeDB();
    }


}
