package be.matthieu.mybasicsyncadapter.contracts;

import android.provider.BaseColumns;

/**
 * Created by Matthieu on 02-02-16  .
 */
public class ValveContract extends BaseContract{
    public static final String BASE_PATH = "valves";

    /*Definition de la table*/
    public static abstract class ValvesEntry implements BaseColumns {
        public static final String TABLE_NAME = "valves";
        public static final String COLUMN_NAME_VALVE_TITLE = "valve_title";
        public static final String COLUMN_NAME_VALVE_CONTENT = "valve_content";
        public static final String COLUMN_NAME_VALVE_CREATION = "valve_writing_date_unix";
        public static final String COLUMN_NAME_VALVE_UPDATE = "valve_update_date_unix";
    }

    /**
     * Créer table
     * */
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ValvesEntry.TABLE_NAME + " (" +
                    ValvesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ValvesEntry.COLUMN_NAME_VALVE_TITLE + TEXT_TYPE + COMMA_SEP +
                    ValvesEntry.COLUMN_NAME_VALVE_CONTENT + TEXT_TYPE + COMMA_SEP +
                    ValvesEntry.COLUMN_NAME_VALVE_CREATION + INT_TYPE + COMMA_SEP +
                    ValvesEntry.COLUMN_NAME_VALVE_UPDATE + INT_TYPE +
            " )";
    /**
     * Effacer table si existe (lors de la création)
     * */
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ValvesEntry.TABLE_NAME;

    public ValveContract(){};


}