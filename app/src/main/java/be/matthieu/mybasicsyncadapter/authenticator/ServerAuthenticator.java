package be.matthieu.mybasicsyncadapter.authenticator;

/**
 * Created by Matthieu on 12/02/2016.
 */
public class ServerAuthenticator {
    public String userSignIn(String name, String password, String authType){
        return name + ":" + password + ":"  + authType;
    }
}
