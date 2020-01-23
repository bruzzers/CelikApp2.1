package com.example.celikapp20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BarcodeProductDB {
    public static final String DB_NAME = "Barcode.db";
    public static final int DB_VERSION = 2;

    public static final String LIST_TABLE = "barcode_product";

    public static final String PRODUCT_ID = "_id";
    public static final int PRODUCT_ID_COL = 0;

    public static final String PRODUCT_NAME = "product_name";
    public static final int PRODUCT_NAME_COL = 1;

    public static final String PRODUCT_BRAND = "product_brand";
    public static final int PRODUCT_BRAND_COL = 2;

    public static final String PRODUCT_CODE = "product_code";
    public static final int PRODUCT_CODE_COL = 3;

    public static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE "          +  LIST_TABLE + " (" +
                    PRODUCT_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PRODUCT_NAME     + " STRING , " +
                    PRODUCT_BRAND + " STRING, " +
                    PRODUCT_CODE + " STRING );";

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
    private BarcodeProductDB.DBHelper dbHelper;

    public BarcodeProductDB (Context context) {
        dbHelper = new BarcodeProductDB.DBHelper(context, DB_NAME, null, DB_VERSION);
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

    public void insert(String name, String brand, String code){
        openWriteableDB();
        ContentValues content= new ContentValues();
        content.put(PRODUCT_NAME, name);
        content.put(PRODUCT_BRAND, brand);
        content.put(PRODUCT_CODE, code);
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
