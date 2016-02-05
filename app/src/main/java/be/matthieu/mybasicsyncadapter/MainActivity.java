package be.matthieu.mybasicsyncadapter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import be.matthieu.mybasicsyncadapter.helpers.ValveReaderHelper;
import be.matthieu.mybasicsyncadapter.providers.ValveReaderContract;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ValveReaderHelper mValveReaderHelper = new ValveReaderHelper(MainActivity.this);
        SQLiteDatabase db = mValveReaderHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
    }

    class AjoutDansBdd extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
