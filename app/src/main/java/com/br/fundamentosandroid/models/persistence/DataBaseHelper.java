package com.br.fundamentosandroid.models.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE = "SERVICE_ORDER_DB";
    private static int VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DataBaseHelper.DATA_BASE, null, DataBaseHelper.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ServiceOrderContract.createTable());
        db.execSQL(UserContract.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

}
