package com.brunocesar.trackmypack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.brunocesar.trackmypack.models.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BrunoCesar on 05/05/2015.
 */
public class HistoryDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.HISTORY_ID,
            DatabaseHelper.HISTORY_ID_PACKAGE,
            DatabaseHelper.HISTORY_ACTION,
            DatabaseHelper.HISTORY_DATE,
            DatabaseHelper.HISTORY_DETAILS,
            DatabaseHelper.HISTORY_PLACE,
    };

    public HistoryDataSource(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public History create(History history) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.HISTORY_ACTION, history.getAction());
        values.put(DatabaseHelper.HISTORY_DATE, history.getDate());
        values.put(DatabaseHelper.HISTORY_DETAILS, history.getDetails());
        values.put(DatabaseHelper.HISTORY_PLACE, history.getPlace());
        values.put(DatabaseHelper.HISTORY_ID_PACKAGE, history.getIdPackage());

        long id = database.insert(DatabaseHelper.HISTORY_TABLE, null, values);

        return get(id);
    }

    public History update(History history) {

        long id = history.getId();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.HISTORY_ID, history.getId());
        values.put(DatabaseHelper.HISTORY_ID_PACKAGE, history.getIdPackage());
        values.put(DatabaseHelper.HISTORY_ACTION, history.getAction());
        values.put(DatabaseHelper.HISTORY_DATE, history.getDate());
        values.put(DatabaseHelper.HISTORY_DETAILS, history.getDetails());
        values.put(DatabaseHelper.HISTORY_PLACE, history.getPlace());

        database.update(DatabaseHelper.HISTORY_TABLE, values,
                DatabaseHelper.HISTORY_ID + " = " + id, null);

        return get(id);
    }

    public void delete(History history) {
        long id = history.getId();
        System.out.println("History deleted with id: " + id);
        database.delete(DatabaseHelper.HISTORY_TABLE, DatabaseHelper.HISTORY_ID + " = " + id, null);
    }

    public History get(long id) {

        History history = null;

        Cursor cursor = database.query(DatabaseHelper.HISTORY_TABLE,
                allColumns, DatabaseHelper.HISTORY_ID + " = " + id, null, null, null, null);

        cursor.moveToFirst();
        history = cursorToHistory(cursor);
        cursor.close();

        return history;
    }

    public List<History> get()
    {
        return this.get(null, null);
    }

    public List<History> get(String where, String orderBy) {
        List<History> histories = new ArrayList<History>();

        Cursor cursor = database.query(DatabaseHelper.HISTORY_TABLE,
                allColumns, where, null, null, null, orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            History history = cursorToHistory(cursor);
            histories.add(history);
            cursor.moveToNext();
        }
        cursor.close();

        return histories;
    }

    private History cursorToHistory(Cursor cursor) {
        History history = new History();

        history.setId(cursor.getLong(0));
        history.setIdPackage(cursor.getLong(1));
        history.setAction(cursor.getString(2));
        history.setDate(cursor.getString(3));
        history.setDetails(cursor.getString(4));
        history.setPlace(cursor.getString(5));

        return history;
    }
}
