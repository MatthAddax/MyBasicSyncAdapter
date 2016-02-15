package be.matthieu.mybasicsyncadapter.managers;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Matthieu on 8/01/2016.
 */
public class NetworkManager {
    private static final String USER_AGENT = "";

    private static final String GET_URL = "http://portail.net23.net/";

    private static final String POST_URL = "http://portail.net23.net/";



    public String sendGET(String url_path) throws IOException {
        URL obj = new URL(GET_URL + url_path);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        Log.v(this.getClass().getSimpleName(), "GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            return (response.toString());
        } else {
            Log.d(this.getClass().getSimpleName(), "GET request not worked");
            return null;
        }

    }

    public String sendPOST(String url_path) throws IOException{
        return sendPOST(url_path, "");
    }
    public String sendPOST(String url_path, String post_params) throws IOException {
        URL obj = new URL(POST_URL + url_path);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(post_params.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        Log.v(this.getClass().getSimpleName(), "POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            return (response.toString());
        } else {
            Log.d(this.getClass().getSimpleName(), "POST request not worked");
            return null;
        }
    }

    public JSONObject jsonHTTPRequest(String url_path, String post_params) throws IOException, JSONException {
        String json_string = sendPOST(url_path, post_params);
        return new JSONObject(json_string);
    }

}
