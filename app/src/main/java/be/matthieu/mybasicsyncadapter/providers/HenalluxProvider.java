package be.matthieu.mybasicsyncadapter.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
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


    private static final String AUTHORITY = "be.matthieu.mybasicsyncadapter.henalluxprovider";

    public static final Uri CONTENT_URI_VALVE = Uri.parse("content://" + AUTHORITY + "/" + ValveContract.BASE_PATH);
    public static final Uri CONTENT_URI_HORAIRE = Uri.parse("content://" + AUTHORITY + "/" + HoraireContract.BASE_PATH);

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
     * Implement this to handle requests to insert a new row.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after inserting.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     *
     * @param uri    The content:// URI of the insertion request. This must not be {@code null}.
     * @param values A set of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     * @return The URI for the newly inserted item.
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqliDB = database.getWritableDatabase();

        String table, base_path;
        switch(uriType){
            case VALVES:
            case VALVE_ID:
                table = ValveContract.ValvesEntry.TABLE_NAME;
                base_path = ValveContract.BASE_PATH;
                break;
            case HORAIRES:
            case HORAIRE_ID:
                table = HoraireContract.HoraireEntry.TABLE_NAME;
                base_path = HoraireContract.BASE_PATH;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }


        long insertedID = sqliDB.insert(table, null, values);
        sqliDB.close();

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(base_path + "/" + insertedID);
    }

    /**
     * Implement this to handle requests to delete one or more rows.
     * The implementation should apply the selection clause when performing
     * deletion, allowing the operation to affect multiple rows in a directory.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after deleting.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * <p>The implementation is responsible for parsing out a row ID at the end
     * of the URI, if a specific row is being deleted. That is, the client would
     * pass in <code>content://contacts/people/22</code> and the implementation is
     * responsible for parsing the record number (22) when creating a SQL statement.
     *
     * @param uri           The full URI to query, including a row ID (if a specific record is requested).
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs
     * @return The number of rows affected.
     * @throws SQLException
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) throws SQLException{
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqliDB = database.getWritableDatabase();

        int id;
        String table, whereClause = "";

        switch(uriType){
            case VALVES:
                table = ValveContract.ValvesEntry.TABLE_NAME;
                break;
            case HORAIRES:
                table = HoraireContract.HoraireEntry.TABLE_NAME;
                break;
            case VALVE_ID:
                id = Integer.parseInt(uri.getLastPathSegment());
                table = ValveContract.ValvesEntry.TABLE_NAME;
                whereClause = "_ID = " + id + " AND ";
                break;
            case HORAIRE_ID:
                id = Integer.parseInt(uri.getLastPathSegment());
                table = HoraireContract.HoraireEntry.TABLE_NAME;
                whereClause = "_ID = " + id + "  AND ";
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if(!TextUtils.isEmpty(selection))
            whereClause += selection;

        int deletedRows = sqliDB.delete(table, whereClause, selectionArgs);

        sqliDB.close();
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    /**
     * Implement this to handle requests to update one or more rows.
     * The implementation should update all rows matching the selection
     * to set the columns according to the provided values map.
     * As a courtesy, call {@link ContentResolver#notifyChange(Uri, ContentObserver) notifyChange()}
     * after updating.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     *
     * @param uri           The URI to query. This can potentially have a record ID if this
     *                      is an update request for a specific record.
     * @param values        A set of column_name/value pairs to update in the database.
     *                      This must not be {@code null}.
     * @param selection     An optional filter to match rows to update.
     * @param selectionArgs
     * @return the number of rows affected.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqliDB = database.getWritableDatabase();

        int id;
        String table, whereClause = "";

        switch(uriType){
            case VALVES:
                table = ValveContract.ValvesEntry.TABLE_NAME;
                break;
            case HORAIRES:
                table = HoraireContract.HoraireEntry.TABLE_NAME;
                break;
            case VALVE_ID:
                id = Integer.parseInt(uri.getLastPathSegment());
                table = ValveContract.ValvesEntry.TABLE_NAME;
                whereClause = "_ID = " + id + " AND ";
                break;
            case HORAIRE_ID:
                id = Integer.parseInt(uri.getLastPathSegment());
                table = HoraireContract.HoraireEntry.TABLE_NAME;
                whereClause = "_ID = " + id + "  AND ";
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if(!TextUtils.isEmpty(selection))
            whereClause += selection;

        int updatedRows = sqliDB.update(table, values, whereClause, selectionArgs);

        sqliDB.close();
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.  The returned MIME type should start with
     * <code>vnd.android.cursor.item</code> for a single record,
     * or <code>vnd.android.cursor.dir/</code> for multiple items.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * <p>Note that there are no permissions needed for an application to
     * access this information; if your content provider requires read and/or
     * write permissions, or is not exported, all applications can still call
     * this method regardless of their access permissions.  This allows them
     * to retrieve the MIME type for a URI when dispatching intents.
     *
     * @param uri the URI to query.
     * @return a MIME type string, or {@code null} if there is no type.
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * Implement this to handle query requests from clients.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * Example client call:<p>
     * <pre>// Request a specific record.
     * Cursor managedCursor = managedQuery(
     * ContentUris.withAppendedId(Contacts.People.CONTENT_URI, 2),
     * projection,    // Which columns to return.
     * null,          // WHERE clause.
     * null,          // WHERE clause value substitution
     * People.NAME + " ASC");   // Sort order.</pre>
     * Example implementation:<p>
     * <pre>// SQLiteQueryBuilder is a helper class that creates the
     * // proper SQL syntax for us.
     * SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
     * <p/>
     * // Set the table we're querying.
     * qBuilder.setTables(DATABASE_TABLE_NAME);
     * <p/>
     * // If the query ends in a specific record number, we're
     * // being asked for a specific record, so set the
     * // WHERE clause in our query.
     * if((URI_MATCHER.match(uri)) == SPECIFIC_MESSAGE){
     * qBuilder.appendWhere("_id=" + uri.getPathLeafId());
     * }
     * <p/>
     * // Make the query.
     * Cursor c = qBuilder.query(mDb,
     * projection,
     * selection,
     * selectionArgs,
     * groupBy,
     * having,
     * sortOrder);
     * c.setNotificationUri(getContext().getContentResolver(), uri);
     * return c;</pre>
     *
     * @param uri           The URI to query. This will be the full URI sent by the client;
     *                      if the client is requesting a specific record, the URI will end in a record number
     *                      that the implementation should parse and add to a WHERE or HAVING clause, specifying
     *                      that _id value.
     * @param projection    The list of columns to put into the cursor. If
     *                      {@code null} all columns are included.
     * @param selection     A selection criteria to apply when filtering rows.
     *                      If {@code null} then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder     How the rows in the cursor should be sorted.
     *                      If {@code null} then the provider is free to define the sort order.
     * @return a Cursor or {@code null}.
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        //checkColumns(projection);

        // Set the table

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case VALVES:
                queryBuilder.setTables(ValveContract.ValvesEntry.TABLE_NAME);
                break;
            case HORAIRES:
                queryBuilder.setTables(HoraireContract.HoraireEntry.TABLE_NAME);
                break;
            case VALVE_ID:
                queryBuilder.setTables(ValveContract.ValvesEntry.TABLE_NAME);
                queryBuilder.appendWhere(ValveContract.ValvesEntry._ID + "="
                        + uri.getLastPathSegment());
                // adding the ID to the original query
                break;
            case HORAIRE_ID:
                queryBuilder.setTables(HoraireContract.HoraireEntry.TABLE_NAME);
                queryBuilder.appendWhere(HoraireContract.HoraireEntry._ID + "="
                        + uri.getLastPathSegment());
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
