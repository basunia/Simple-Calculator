package com.example.mahmudbasunia.simplecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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

    public void operationButtonClick(View view) {
        firstNumber = Double.parseDouble(display.getText().toString());
        decimal.setEnabled(true);
        display.setText("");
        switch (view.getId()) {
            case R.id.add:
                operation = Operation.ADD;
                break;
            case R.id.multiply:
                operation = Operation.MULTIPLY;
                break;
            case R.id.subtract:
                operation = Operation.SUBTRACT;
                break;
            case R.id.divide:
                operation = Operation.DIVIDE;
                break;
        }
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This is to tell the Activity what layout to use
        setContentView(R.layout.activity_main);
        display = (TextView) findViewById(R.id.display);
        operation = Operation.STARTOVER;
        decimal = (Button) findViewById(R.id.decimal);
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
}
