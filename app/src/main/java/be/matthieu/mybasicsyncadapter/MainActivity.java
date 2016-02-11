package be.matthieu.mybasicsyncadapter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import be.matthieu.mybasicsyncadapter.contracts.ValveContract;
import be.matthieu.mybasicsyncadapter.helpers.DatabaseHelper;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        /*for(int i = 0; i<20; i++){
            ContentValues values = new ContentValues();
            values.put(ValveContract.ValvesEntry.COLUMN_NAME_VALVE_TITLE, "Titre " + i);
            values.put(ValveContract.ValvesEntry.COLUMN_NAME_VALVE_CONTENT, "Contenu numéro " + i);
            db.insert(ValveContract.ValvesEntry.TABLE_NAME, null, values);

        }*/

        // Create a new map of values, where column names are the key
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("content");
        builder.authority("be.matthieu.mybasicsyncadapter.valveprovider");
        builder.appendPath("valves");
        Uri uri = builder.build();
        Log.d("URI Authority String", uri.getAuthority());
        Log.d("URI Lastpathseg String", uri.getLastPathSegment().toString());
        Log.d("URI", uri.toString());
        /*
        Cursor cursor = getContentResolver().query(uri, new String[]{ValveContract.ValvesEntry.COLUMN_NAME_VALVE_TITLE}, null, null, null);
        if(cursor!= null) {
            while (cursor.moveToNext()) {
                Log.d("Titre valve ", cursor.getColumnName(cursor.getColumnIndex(ValveContract.ValvesEntry.COLUMN_NAME_VALVE_TITLE)));
            }
        }else{
            Log.e("Cursor", "Cursor is null");
        }*/
    }

    class AjoutDansBdd extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
