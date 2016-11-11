package com.favn.firstaid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PointF;
import android.util.Log;

import com.favn.firstaid.Models.HealthFacility;
import com.favn.firstaid.Models.Injury;
import com.favn.firstaid.Models.Instruction;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hung Gia on 10/6/2016.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static String DB_PATH = "/data/data/com.favn.firstaid/databases/";
    public static String DB_NAME = "favn_ver4.db";
    public static String TABLE_NAME_INJURIES = "injuries";
    public static String TABLE_NAME_INSTRUCTIONS = "instructions";
    public static String TABLE_NAME_HEALTH_FACILITIES = "health_facilities";
    public static String TABLE_NAME_FAQS = "faqs";
    public static String TABLE_NAME_NOTIFICATIONS = "notifications";
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
                Log.e("DB error", e.getMessage());
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
            Log.e("DB error", e.getMessage());
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

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Get Injuries
    public List<Injury> getListInjury() {
        Injury injury = null;
        List<Injury> injuryListList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_INJURIES, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String injuryName = cursor.getString(1);
            String injurySymptom = cursor.getString(2);
            String injuryImage = cursor.getString(4);
            injury = new Injury(id, injuryName, injuryImage);
            injuryListList.add(injury);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return injuryListList;
    }

    // START CRUD INJURY FUNCTIONS - Kienmt : 11/08/2016
    // Insert injury
    public long insertInjury(Injury injury) {
        ContentValues values = new ContentValues();

        values.put("id", injury.getId());
        values.put("injury_name", injury.getInjuryName());
        values.put("symptom", injury.getInjurySymptom());
        values.put("priority", injury.getPriority());
        values.put("image", injury.getImage());
        values.put("updated_at", injury.getUpdated_at());
        values.put("created_at", injury.getCreated_at());

        return mDatabase.insert(TABLE_NAME_INJURIES, null, values);

    }

    // Insert injury
    public long updateInjury(Injury injury) {
        ContentValues values = new ContentValues();

        values.put("id", injury.getId());
        values.put("injury_name", injury.getInjuryName());
        values.put("symptom", injury.getInjurySymptom());
        values.put("priority", injury.getPriority());
        values.put("image", injury.getImage());
        values.put("updated_at", injury.getUpdated_at());
        values.put("created_at", injury.getCreated_at());

        values.put("id", 100);
        values.put("injury_name", "new injury_name");
        values.put("symptom", "new symptom");
        values.put("priority", "new priority");
        values.put("image", "new image");
        values.put("updated_at", "new updated_at");
        values.put("created_at", "new created_at");

        String whereClause = "id = " + injury.getId();

        return mDatabase.update(TABLE_NAME_INJURIES, values, whereClause, null);

    }

    // Delete injury

    // END CRUD INJURY FUNCTIONS



    // Get Instructions
    public List<Instruction> getListInstruction(int id) {
        Instruction instruction = null;
        List<Instruction> instructionList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_INSTRUCTIONS + " WHERE " +
                "injury_id == " + id, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int injuryId = cursor.getInt(1);
            int step = cursor.getInt(2);
            String content = cursor.getString(3);
            boolean isMakeCall = cursor.getInt(4) == 1;
            String explanation = cursor.getString(5);
            String image = cursor.getString(6);
            String audio = cursor.getString(7);
            instruction = new Instruction(injuryId, step, content, isMakeCall, explanation, image,
                    audio);
            instructionList.add(instruction);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return instructionList;
    }




    // Get Health Facility
    public List<HealthFacility> getListHealthFacility(PointF[] points) {
        HealthFacility healthFacility = null;
        List<HealthFacility> healthFacilities = new ArrayList<>();
        openDatabase();
        String WHERE_CLAUSE = "WHERE "
                + "latitude > " + String.valueOf(points[2].x) + " AND "
                + "latitude < " + String.valueOf(points[0].x) + " AND "
                + "longitude < " + String.valueOf(points[1].y) + " AND "
                + "longitude > " + String.valueOf(points[3].y);
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_HEALTH_FACILITIES +
                        " " +WHERE_CLAUSE,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int healthFacilityId = cursor.getInt(0);
            String name = cursor.getString(1);
            int type = cursor.getInt(2);
            String address = cursor.getString(3);
            String vicinity = cursor.getString(4);
            String phone = cursor.getString(5);
            double latitude = cursor.getDouble(6);
            double longitude = cursor.getDouble(7);

            healthFacility = new HealthFacility(healthFacilityId, name, type, address, vicinity,
                    phone,
                    latitude,
                    longitude);

            healthFacilities.add(healthFacility);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return healthFacilities;
    }
}
