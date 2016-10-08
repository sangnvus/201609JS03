package com.favn.firstaid.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.favn.firstaid.Models.Injury;
import com.favn.firstaid.Models.Instruction;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hung Gia on 10/6/2016.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static String DB_PATH = "/data/data/com.favn.firstaid/databases/";
    public static String DB_NAME = "favn.db";
    public static String TABLE_NAME_INJURY = "injury";
    public static String TABLE_NAME_INSTRUCTION = "intruction";
    private static int DB_VERSION = 1;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    public void createDatabase() {
        if (checkDatabase()) {
        } else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (Exception e) {
                Log.e("DB error",e.getMessage());
            }
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String dbPath = mContext.getDatabasePath(DB_NAME).getPath();
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {

        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private boolean copyDatabase() {
        try {
            InputStream inputStream = mContext.getAssets().open(DatabaseOpenHelper.DB_NAME);
            String outFileName = DatabaseOpenHelper.DB_PATH + DatabaseOpenHelper.DB_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            Log.e("DB error",e.getMessage());
            return false;
        }
    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DB_NAME).getPath();
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public List<Injury> getListInjury() {
        Injury injury = null;
        List<Injury> injuryListList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_INJURY, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            injury = new Injury(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            injuryListList.add(injury);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return injuryListList;
    }

    public List<Instruction> getListInstruction(int id) {
        Instruction instruction = null;
        List<Instruction> instructionList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_INSTRUCTION + " WHERE field1 == '" + id + "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            instruction = new Instruction(cursor.getInt(0), cursor.getString(1));
            instructionList.add(instruction);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return instructionList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
