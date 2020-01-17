package com.example.fulltaskc4i.SQLiteDBPackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fulltaskc4i.ModelsPackage.LoginModel;

public class LoginDbManager {

    private Context context;
    private SQLiteDatabase db;
    private SQLiteHelper sqLiteHelper;

    public LoginDbManager(Context context) {
        this.context = context;
    }

    public LoginDbManager open() {
        sqLiteHelper = new SQLiteHelper(context);
        db = sqLiteHelper.getWritableDatabase();
        return this;
    }

//    public void close() {
//        sqLiteHelper.close();
//    }

    public void addUser(LoginModel user) {
        if (user != null) {
            db = sqLiteHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(BDConstants.EMAIL, user.getEmail());
            values.put(BDConstants.PASSWORD, user.getPassword());
            db.insert(BDConstants.LOGIN_TABLE, null, values);
        }
    }

    public LoginModel getUser(String userEmail) {
        if (userEmail != null) {
            Cursor cursor = db.query(BDConstants.LOGIN_TABLE,
                    null,
                    BDConstants.EMAIL + "=?",
                    new String[]{userEmail},
                    null,
                    null,
                    null,
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                LoginModel loginModel = new LoginModel(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2));
                cursor.close();

                return loginModel;

            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
