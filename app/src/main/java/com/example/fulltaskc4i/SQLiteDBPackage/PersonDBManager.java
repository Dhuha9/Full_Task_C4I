package com.example.fulltaskc4i.SQLiteDBPackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.fulltaskc4i.ModelsPackage.PersonModel;

import java.util.ArrayList;

public class PersonDBManager {

    private Context context;
    private SQLiteDatabase db;
    private SQLiteHelper sqLiteHelper;

    public PersonDBManager(Context context) {
        this.context = context;

    }

    public PersonDBManager open() {
        sqLiteHelper = new SQLiteHelper(context);
        db = sqLiteHelper.getWritableDatabase();
        return this;
    }

//    public void close() {
//        sqLiteHelper.close();
//    }

    public void addPerson(PersonModel person) {
        if (person != null) {
            ContentValues values = new ContentValues();
            values.put(BDConstants.PERSON_NAME, person.getPersonName());
            values.put(BDConstants.PERSON_SAYING, person.getPersonSaying());
            values.put(BDConstants.PERSON_INFO, person.getPersonInfo());
            values.put(BDConstants.PERSON_IMAGE, person.getPersonImage());
            db.insert(BDConstants.PERSON_TABLE, null, values);
        }
    }

    public ArrayList<PersonModel> getPage(int personId) {
        Cursor cursor = db.query(BDConstants.PERSON_TABLE, null, getSelectionStr(personId), null, null, null, BDConstants._ID + " ASC", "3");
        return cursorItemsToList(cursor);
    }

    private String getSelectionStr(int personId) {
        if (personId == 0) {
            return null;
        } else {
            return BDConstants._ID + ">" + personId;
        }
    }

    public PersonModel getPerson(int personId) {
        Cursor cursor = db.query(BDConstants.PERSON_TABLE, null, BDConstants._ID + "=?", new String[]{String.valueOf(personId)}, null, null, null, null);
        ArrayList<PersonModel> personsList=cursorItemsToList(cursor);
        if ( personsList== null) {
            return null;
        } else {
            return personsList.get(0);
        }
    }

    public PersonModel getLastPerson() {
        Cursor cursor = db.query(BDConstants.PERSON_TABLE, null, null, null, null, null, BDConstants._ID + " DESC", "1");
        ArrayList<PersonModel> personsList=cursorItemsToList(cursor);
        if ( personsList== null) {
            return null;
        } else {
            return personsList.get(0);
        }
    }

    public ArrayList<PersonModel> getLastTenRecords() {
        Cursor cursor = db.query(BDConstants.PERSON_TABLE, null, null, null, null, null, BDConstants._ID + " DESC", "10");
        return cursorItemsToList(cursor);
    }

    private ArrayList<PersonModel> cursorItemsToList(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            ArrayList<PersonModel> tempList = new ArrayList<>();

            do {
                PersonModel personModel = new PersonModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getBlob(4));
                tempList.add(personModel);
            } while (cursor.moveToNext());
            cursor.close();
            return tempList;
        } else {
            return null;
        }
    }

    public void updatePerson(PersonModel person) {

        if (person != null) {
            ContentValues values = new ContentValues();
            values.put(BDConstants.PERSON_NAME, person.getPersonName());
            values.put(BDConstants.PERSON_SAYING, person.getPersonSaying());
            values.put(BDConstants.PERSON_INFO, person.getPersonInfo());
            values.put(BDConstants.PERSON_IMAGE, person.getPersonImage());

            db.update(BDConstants.PERSON_TABLE,
                    values,
                    BDConstants._ID + " = ?",
                    new String[]{String.valueOf(person.getId())});
        }
    }

    public void deletePerson(int personId) {
        db.delete(BDConstants.PERSON_TABLE, BDConstants._ID + " = ?",
                new String[]{String.valueOf(personId)});
    }


    public long getTableRowCount() {
        return DatabaseUtils.queryNumEntries(db, BDConstants.PERSON_TABLE);
    }


}
