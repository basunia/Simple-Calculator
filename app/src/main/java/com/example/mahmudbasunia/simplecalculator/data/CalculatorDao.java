package com.example.mahmudbasunia.simplecalculator.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mahmudbasunia.simplecalculator.data.model.CalculationModel;

import java.util.List;

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
