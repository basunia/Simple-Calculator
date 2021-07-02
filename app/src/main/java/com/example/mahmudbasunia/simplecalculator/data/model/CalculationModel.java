package com.example.mahmudbasunia.simplecalculator.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.provider.BaseColumns;

/**
 * Created by Mahmud Basunia on 6/10/2018.
 */

@Entity(tableName = CalculationModel.TABLE_NAME)
public class CalculationModel {
    public static final String TABLE_NAME = "calculation";

    private static final String COLUMN_ID = BaseColumns._ID;
    private static final String COLUMN_FIRST_OPERAND = "first";
    private static final String COLUMN_SECOND_OPERAND = "second";
    private static final String COLUMN_OPERATOR = "operator";
    private static final String RESULT = "result";

    /** The unique ID of the question. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    @ColumnInfo(name = COLUMN_FIRST_OPERAND)
    public String firstOperand;

    @ColumnInfo(name = COLUMN_SECOND_OPERAND)
    public String secondOperand;

    @ColumnInfo(name = COLUMN_OPERATOR)
    public String operator;

    @ColumnInfo(name = RESULT)
    public String result;

    public CalculationModel(String firstOperand, String secondOperand, String operator, String result) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.operator = operator;
        this.result = result;
    }
}
