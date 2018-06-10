package com.example.mahmudbasunia.simplecalculator;

import android.app.Application;
import android.content.Context;

import com.example.mahmudbasunia.simplecalculator.dagger.AppComponent;
import com.example.mahmudbasunia.simplecalculator.dagger.DaggerAppComponent;
import com.example.mahmudbasunia.simplecalculator.dagger.RoomModule;

/**
 * Created by Mahmud Basunia on 6/10/2018.
 */

public class CalcApplication extends Application {

    protected static AppComponent appComponent;

    public static AppComponent component() {
        return appComponent;
    }

    private AppComponent init(Context context) {
        return DaggerAppComponent.builder()
                .roomModule(new RoomModule(context))
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize Dagger component
        appComponent = init(this);

    }
}
