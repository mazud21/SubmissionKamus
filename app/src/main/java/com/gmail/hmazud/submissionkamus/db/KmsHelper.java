package com.gmail.hmazud.submissionkamus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.gmail.hmazud.submissionkamus.model.KmsModel;

import java.util.ArrayList;

public class KmsHelper {
    private static String ENGLISH = DBHelper.TABLE_ENGLISH;
    private static String INDONESIA = DBHelper.TABLE_INDONESIA;

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public KmsHelper(Context context) {
        this.context = context;
    }

    public KmsHelper open() throws SQLException {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqLiteDatabase.close();
    }

    public Cursor searchQueryByName(String query, boolean english){
        String DATABASE_TABLE = english ? ENGLISH : INDONESIA;
        return sqLiteDatabase.rawQuery("SELECT * FROM "
                + DATABASE_TABLE
                + " WHERE "
                + DBHelper.FIELD_WORD
                + " LIKE '%"
                + query.trim()
                + "%'", null);
    }

    public ArrayList<KmsModel> getDataByName(String search, boolean english) {
        KmsModel kmsModel;

        ArrayList<KmsModel> arrayList = new ArrayList<>();
        Cursor cursor = searchQueryByName(search, english);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                kmsModel = new KmsModel();
                kmsModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ID)));
                kmsModel.setWord(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_WORD)));
                kmsModel.setTranslate(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_TRANSLATE)));

                arrayList.add(kmsModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public String getData(String search, boolean english) {
        String result = "";
        Cursor cursor = searchQueryByName(search, english);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            result = cursor.getString(2);
            for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getString(2);
            }
        }
        cursor.close();
        return result;
    }

    public Cursor queryAllData(boolean english) {
        String DATABASE_TABLE = english ? ENGLISH : INDONESIA;
        return sqLiteDatabase.rawQuery("SELECT * FROM "
                + DATABASE_TABLE
                + " ORDER BY "
                + DBHelper.FIELD_ID
                + " ASC", null);
    }

    public ArrayList<KmsModel> getAllData(boolean english) {
        KmsModel kmsModel;

        ArrayList<KmsModel> arrayList = new ArrayList<>();
        Cursor cursor = queryAllData(english);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                kmsModel = new KmsModel();
                kmsModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.FIELD_ID)));
                kmsModel.setWord(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_WORD)));
                kmsModel.setTranslate(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FIELD_TRANSLATE)));

                arrayList.add(kmsModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(KmsModel kmsModel, boolean english) {
        String DATABASE_TABLE = english ? ENGLISH : INDONESIA;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.FIELD_WORD, kmsModel.getWord());
        contentValues.put(DBHelper.FIELD_TRANSLATE, kmsModel.getTranslate());

        return sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    public void insertTransaction(ArrayList<KmsModel> kmsModels, boolean english) {
        String DATABASE_TABLE = english ? ENGLISH : INDONESIA;
        String sql_query = "INSERT INTO "
                + DATABASE_TABLE + " ("
                + DBHelper.FIELD_WORD + ", "
                + DBHelper.FIELD_TRANSLATE
                + ") VALUES (?, ?)";

        sqLiteDatabase.beginTransaction();

        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql_query);
        for (int i = 0; i < kmsModels.size(); i++) {
            sqLiteStatement.bindString(1, kmsModels.get(i).getWord());
            sqLiteStatement.bindString(2, kmsModels.get(i).getTranslate());
            sqLiteStatement.execute();
            sqLiteStatement.clearBindings();
        }

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public void update(KmsModel kmsModel, boolean english) {
        String DATABASE_TABLE = english ? ENGLISH : INDONESIA;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.FIELD_WORD, kmsModel.getWord());
        contentValues.put(DBHelper.FIELD_TRANSLATE, kmsModel.getTranslate());
        sqLiteDatabase.update(DATABASE_TABLE, contentValues, DBHelper.FIELD_ID + "= '"+ kmsModel.getId() + "'",null);
    }

    public void delete(int id, boolean english) {
        String DATABASE_TABLE = english ? ENGLISH : INDONESIA;
        sqLiteDatabase.delete(DATABASE_TABLE, DBHelper.FIELD_ID + "= '" + id + "'", null);
    }
}

