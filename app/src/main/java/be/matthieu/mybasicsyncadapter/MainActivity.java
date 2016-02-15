package be.matthieu.mybasicsyncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import be.matthieu.mybasicsyncadapter.authenticator.ServerAuthenticator;
import be.matthieu.mybasicsyncadapter.contracts.ValveContract;
import be.matthieu.mybasicsyncadapter.helpers.DatabaseHelper;
import be.matthieu.mybasicsyncadapter.managers.NetworkManager;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccountManager accountManager = AccountManager.get(MainActivity.this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.account_type));
        if(accounts.length == 0){
            loadAuthenticatorActivity();
        }
        else{
            syncAccount();
        }
    }

    private void syncAccount() {
    }

    private void loadAuthenticatorActivity() {
        String response = null;

        Intent intent = new Intent(MainActivity.this, AccountAuthenticatorActivity.class);
        intent.putExtra(AccountAuthenticatorActivity.ARG_ACCOUNT_TYPE, MainActivity.this.getString(R.string.account_type));
        intent.putExtra(AccountAuthenticatorActivity.ARG_AUTH_TYPE, AccountAuthenticatorActivity.PARAM_AUTH_TYPE);
        intent.putExtra(AccountAuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        startActivity(intent);
    }

}
