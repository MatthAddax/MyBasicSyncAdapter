package be.matthieu.mybasicsyncadapter.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import be.matthieu.mybasicsyncadapter.contracts.ValveContract;
import be.matthieu.mybasicsyncadapter.model.Valve;
import be.matthieu.mybasicsyncadapter.helpers.DatabaseHelper;

/**
 * Created by Matthieu on 05-02-16  .
 */
public class ValveDataSource {
    private SQLiteDatabase db;
    private DatabaseHelper databaseHelper;

    private String[] allColumns = {
            ValveContract.ValvesEntry._ID,
            ValveContract.ValvesEntry.COLUMN_NAME_VALVE_TITLE,
            ValveContract.ValvesEntry.COLUMN_NAME_VALVE_CONTENT
    };

    public ValveDataSource(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
    }

    public Valve createValve(String titre, String contenu){
        ContentValues valve = new ContentValues();
        valve.put(ValveContract.ValvesEntry.COLUMN_NAME_VALVE_TITLE, titre);
        valve.put(ValveContract.ValvesEntry.COLUMN_NAME_VALVE_CONTENT,contenu);

        long insertId = db.insert(ValveContract.ValvesEntry.TABLE_NAME, null, valve);

        Cursor cursor = db.query(ValveContract.ValvesEntry.TABLE_NAME,
                new String[]{
                        ValveContract.ValvesEntry.COLUMN_NAME_VALVE_TITLE,
                        ValveContract.ValvesEntry.COLUMN_NAME_VALVE_CONTENT},
                ValveContract.ValvesEntry._ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        Valve newValve = cursorToValve(cursor);
        cursor.close();

        return newValve;
    }

    public List<Valve> getValves(String[] columns, String whereClause, String[] conditions, String groupBy, String having, String orderBy){
        List<Valve> comments = new ArrayList<Valve>();

        Cursor cursor = db.query(ValveContract.ValvesEntry.TABLE_NAME,
                columns,
                whereClause, conditions,
                groupBy, having,
                orderBy);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Valve comment = cursorToValve(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    public List<Valve> getValves(String [] columns){
        return getValves(columns, null, null, null, null, null);
    }

    public List<Valve> getValves(){
        return getValves(null);
    }

    public void deleteValve(Valve valve){
        long id = valve.get_id();
        System.out.println("Comment deleted with id: " + id);
        db.delete(ValveContract.ValvesEntry.TABLE_NAME, ValveContract.ValvesEntry._ID
                + " = " + id, null);
    }

    private Valve cursorToValve(Cursor cursor) {
        Valve valve = new Valve();
        valve.set_id(cursor.getLong(0));
        valve.setTitre(cursor.getString(1));
        valve.setContenu(cursor.getString(1));
        return valve;
    }
}
