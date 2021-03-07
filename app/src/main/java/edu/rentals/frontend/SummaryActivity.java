package edu.rentals.frontend;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SummaryActivity extends AppCompatActivity {
    static final String TAG = SummaryActivity.class.getSimpleName();
    DatePickerDialog picker;
    EditText etStartDate, etEndDate;
    TextView tvStartDate, tvEndDate;
    Button back, checkOut;
    TextView subTotal, duration, totalSum;
    private RecyclerView recyclerView;
    private List<Equipment> equipmentList;
    private edu.rentals.frontend.SummaryAdapter eAdapter;
    private long storeId;
    private String userId;
    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;
    Calendar startDay, endDay;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // get storeId, userId;
        Intent intent = getIntent();
        storeId = intent.getLongExtra("storeId", 0);
        userId = "1";

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

        // set sub total
        subTotal = findViewById(R.id.subTotal);
        subTotal.setText("Sub Total : $" + String.valueOf(edu.rentals.frontend.SummaryAdapter.totalSum()));

        // set duration
        duration = findViewById(R.id.duration);

        // set total price
        totalSum = findViewById(R.id.total);
        totalSum.setText("Total : $" + String.valueOf(edu.rentals.frontend.SummaryAdapter.totalSum()));

        // set initial start date as today and end date as today + 1
        startDay = Calendar.getInstance();
        endDay = Calendar.getInstance();

        // select and set start date
        tvStartDate = findViewById(R.id.startDateDisplay);
        etStartDate = findViewById(R.id.startDate);
        etStartDate.setInputType(InputType.TYPE_NULL);

        // get today's date
        int day = startDay.get(Calendar.DAY_OF_MONTH);
        int month = startDay.get(Calendar.MONTH);
        int year = startDay.get(Calendar.YEAR);
        etStartDate.setText( (month + 1) + "/" + day + "/" + year);

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Calendar cldr = Calendar.getInstance();
                int day = startDay.get(Calendar.DAY_OF_MONTH);
                int month = startDay.get(Calendar.MONTH);
                int year = startDay.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(edu.rentals.frontend.SummaryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etStartDate.setText( (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                                setCalendar(year, monthOfYear, dayOfMonth, startDay);

                                setTotalPrice(endDay, startDay);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        // select and set end date
        tvEndDate = findViewById(R.id.endDateDisplay);
        etEndDate = findViewById(R.id.endDate);
        etEndDate.setInputType(InputType.TYPE_NULL);
        etEndDate.setText( (month + 1) + "/" + day + "/" + year);
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = endDay.get(Calendar.DAY_OF_MONTH);
                int month = endDay.get(Calendar.MONTH);
                int year = endDay.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(edu.rentals.frontend.SummaryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etEndDate.setText( (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                                setCalendar(year, monthOfYear, dayOfMonth, endDay);

                                setTotalPrice(endDay, startDay);
                            }
                        }, year, month, day);
                picker.show();

            }
        });


        // done button
        checkOut = findViewById(R.id.checkOut);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToLocalDB(v);
                processCheckOut();
                Intent intent = new Intent(edu.rentals.frontend.SummaryActivity.this, CustomerHomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addToLocalDB(View view) {
        Timestamp endTimestamp = new Timestamp(endDay.getTimeInMillis());

        RentalDatabaseHelper rentalDatabaseHelper = new RentalDatabaseHelper(this);
        db = rentalDatabaseHelper.getWritableDatabase();
        int count = SummaryAdapter.getRentalCount();
        try {
            rentalDatabaseHelper.add(db, userId, count, String.valueOf(endTimestamp));
            Log.d("Database addind: ", "userId: " + userId + ", count: "+ String.valueOf(count) + ", dueDate: " + String.valueOf(endTimestamp));
//            Toast toast = Toast.makeText(view.getContext(),
//                    "Database available, adding userId: " + userId + ", count: "+ String.valueOf(count) + " dueDate: " + String.valueOf(endTimestamp),
//                    Toast.LENGTH_SHORT);
//            toast.show();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(view.getContext(),
                    "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        // select * from db where userId == current userId
        db = rentalDatabaseHelper.getReadableDatabase();
        cursor = db.query("rentalDue",
                            new String[] {"userId", "count", "dueDate"},
                            "userId = ?",
                            new String[] {userId},
                            null, null, null);
        if (cursor.moveToFirst()) {
            Log.d("Database query: ",  "userId: " + cursor.getString(0) + ", count: "+ cursor.getString(1) + ", dueDate: " + cursor.getString(2));
            while (cursor.moveToNext()) {
                Log.d("Database query: ",  "userId: " + cursor.getString(0) + ", count: "+ cursor.getString(1) + ", dueDate: " + cursor.getString(2));
            }
        }
    }

    private void processCheckOut() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        Log.d("hi", "hi");
        Timestamp startTimestamp = new Timestamp(startDay.getTimeInMillis());
        Timestamp endTimestamp = new Timestamp(endDay.getTimeInMillis());
        Log.d("startDate", String.valueOf(startTimestamp));
        Log.d("endDate", String.valueOf(endTimestamp));
        ShoppingApiService shoppingApiService = retrofit.create(ShoppingApiService.class);
        ShoppingCheckoutRental rental = new ShoppingCheckoutRental(storeId, userId, startTimestamp, endTimestamp, SummaryAdapter.getRentalSummaryList());
        Call<ShoppingCheckoutRental> call = shoppingApiService.createRental(rental);
        call.enqueue(new Callback<ShoppingCheckoutRental>() {
            @Override
            public void onResponse(Call<ShoppingCheckoutRental> call, Response<ShoppingCheckoutRental> response) {

            }

            @Override
            public void onFailure(Call<ShoppingCheckoutRental> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void setCalendar(int year, int monthOfYear, int dayOfMonth, Calendar Day) {
        Day.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Day.set(Calendar.MONTH, monthOfYear);
        Day.set(Calendar.YEAR, year);
    }


    private void setTotalPrice(Calendar endDay, Calendar startDay) {
        // change total price when startDay or endDay changes
        long diff = endDay.getTimeInMillis() - startDay.getTimeInMillis();
        long days = 1 + diff / (1000 * 60 * 60 * 24);

        // start date >= today
        boolean validStartDate;
        if ((startDay.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / (1000 * 60 * 60 * 24) >= 0) {
            validStartDate = true;
        } else {
            validStartDate = false;
        }

        if (days > 0 && validStartDate) {
            totalSum.setText("Total: $" + String.valueOf(SummaryAdapter.totalSum() * days));
            //set duration
            duration.setText("Duration (day) : " + days);
        } else {
            totalSum.setText("Total: please select proper dates");
            //set duration
            duration.setText("Duration (day) : 0");
        }

    }
}
