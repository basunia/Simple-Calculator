package com.example.mahmudbasunia.simplecalculator.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import butterknife.BindView
import butterknife.ButterKnife
import com.example.mahmudbasunia.simplecalculator.CalcApplication
import com.example.mahmudbasunia.simplecalculator.R
import com.example.mahmudbasunia.simplecalculator.data.CalculatorDao
import com.example.mahmudbasunia.simplecalculator.data.CalculatorDatabase
import com.example.mahmudbasunia.simplecalculator.data.model.CalculationModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by Mahmud Basunia on 6/11/2018.
 */
class RecordsActivity : AppCompatActivity() {
    private var calculationList: MutableList<CalculationModel?>? = null
    private var calculatorAdapter: CalculatorAdapter? = null

    @kotlin.jvm.JvmField
    @Inject
    var calculatorDatabase: CalculatorDatabase? = null
    var calculatorDao: CalculatorDao? = null
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)
        ButterKnife.bind(this)
        calculationList = ArrayList()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        calculatorAdapter = CalculatorAdapter(this)
        recyclerView!!.adapter = calculatorAdapter

        /*Injecting Dagger*/CalcApplication.component().inject(this)
        calculatorDao = calculatorDatabase!!.calculatorDao()
        compositeDisposable.add(calculatorDao!!.allResults
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CalculationModel?>?>() {
                    override fun onSuccess(calculationModels: List<CalculationModel?>) {
                        calculationList?.clear()
                        calculationList?.addAll(calculationModels)
                        calculatorAdapter!!.notifyDataSetChanged()
                        for (model in calculationModels) {
                            Log.d("mahmud", "records: " + model?.result)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("mahmud", e.message)
                    }
                }))
    }

    private inner class CalculatorAdapter(var context: Context) : RecyclerView.Adapter<CalculatorAdapter.RecordHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.records_item, parent, false)
            return RecordHolder(view)
        }

        override fun onBindViewHolder(holder: RecordHolder, position: Int) {
            val model = calculationList!![position]
            holder.id.text = "" + model?.id
            holder.result.text = "" + model?.result
        }

        override fun getItemCount(): Int {
            return calculationList!!.size
        }

        internal inner class RecordHolder(itemView: View) : ViewHolder(itemView) {
            var id: TextView
            var result: TextView

            init {
                id = itemView.findViewById(R.id.recordId)
                result = itemView.findViewById(R.id.recordResults)
            }
        }

    }
}