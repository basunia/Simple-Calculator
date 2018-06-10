package com.example.mahmudbasunia.simplecalculator.dagger;

import com.example.mahmudbasunia.simplecalculator.activities.MainActivity;
import com.example.mahmudbasunia.simplecalculator.activities.RecordsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Mahmud Basunia on 6/10/2018.
 */

@Singleton
@Component(modules = RoomModule.class)
public interface AppComponent {

    void inject(MainActivity mainActivity);
    void inject(RecordsActivity recordsActivity);

}
