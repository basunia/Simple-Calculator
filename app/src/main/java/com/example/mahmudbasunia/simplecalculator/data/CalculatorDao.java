package com.example.mahmudbasunia.simplecalculator.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.mahmudbasunia.simplecalculator.data.model.CalculationModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Mahmud Basunia on 6/10/2018.
 */

@Dao
public interface CalculatorDao {

    @Insert
    Long insert(CalculationModel calculationModel);

    @Query("SELECT * FROM " + CalculationModel.TABLE_NAME)
    Single<List<CalculationModel>> getAllResults();

}
