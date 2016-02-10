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

import be.matthieu.mybasicsyncadapter.helpers.ValveReaderHelper;


public class ValveProvider extends ContentProvider {
    // database
    private ValveReaderHelper database;

    // used for the UriMacher
    private static final int VALVES = 10;
    private static final int VALVE_ID = 20;

    private static final String AUTHORITY = "be.matthieu.mybasicsyncadapter.valveprovider";
    private static final String BASE_PATH = "valves";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/valves";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/valve";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, VALVES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", VALVE_ID);
    }

    public ValveProvider() {

    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
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
        int rowsDeleted = 0;
        switch (uriType) {
            case VALVES:
                //rowsDeleted = sqlDB.delete(ValveReaderContract.ValvesEntry.TABLE_NAME, selection,selectionArgs);
                break;
            case VALVE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ValveReaderContract.ValvesEntry.TABLE_NAME,
                            ValveReaderContract.ValvesEntry._ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(ValveReaderContract.ValvesEntry.TABLE_NAME,
                            ValveReaderContract.ValvesEntry._ID + "=" + id
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
                id = sqlDB.insert(ValveReaderContract.ValvesEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) throws IllegalArgumentException{

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(ValveReaderContract.ValvesEntry.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case VALVES:
                Log.e("ValvesProvider", "All valves");
                break;
            case VALVE_ID:
                Log.e("ValvesProvider", "One valve");
                // adding the ID to the original query
                queryBuilder.appendWhere(ValveReaderContract.ValvesEntry._ID + "=" + uri.getLastPathSegment());
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
                rowsUpdated = sqlDB.update(ValveReaderContract.ValvesEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case VALVE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ValveReaderContract.ValvesEntry.TABLE_NAME,
                            values,
                            ValveReaderContract.ValvesEntry._ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(ValveReaderContract.ValvesEntry.TABLE_NAME,
                            values,
                            ValveReaderContract.ValvesEntry._ID + "=" + id
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
                ValveReaderContract.ValvesEntry.COLUMN_NAME_VALVE_TITLE,
                ValveReaderContract.ValvesEntry.COLUMN_NAME_VALVE_CONTENT
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
