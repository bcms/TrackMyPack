package com.brunocesar.trackmypack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.brunocesar.trackmypack.models.Package;

import java.util.ArrayList;
import java.util.List;

public class PackageDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.PACKAGES_ID,
            DatabaseHelper.PACKAGES_CODE,
            DatabaseHelper.PACKAGES_NAME,
    };

    public PackageDataSource(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Package create(Package pack) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.PACKAGES_CODE, pack.getCode());
        values.put(DatabaseHelper.PACKAGES_NAME, pack.getName());

        long id = database.insert(DatabaseHelper.PACKAGES_TABLE, null, values);

        return get(id);
    }

    public Package update(Package pack) {

        long id = pack.getId();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.PACKAGES_ID, pack.getId());
        values.put(DatabaseHelper.PACKAGES_NAME, pack.getName());
        values.put(DatabaseHelper.PACKAGES_CODE, pack.getCode());

        database.update(DatabaseHelper.PACKAGES_TABLE, values,
                DatabaseHelper.PACKAGES_ID + " = " + id, null);

        return get(id);
    }

    public void delete(Package pack) {
        long id = pack.getId();
        System.out.println("Package deleted with id: " + id);
        database.delete(DatabaseHelper.PACKAGES_TABLE, DatabaseHelper.PACKAGES_ID + " = " + id, null);
    }

    public Package get(long id) {

        Package pack;

        Cursor cursor = database.query(DatabaseHelper.PACKAGES_TABLE,
                allColumns, DatabaseHelper.PACKAGES_ID + " = " + id, null, null, null, null);

        cursor.moveToFirst();
        pack = cursorToPackage(cursor);
        cursor.close();

        return pack;
    }

    //public List<Package> get()
    //{
    //    return this.get(null, null);
    //}

    public List<Package> get(String where, String orderBy) {
        List<Package> packages = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.PACKAGES_TABLE,
                allColumns, where, null, null, null, orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Package pack = cursorToPackage(cursor);
            packages.add(pack);
            cursor.moveToNext();
        }
        cursor.close();

        return packages;
    }

    private Package cursorToPackage(Cursor cursor) {
        Package pack = new Package();

        pack.setId(cursor.getLong(0));
        pack.setCode(cursor.getString(1));
        pack.setName(cursor.getString(2));

        return pack;
    }
}
