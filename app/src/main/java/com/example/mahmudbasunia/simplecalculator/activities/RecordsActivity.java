package com.example.mahmudbasunia.simplecalculator.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mahmudbasunia.simplecalculator.CalcApplication;
import com.example.mahmudbasunia.simplecalculator.R;
import com.example.mahmudbasunia.simplecalculator.data.CalculatorDao;
import com.example.mahmudbasunia.simplecalculator.data.CalculatorDatabase;
import com.example.mahmudbasunia.simplecalculator.data.model.CalculationModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mahmud Basunia on 6/11/2018.
 */

public class RecordsActivity extends AppCompatActivity {

    private List<CalculationModel> calculationList;
    private CalculatorAdapter calculatorAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Inject
    CalculatorDatabase calculatorDatabase;
    CalculatorDao calculatorDao;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        ButterKnife.bind(this);
        calculationList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        calculatorAdapter = new CalculatorAdapter(this);
        recyclerView.setAdapter(calculatorAdapter);

        /*Injecting Dagger*/
        CalcApplication.component().inject(this);
        calculatorDao = calculatorDatabase.calculatorDao();

        compositeDisposable.add(calculatorDao.getAllResults()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<CalculationModel>>() {
                    @Override
                    public void onSuccess(List<CalculationModel> calculationModels) {
                        calculationList.clear();
                        calculationList.addAll(calculationModels);
                        calculatorAdapter.notifyDataSetChanged();
                        for (CalculationModel model : calculationModels) {
                            Log.d("mahmud", "records: " + model.result);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("mahmud", e.getMessage());
                    }
                }));


    }

    private class CalculatorAdapter extends RecyclerView.Adapter<CalculatorAdapter.RecordHolder> {
        Context context;
        public CalculatorAdapter(Context context) {
            super();
            this.context = context;
        }

        @NonNull
        @Override
        public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.records_item, parent, false);

            return new RecordHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
            CalculationModel model = calculationList.get(position);
            holder.id.setText("" + model.id);
            holder.result.setText("" + model.result);
        }

        @Override
        public int getItemCount() {
            return calculationList.size();
        }

        class RecordHolder extends RecyclerView.ViewHolder {
            TextView id, result;

            public RecordHolder(View itemView) {
                super(itemView);
                id = itemView.findViewById(R.id.recordId);
                result = itemView.findViewById(R.id.recordResults);
            }
        }
    }
}
