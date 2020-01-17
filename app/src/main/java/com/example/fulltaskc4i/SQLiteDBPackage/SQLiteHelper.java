package com.example.fulltaskc4i.SQLiteDBPackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    // Login Table
    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + BDConstants.LOGIN_TABLE + "(" + BDConstants._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BDConstants.EMAIL + " TEXT NOT NULL, " + BDConstants.PASSWORD + " TEXT NOT NULL);";

    // Person Table
    private static final String CREATE_PERSON_TABLE = "CREATE TABLE IF NOT EXISTS " + BDConstants.PERSON_TABLE + "(" + BDConstants._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BDConstants.PERSON_NAME + " TEXT NOT NULL, " + BDConstants.PERSON_SAYING + " TEXT NOT NULL, " + BDConstants.PERSON_INFO + " TEXT NOT NULL, " + BDConstants.PERSON_IMAGE + " BLOB);";

    // DB
    private static String DATABASE_NAME = "personDb.db";
    private static int DATABASE_VERSION = 2;
    private SQLiteDatabase db;


    SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PERSON_TABLE);
        sqLiteDatabase.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BDConstants.LOGIN_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BDConstants.PERSON_TABLE);
        onCreate(sqLiteDatabase);
    }


}
