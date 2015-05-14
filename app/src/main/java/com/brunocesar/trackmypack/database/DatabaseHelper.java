package com.brunocesar.trackmypack.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Packages
    public static final String PACKAGES_TABLE = "packages";
    public static final String PACKAGES_ID = "id";
    public static final String PACKAGES_NAME = "name";
    public static final String PACKAGES_CODE = "code";

    public static final String PACKAGES_CREATE_TABLE =
            "create table " + PACKAGES_TABLE + "(" +
                PACKAGES_ID + " integer primary key autoincrement," +
                PACKAGES_NAME + " text not null," +
                PACKAGES_CODE + " text not null" +
            ");";

    //History
    public static final String HISTORY_TABLE = "history";
    public static final String HISTORY_ID = "id";
    public static final String HISTORY_DATE = "date";
    public static final String HISTORY_PLACE = "place";
    public static final String HISTORY_ACTION = "action";
    public static final String HISTORY_DETAILS = "details";
    public static final String HISTORY_ID_PACKAGE = "id_package";

    public static final String HISTORY_CREATE_TABLE =
            "create table " + HISTORY_TABLE + "(" +
                    HISTORY_ID + " integer primary key autoincrement," +
                    HISTORY_DATE + " text not null," +
                    HISTORY_PLACE + " text not null," +
                    HISTORY_ACTION + " text not null," +
                    HISTORY_DETAILS + " text not null," +
                    HISTORY_ID_PACKAGE + " integer not null," +

                    "FOREIGN KEY(" + HISTORY_ID_PACKAGE + ") REFERENCES " + PACKAGES_TABLE + "(" + PACKAGES_ID + ")  ON DELETE CASCADE" +
            ");";

    //Database
    private static final String DATABASE_NAME = "trackmypack.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();

        db.execSQL("PRAGMA foreign_keys = ON");

        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PACKAGES_CREATE_TABLE);
        db.execSQL(HISTORY_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PACKAGES_TABLE);

        onCreate(db);
    }
}
