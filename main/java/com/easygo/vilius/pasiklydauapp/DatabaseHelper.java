package com.easygo.vilius.pasiklydauapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Klase duomenu bazes operacijoms apdoroti
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;//DB versijaa
    private static final String DATABASE_NAME = "location.db";//DB pavadinimas
    private static String TABLE_NAME = "locations";//Lenteles pavadinimas
    private final static String KEY_ID = "id";//ID laukas
    private final static String KEY_ADDRESS = "address";//Adreso laukas
    private final static String KEY_DATE = "date";//Datos laukas

    /**
     * DatabaseHelper objecto konstruktorius
     * @param context - kontekstas
     */
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate metodas- sukuria lentele su nurodytais laukais
     * @param db - SQLite duombazes objektas
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_ADDRESS + " TEXT," +
                KEY_DATE + " TEXT " +
                ")";
        db.execSQL(query);
    }

    /**
     * onUpgrade metodas- atnaujina lentele
     * @param db - SQLite duombazes objektas
     * @param oldVersion - senoji versija
     * @param newVersion - naujoji versija
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    /**
     * Metodas iterpti vietoves irasui
     * @param entry - LocationEntry objektas
     * @return id - vieta,kur iterpti duomenys
     */
    public long addEntry(LocationEntry entry)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        cv.put(KEY_ADDRESS, entry.getAddress());
        cv.put(KEY_DATE, currentDateTimeString);
        long id = db.insert(TABLE_NAME, null, cv);
        db.close();
        return id;
    }

    /**
     * Metodas grazinantis vietoves irasa pagal id
     * @param id - grazinamo iraso id
     * @return entry- irasas pagal id
     */
    public LocationEntry getEntry(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_ADDRESS, KEY_DATE}, KEY_ID + "=?", new String[] { Integer.toString(id) }, null, null, null);

        LocationEntry entry = new LocationEntry();
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                entry.setID(cursor.getInt(0));
                entry.setAddress(cursor.getString(1));
                entry.setDate(cursor.getString(2));
            }
        }

        cursor.close();
        db.close();

        return entry;
    }

    /**
     * Grazina visus irasus
     * @return entries- visi irasai
     */
    public ArrayList<LocationEntry> getAllEntries()
    {
        ArrayList<LocationEntry> entries = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_DATE + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    LocationEntry entry = new LocationEntry();
                    entry.setID(cursor.getInt(0));
                    entry.setAddress(cursor.getString(1));
                    entry.setDate(cursor.getString(2));
                    entries.add(entry);
                } while(cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();

        return entries;
    }

    /**
     * Metodas istrinantis irasa pagal id
     * @param id - trinamo iraso numeris
     */
    public void deleteEntry(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{Integer.toString(id)});
        db.close();
    }

    /**
     * Metodas istrinti lentelei
     */
    public void delete()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,"",new String[]{});
        db.close();
    }

    /**
     * Metodas atnaujinti irasa
     * @param entry - naujinamo iraso objektas
     */
    public void updateEntry(LocationEntry entry)
    {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ADDRESS, entry.getAddress());
        cv.put(KEY_DATE, currentDateTimeString);

        db.update(TABLE_NAME, cv, KEY_ID + "=?", new String[] { Integer.toString(entry.getID()) });

        db.close();
    }
}
