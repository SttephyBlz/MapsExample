package com.sttephyblz.mapexample.Util;

import android.app.Application;

import com.squareup.otto.Bus;

/**
 * Created by Sttephy on 22/02/2017.
 */

public class App extends Application{

    private static Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus();
    }

    public static Bus getBus(){
        return bus;
    }

}
