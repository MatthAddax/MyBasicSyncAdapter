package be.matthieu.mybasicsyncadapter.authenticator;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import be.matthieu.mybasicsyncadapter.managers.NetworkManager;

public class ServerAuthenticator {
    public static final String SERVER_URL = "url_de_mon_serveur";

    private static final String SIGNIN_BASE_PATH = "/signin/index.php";
    private static final String ARG_AUTH_TOKEN = "auth_token";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_PASSWORD = "password";
    public String userSignIn(String username, String password, String authType){
        NetworkManager networkManager = new NetworkManager();
        String post_params = "username=" + username + "&password=" + password;
        try {
            return networkManager.sendPOST(SIGNIN_BASE_PATH, post_params);
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
            return null;
        }
    }
}
