package edu.rentals.frontend;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {
    DatePickerDialog picker;
    EditText etStartDate, etEndDate;
    TextView tvStartDate, tvEndDate;
    Button back;
    TextView totalSum;
    private RecyclerView recyclerView;
    private List<Equipment> equipmentList;
    private edu.rentals.frontend.SummaryAdapter eAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // back
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.SummaryActivity.this, AgreementActivity.class);
                startActivity(intent);

            }
        });

        // recycleView
        recyclerView = findViewById(R.id.summaryListRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));

        // adapter
        equipmentList = EquipmentListActivity.getEquipmentList();
        eAdapter = new edu.rentals.frontend.SummaryAdapter(equipmentList);
        recyclerView.setAdapter(eAdapter);

        // set total sum
        totalSum = findViewById(R.id.total);
        totalSum.setText("Total: $" + String.valueOf(edu.rentals.frontend.SummaryAdapter.totalSum()));
//        totalSum.setText("Total: $" + String.valueOf(EquipmentListActivity.getTotal()));

        // select and set start date
        tvStartDate = findViewById(R.id.startDateDisplay);
        etStartDate = findViewById(R.id.startDate);
        etStartDate.setInputType(InputType.TYPE_NULL);
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(edu.rentals.frontend.SummaryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etStartDate.setText( (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
//                                tvStartDate.setText("Start Date: "+ etStartDate.getText());
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        // select and set end date
        tvEndDate = findViewById(R.id.endDateDisplay);
        etEndDate = findViewById(R.id.endDate);
        etEndDate.setInputType(InputType.TYPE_NULL);
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(edu.rentals.frontend.SummaryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etEndDate.setText( (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
//                                tvEndDate.setText("End Date: "+ etEndDate.getText());
                            }
                        }, year, month, day);
                picker.show();
            }
        });


//        Button clearthedata = findViewById(R.id.clearthedatabase);
//
//        clearthedata.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int deletethedata = getContentResolver().delete(OrderContract.OrderEntry.CONTENT_URI, null, null);
//            }
//        });
//
//        getLoaderManager().initLoader(LOADER, null, this);
//
//        ListView listView = findViewById(R.id.list);
//        mAdapter = new CartAdapter(this, null);
//        listView.setAdapter(mAdapter);
    }
}
