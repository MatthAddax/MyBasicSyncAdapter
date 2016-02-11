package be.matthieu.mybasicsyncadapter.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

import be.matthieu.mybasicsyncadapter.contracts.HoraireContract;
import be.matthieu.mybasicsyncadapter.contracts.ValveContract;
import be.matthieu.mybasicsyncadapter.helpers.DatabaseHelper;


public class HenalluxProvider extends ContentProvider {
    // database
    private DatabaseHelper database;

    // used for the UriMacher
    //valve
    private static final int VALVES = 10;
    private static final int VALVE_ID = 11;

    //horaire
    private static final int HORAIRES = 20;
    private static final int HORAIRE_ID = 21;


    private static final String AUTHORITY = "be.matthieu.mybasicsyncadapter.valveprovider";

    public static final Uri CONTENT_URI_VALVE = Uri.parse("content://" + AUTHORITY + "/" + ValveContract.BASE_PATH);

    public static final String CONTENT_TYPE_VALVE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/valves";
    public static final String CONTENT_ITEM_VALVE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/valve";

    public static final String CONTENT_TYPE_HORAIRE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/horaires";
    public static final String CONTENT_ITEM_HORAIRE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/horaire";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, ValveContract.BASE_PATH, VALVES);
        sURIMatcher.addURI(AUTHORITY, ValveContract.BASE_PATH + "/#", VALVE_ID);

        sURIMatcher.addURI(AUTHORITY, HoraireContract.BASE_PATH, HORAIRES);
        sURIMatcher.addURI(AUTHORITY, HoraireContract.BASE_PATH + "/#", HORAIRE_ID);
    }

    public HenalluxProvider() {

    }

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());
        return false;
    }

    /**
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) throws IllegalArgumentException {
        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase sqlDB = database.getWritableDatabase();
        String id;
        int rowsDeleted = 0;
        switch (uriType) {
            case VALVES:
                rowsDeleted = sqlDB.delete(ValveContract.ValvesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VALVE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ValveContract.ValvesEntry.TABLE_NAME,
                            ValveContract.ValvesEntry._ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(ValveContract.ValvesEntry.TABLE_NAME,
                            ValveContract.ValvesEntry._ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case HORAIRES:
                rowsDeleted = sqlDB.delete(HoraireContract.HoraireEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HORAIRE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(HoraireContract.HoraireEntry.TABLE_NAME,
                            HoraireContract.HoraireEntry._ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(HoraireContract.HoraireEntry.TABLE_NAME,
                            HoraireContract.HoraireEntry._ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) throws IllegalArgumentException {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case VALVES:
                id = sqlDB.insert(ValveContract.ValvesEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        /* change to adapt URI*/
        return Uri.parse(ValveContract.BASE_PATH + "/" + id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) throws IllegalArgumentException{

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(ValveContract.ValvesEntry.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case VALVES:
                Log.e("ValvesProvider", "All valves");
                break;
            case VALVE_ID:
                Log.e("ValvesProvider", "One valve");
                // adding the ID to the original query
                queryBuilder.appendWhere(ValveContract.ValvesEntry._ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case VALVES:
                rowsUpdated = sqlDB.update(ValveContract.ValvesEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case VALVE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ValveContract.ValvesEntry.TABLE_NAME,
                            values,
                            ValveContract.ValvesEntry._ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(ValveContract.ValvesEntry.TABLE_NAME,
                            values,
                            ValveContract.ValvesEntry._ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {
                ValveContract.ValvesEntry.COLUMN_NAME_VALVE_TITLE,
                ValveContract.ValvesEntry.COLUMN_NAME_VALVE_CONTENT
        };

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
