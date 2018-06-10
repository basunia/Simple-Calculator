package com.example.mahmudbasunia.simplecalculator.dagger;

import android.content.Context;

import com.example.mahmudbasunia.simplecalculator.data.CalculatorDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mahmud Basunia on 6/10/2018.
 */

/**
 * Injecting Room
 */
@Module
public class RoomModule {
    private Context context;

    public RoomModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    CalculatorDatabase providesCalculatorDatabase() {
        return CalculatorDatabase.getInstance(context);
    }
}
