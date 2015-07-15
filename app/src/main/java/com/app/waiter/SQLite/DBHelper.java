package com.app.waiter.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.waiter.Model.List.Content;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

/**
 * Created by javier.gomez on 18/06/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WaiterDB.db";
    public static final String MENU_TABLE_NAME = "menu";
    public static final String MENU_COLUMN_ID = "id";
    public static final String MENU_COLUMN_NAME = "name";
    public static final String MENU_COLUMN_PRODUCT = "product";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "create table " + MENU_COLUMN_NAME +
            " ("+MENU_COLUMN_ID+" integer primary key, "+MENU_COLUMN_NAME+
            " text,"+ MENU_COLUMN_PRODUCT +" text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MENU_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertProducts(String type, List<Content> listProducts) {
        SQLiteDatabase db = this.getWritableDatabase();
        Gson gson = new Gson();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MENU_COLUMN_NAME, type);
        contentValues.put(MENU_COLUMN_PRODUCT, gson.toJson(listProducts).getBytes());
        db.insert(MENU_TABLE_NAME, null, contentValues);
        return true;
    }

    public List<Content> getProducts(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + MENU_TABLE_NAME + " where name = " + type, null);
        byte[] info = res.getBlob(res.getColumnIndex(MENU_COLUMN_PRODUCT));
        String json = new String(info);
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Content>>(){}.getType());
    }

    public HashMap<String, List<Content>> getAllProducts() {
        HashMap<String, List<Content>> dataset = new HashMap<String, List<Content>>();
        Gson gson = new Gson();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + MENU_TABLE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            byte[] info = res.getBlob(res.getColumnIndex(MENU_COLUMN_NAME));
            String json = new String(info);
            String type = gson.fromJson(json, new TypeToken<String>() {
            }.getType());
            info = res.getBlob(res.getColumnIndex(MENU_COLUMN_PRODUCT));
            json = new String(info);
            List<Content> listProducts = gson.fromJson(json, new TypeToken<List<Content>>() {
            }.getType());
            dataset.put(type, listProducts);
        }
        return dataset;
    }
}
