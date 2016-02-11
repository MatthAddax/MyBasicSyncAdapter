package be.matthieu.mybasicsyncadapter.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.matthieu.mybasicsyncadapter.contracts.HoraireContract;
import be.matthieu.mybasicsyncadapter.contracts.ValveContract;

/**
 * Created by Matthieu on 02-02-16  .
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LocalHenallux.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ValveContract.SQL_CREATE_ENTRIES);
        db.execSQL(HoraireContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ValveContract.SQL_DELETE_ENTRIES);
        db.execSQL(HoraireContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
