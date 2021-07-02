package com.example.mahmudbasunia.simplecalculator.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmudbasunia.simplecalculator.CalcApplication;
import com.example.mahmudbasunia.simplecalculator.R;
import com.example.mahmudbasunia.simplecalculator.data.CalculatorDao;
import com.example.mahmudbasunia.simplecalculator.data.CalculatorDatabase;
import com.example.mahmudbasunia.simplecalculator.data.model.CalculationModel;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView display;
    private Operation operation;
    private double firstNumber;
    private Button decimal;
    private boolean startOver = true;

    private enum Operation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        STARTOVER,
        NONE
    }

    CalculatorDao calculatorDao;
    @Inject
    CalculatorDatabase calculatorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This is to tell the Activity what layout to use
        setContentView(R.layout.activity_main);
        //initializing dagger
        CalcApplication.component().inject(this);

        calculatorDao = calculatorDatabase.calculatorDao();

        display = (TextView) findViewById(R.id.display);
        operation = Operation.STARTOVER;
        decimal = (Button) findViewById(R.id.decimal);
    }

    public void operationButtonClick(View view) {
        firstNumber = Double.parseDouble(display.getText().toString());
        decimal.setEnabled(true);
        display.setText("");
        String operator = null;
        switch (view.getId()) {
            case R.id.add:
                operator = " + ";
                operation = Operation.ADD;
                break;
            case R.id.multiply:
                operator = " * ";
                operation = Operation.MULTIPLY;
                break;
            case R.id.subtract:
                operator = " - ";
                operation = Operation.SUBTRACT;
                break;
            case R.id.divide:
                operator = " / ";
                operation = Operation.DIVIDE;
                break;
        }
        //display.append(operator);
    }

    public void equalButtonClick(View view) {
        decimal.setEnabled(true);
        double secondNumber = Double.parseDouble(display.getText().toString());

        switch (operation) {
            case ADD:
                firstNumber += secondNumber;
                break;
            case SUBTRACT:
                firstNumber -= secondNumber;
                break;
            case MULTIPLY:
                firstNumber *= secondNumber;
                break;
            case DIVIDE:
                firstNumber /= secondNumber;
                break;
            default:
                firstNumber = secondNumber;
        }
        display.setText(firstNumber + "");
        operation = Operation.STARTOVER;

        //insrtIntoDatabase(firstNumber);
    }

    public void clearData(View view) {
        //Clear the display
        display.setText("0");
        operation = Operation.STARTOVER;
        decimal.setEnabled(true);
    }

    public void saveRecords(View view) {
        insrtIntoDatabase();
    }

    public void showRecords(View view) {
        compositeDisposable.add(calculatorDao.getAllResults()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<CalculationModel>>() {
                    @Override
                    public void onSuccess(List<CalculationModel> calculationModels) {
                        if (calculationModels.size() > 0)
                            startActivity(new Intent(MainActivity.this, RecordsActivity.class));
                        else Toast.makeText(MainActivity.this, "No records saved yet!", Toast.LENGTH_SHORT).show();
                        /*for (CalculationModel model : calculationModels) {
                            Log.d("mahmud", "records: " + model.result);
                        }*/

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("mahmud", e.getMessage());
                    }
                }));
    }

    private void insrtIntoDatabase() {
        if (firstNumber != 0) {
            final CalculationModel model = new CalculationModel(null, null, null, String.valueOf(firstNumber));
            Single.fromCallable(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return calculatorDao.insert(model);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new SingleObserver<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Long aLong) {
                            Log.d("mahmud", "Successful");
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

        }
    }

    public void numberButtonClick(View view) {
        Button button = (Button) view;
        if (operation == Operation.STARTOVER) {
            display.setText("");
            operation = Operation.NONE;
        }
        if (button.getText().equals(".") || display.getText().toString().contains("."))
            decimal.setEnabled(false);
        display.append(button.getText() + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This is to tell the activity which menu to use
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.clear_display) {
            //Clear the display
            display.setText("0");
            operation = Operation.STARTOVER;
            decimal.setEnabled(true);
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
