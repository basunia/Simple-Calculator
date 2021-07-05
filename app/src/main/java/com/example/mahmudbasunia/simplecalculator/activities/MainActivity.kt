package com.example.mahmudbasunia.simplecalculator.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mahmudbasunia.simplecalculator.CalcApplication
import com.example.mahmudbasunia.simplecalculator.R
import com.example.mahmudbasunia.simplecalculator.data.CalculatorDao
import com.example.mahmudbasunia.simplecalculator.data.CalculatorDatabase
import com.example.mahmudbasunia.simplecalculator.data.model.CalculationModel
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    var compositeDisposable = CompositeDisposable()
    private var display: TextView? = null
    private var operation: Operation? = null
    private var firstNumber = 0.0
    private var decimal: Button? = null
    private val startOver = true

    private enum class Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, STARTOVER, NONE
    }

    var calculatorDao: CalculatorDao? = null

    @kotlin.jvm.JvmField
    @Inject
    var calculatorDatabase: CalculatorDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This is to tell the Activity what layout to use
        setContentView(R.layout.activity_main)
        //initializing dagger
        CalcApplication.component().inject(this)
        calculatorDao = calculatorDatabase!!.calculatorDao()
        display = findViewById<View>(R.id.display) as TextView
        operation = Operation.STARTOVER
        decimal = findViewById<View>(R.id.decimal) as Button
    }

    fun operationButtonClick(view: View) {
        firstNumber = display!!.text.toString().toDouble()
        decimal!!.isEnabled = true
        display!!.text = ""
        var operator: String? = null
        when (view.id) {
            R.id.add -> {
                operator = " + "
                operation = Operation.ADD
            }
            R.id.multiply -> {
                operator = " * "
                operation = Operation.MULTIPLY
            }
            R.id.subtract -> {
                operator = " - "
                operation = Operation.SUBTRACT
            }
            R.id.divide -> {
                operator = " / "
                operation = Operation.DIVIDE
            }
        }
        //display.append(operator);
    }

    fun equalButtonClick(view: View?) {
        decimal!!.isEnabled = true
        val secondNumber: Double = display!!.text.toString().toDouble()
        when (operation) {
            Operation.ADD -> firstNumber += secondNumber
            Operation.SUBTRACT -> firstNumber -= secondNumber
            Operation.MULTIPLY -> firstNumber *= secondNumber
            Operation.DIVIDE -> firstNumber /= secondNumber
            else -> firstNumber = secondNumber
        }
        display!!.text = firstNumber.toString() + ""
        operation = Operation.STARTOVER

        //insrtIntoDatabase(firstNumber);
    }

    fun clearData(view: View?) {
        //Clear the display
        display!!.text = "0"
        operation = Operation.STARTOVER
        decimal!!.isEnabled = true
    }

    fun saveRecords(view: View?) {
        insrtIntoDatabase()
    }

    fun showRecords(view: View?) {
        compositeDisposable.add(calculatorDao!!.allResults
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CalculationModel?>?>() {
                    override fun onSuccess(calculationModels: List<CalculationModel?>) {
                        if (calculationModels.isNotEmpty()) startActivity(Intent(this@MainActivity, RecordsActivity::class.java)) else Toast.makeText(this@MainActivity, "No records saved yet!", Toast.LENGTH_SHORT).show()
                        /*for (CalculationModel model : calculationModels) {
                            Log.d("mahmud", "records: " + model.result);
                        }*/
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mahmud", e.message)
                    }
                }))
    }

    private fun insrtIntoDatabase() {
        if (firstNumber != 0.0) {
            val model = CalculationModel(null, null, null, firstNumber.toString())
            Single.fromCallable { calculatorDao!!.insert(model) }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : SingleObserver<Long?> {
                        override fun onSubscribe(d: Disposable) {}
                        override fun onSuccess(t: Long) {
                            Log.d("mahmud", "Successful")
                        }

                        override fun onError(e: Throwable) {}
                    })
        }
    }

    fun numberButtonClick(view: View) {
        val button = view as Button
        if (operation == Operation.STARTOVER) {
            display!!.text = ""
            operation = Operation.NONE
        }
        if (button.text == "." || display!!.text.toString().contains(".")) decimal!!.isEnabled = false
        display!!.append(button.text.toString() + "")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // This is to tell the activity which menu to use
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        /*if (id == R.id.clear_display) {
            //Clear the display
            display.setText("0");
            operation = Operation.STARTOVER;
            decimal.setEnabled(true);
            return true;
        }*/return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}