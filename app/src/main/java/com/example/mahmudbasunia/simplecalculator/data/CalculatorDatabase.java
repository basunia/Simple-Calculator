package com.example.mahmudbasunia.simplecalculator.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.mahmudbasunia.simplecalculator.data.model.CalculationModel;

/**
 * Created by Mahmud Basunia on 6/10/2018.
 */

/**
 * The Room database.
 */
@Database(entities = {CalculationModel.class}, version = 2, exportSchema = false)
public abstract class CalculatorDatabase extends RoomDatabase {

    /**
     * The only instance
     */
    private static CalculatorDatabase sInstance;

    /**
     * Gets the singleton instance of SampleDatabase.
     *
     * @param context The context.
     * @return The singleton instance of SampleDatabase.
     */
    public static synchronized CalculatorDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), CalculatorDatabase.class, "calculators.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sInstance;
    }

    /**
     * @return The DAO for the Cheese table.
     */
    @SuppressWarnings("WeakerAccess")
    public abstract CalculatorDao calculatorDao();



}
