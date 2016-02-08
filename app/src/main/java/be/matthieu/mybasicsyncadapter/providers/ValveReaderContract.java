package be.matthieu.mybasicsyncadapter.providers;

import android.provider.BaseColumns;

/**
 * Created by Matthieu on 02-02-16  .
 */
public class ValveReaderContract {
    /*Definition de la table*/
    public static abstract class ValvesEntry implements BaseColumns {
        public static final String TABLE_NAME = "valves";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_VALVE_TITLE = "valve_title";
        public static final String COLUMN_NAME_VALVE_CONTENT = "valve_content";
    }
    /*Definition des requets de base*/
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    /**
     * Créer table
     * */
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ValvesEntry.TABLE_NAME + " (" +
                    ValvesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ValvesEntry.COLUMN_NAME_VALVE_TITLE + TEXT_TYPE + COMMA_SEP +
                    ValvesEntry.COLUMN_NAME_VALVE_CONTENT + TEXT_TYPE +
            " )";
    /**
     * Effacer table si existe (lors de la création)
     * */
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ValvesEntry.TABLE_NAME;

    public ValveReaderContract(){};


}