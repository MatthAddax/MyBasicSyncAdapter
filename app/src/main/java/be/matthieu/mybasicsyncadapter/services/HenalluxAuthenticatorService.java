package be.matthieu.mybasicsyncadapter.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import be.matthieu.mybasicsyncadapter.authenticator.HenalluxAccountAuthenticator;

/**
 * Created by Matthieu on 12/02/2016.
 */
public class HenalluxAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        HenalluxAccountAuthenticator authenticator = new HenalluxAccountAuthenticator(this);
        return authenticator.getIBinder();
    }
}
