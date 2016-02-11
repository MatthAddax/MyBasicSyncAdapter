package be.matthieu.mybasicsyncadapter.contracts;

import android.provider.BaseColumns;

/**
 * Created by Matthieu on 10/02/2016.
 */
public class HoraireContract extends BaseContract{
    public static final String BASE_PATH = "horaires";


    public static abstract class HoraireEntry implements BaseColumns {
        public static final String TABLE_NAME = "horaires";
        public static final String COLUMN_NAME_HORAIRE_START_TIME = "start_unix";
        public static final String COLUMN_NAME_HORAIRE_END_TIME = "end_unix";
        public static final String COLUMN_NAME_HORAIRE_LOCAL = "local";
        public static final String COLUMN_NAME_HORAIRE_PROF = "professeur";
        public static final String COLUMN_NAME_HORAIRE_INTITULE = "intitule";
    }

    /**
     * Créer table
     * */
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HoraireEntry.TABLE_NAME + " (" +
                    HoraireEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    HoraireEntry.COLUMN_NAME_HORAIRE_START_TIME + INT_TYPE + COMMA_SEP +
                    HoraireEntry.COLUMN_NAME_HORAIRE_END_TIME + INT_TYPE + COMMA_SEP +
                    HoraireEntry.COLUMN_NAME_HORAIRE_INTITULE + TEXT_TYPE + COMMA_SEP +
                    HoraireEntry.COLUMN_NAME_HORAIRE_LOCAL + TEXT_TYPE + COMMA_SEP +
                    HoraireEntry.COLUMN_NAME_HORAIRE_PROF + TEXT_TYPE +
                    " )";
    /**
     * Effacer table si existe (lors de la création)
     * */
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HoraireEntry.TABLE_NAME;

    public HoraireContract(){};
}
