package be.matthieu.mybasicsyncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import be.matthieu.mybasicsyncadapter.authenticator.HenalluxAccountAuthenticator;
import be.matthieu.mybasicsyncadapter.authenticator.ServerAuthenticator;

/**
 * A login screen that offers login via email/password.
 */
public class AccountAuthenticatorActivity extends android.accounts.AccountAuthenticatorActivity{
    public static final String ARG_ACCOUNT_TYPE = "account_type";
    public static final String ARG_AUTH_TYPE = "auth_type";
    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "is_adding_new_account";
    public static final String PARAM_USER_PASS = "password";
    public static final String PARAM_AUTH_TYPE = "signin";

    private static final String ACCOUNT_TYPE = "be.matthieu.mybasicsyncadapter.account";

    private AccountManager accountManager;
    private ServerAuthenticator sServerAuthenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_authenticator);
        accountManager = AccountManager.get(this);
        sServerAuthenticate = new ServerAuthenticator();
        Button signin = (Button)findViewById(R.id.submit);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }


    public void submit() {
        final String userName = ((TextView) findViewById(R.id.accountName)).getText().toString();
        final String userPass = ((TextView) findViewById(R.id.accountPassword)).getText().toString();
        new AsyncTask<Void, Void, Intent>() {
            @Override
            protected Intent doInBackground(Void... params) {
                String authtoken = sServerAuthenticate.userSignIn(userName, userPass, PARAM_AUTH_TYPE);
                final Intent res = new Intent();
                res.putExtra(AccountManager.KEY_ACCOUNT_NAME, userName);
                res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
                res.putExtra(AccountManager.KEY_AUTHTOKEN, authtoken);
                res.putExtra(PARAM_USER_PASS, userPass);
                return res;
            }
            @Override
            protected void onPostExecute(Intent intent) {
                finishLogin(intent);
            }
        }.execute();
    }

    private void finishLogin(Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);

        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = PARAM_AUTH_TYPE;
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            accountManager.addAccountExplicitly(account, accountPassword, null);
            accountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            accountManager.setPassword(account, accountPassword);
        }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }


}