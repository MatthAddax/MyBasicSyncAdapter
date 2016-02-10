package be.matthieu.mybasicsyncadapter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;

import be.matthieu.mybasicsyncadapter.helpers.ValveReaderHelper;
import be.matthieu.mybasicsyncadapter.providers.ValveProvider;
import be.matthieu.mybasicsyncadapter.providers.ValveReaderContract;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ValveReaderHelper mValveReaderHelper = new ValveReaderHelper(MainActivity.this);
        SQLiteDatabase db = mValveReaderHelper.getWritableDatabase();
        /*for(int i = 0; i<20; i++){
            ContentValues values = new ContentValues();
            values.put(ValveReaderContract.ValvesEntry.COLUMN_NAME_VALVE_TITLE, "Titre " + i);
            values.put(ValveReaderContract.ValvesEntry.COLUMN_NAME_VALVE_CONTENT, "Contenu numéro " + i);
            db.insert(ValveReaderContract.ValvesEntry.TABLE_NAME, null, values);

        }*/

        // Create a new map of values, where column names are the key
        Uri.Builder builder = new Uri.Builder();
        builder.appendPath("be.matthieu.mybasicsyncadapter.valveprovider");
        builder.appendPath("valves");
        Uri uri = builder.build();
        Cursor cursor = managedQuery(uri, new String[]{ValveReaderContract.ValvesEntry.COLUMN_NAME_VALVE_TITLE}, null, null, null);
        if(cursor!= null) {
            while (cursor.moveToNext()) {
                Log.d("Titre valve ", cursor.getColumnName(cursor.getColumnIndex(ValveReaderContract.ValvesEntry.COLUMN_NAME_VALVE_TITLE)));
            }
        }else{
            Log.e("Cursor", "Cursor is null");
        }
    }

    class AjoutDansBdd extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
