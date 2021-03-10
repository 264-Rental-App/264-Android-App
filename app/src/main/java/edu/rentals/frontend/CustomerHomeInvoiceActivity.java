package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerHomeInvoiceActivity extends AppCompatActivity {
    static final String TAG = EquipmentListActivity.class.getSimpleName();
    static final String BASE_URL = "http://35.222.193.76/";
    static Retrofit retrofit = null;
    private Long invoiceId;
    Button back;
    TextView tvStoreName, tvStartDate, tvDueDate, tvTotalCost;
    CustomerHomeInvoiceAdapter eAdapter;
    private RecyclerView recyclerView;
    List<Rental> invoiceRental;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_invoice);
        mAuth = FirebaseAuth.getInstance();

        // get invoiceId
        invoiceId = CustomerHomeActivity.getInvoiceId();
        Log.d("position", String.valueOf(invoiceId));

        // search
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.CustomerHomeInvoiceActivity.this, CustomerHomeActivity.class);
                startActivity(intent);
            }
        });

        // text view
        tvStoreName = findViewById(R.id.iStoreName);
        tvStartDate = findViewById(R.id.iStartDate);
        tvDueDate = findViewById(R.id.iDueDate);
        tvTotalCost = findViewById(R.id.iTotalCost);

        // mock date
//        Timestamp rentalStartDate = java.sql.Timestamp.valueOf("2020-12-23 10:10:10.0");
//        Timestamp dueDate = java.sql.Timestamp.valueOf("2021-01-23 10:10:10.0");
        // date transform
//        Date dateStartDate = new Date(rentalStartDate.getTime());
//        String toStartDate = new SimpleDateFormat("MM/dd/yyyy").format(dateStartDate);
//        Date dateDueDate = new Date(dueDate.getTime());
//        String toDueDate = new SimpleDateFormat("MM/dd/yyyy").format(dateDueDate);
        // set text
//        tvStoreName.setText("Big Shop");
//        tvStartDate.setText(toStartDate);
//        tvDueDate.setText(toDueDate);
//        tvTotalCost.setText("$500");
        //
        invoiceRental = new ArrayList<>();
//        invoiceRental.add(new Rental("Ski", 1, 3));
//        invoiceRental.add(new Rental("Helmet", 2, 3));
//        invoiceRental.add(new Rental("Snow Pants", 3, 2));


        // recycleView
        recyclerView = findViewById(R.id.rentalRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
//        // adapter
        eAdapter = new CustomerHomeInvoiceAdapter(invoiceRental);
        recyclerView.setAdapter(eAdapter);
    }

    private void connect(String idToken) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        CustomerApiService customerApiService = retrofit.create(CustomerApiService.class);

        // api rental info
        System.out.println("invoice id in customer invoice: " + invoiceId);
        Call<CustomerRental> customerInfoCall = customerApiService.getRentalInfo(idToken, invoiceId);
        customerInfoCall.enqueue(new Callback<CustomerRental>() {

            @Override
            public void onResponse(Call<CustomerRental> call, Response<CustomerRental> response) {
                // get invoice info
//                JSONObject invoiceInfo = response.body().getCustomerRental();

                // get info
                String storeName = response.body().getStoreName();
                String rentalStartDate = response.body().getRentalStartDate().split(" ")[0];
                String dueDate = response.body().getDueDate().split(" ")[0];
                Double totalCost = response.body().getTotalCost();
                List<JSONObject> rentalList = response.body().getEquipmentList();

                // date transform
//                Date dateStartDate = new Date(rentalStartDate.getTime());
//                String toStartDate = new SimpleDateFormat("MM/dd/yyyy").format(dateStartDate);
//                Date dateDueDate = new Date(dueDate.getTime());
//                String toDueDate = new SimpleDateFormat("MM/dd/yyyy").format(dateDueDate);


                // set text
                tvStoreName.setText(storeName);
                tvStartDate.setText(rentalStartDate);
                tvDueDate.setText(dueDate);
                tvTotalCost.setText(String.valueOf(totalCost));

                // list
                int eId = 0;
                String eName = null;
                int eQuantity = 0;

                for (int i=0; i<rentalList.size(); i++ ) {
                    try {
                        eId = (int) rentalList.get(i).get("id");
                        eName = rentalList.get(i).get("name").toString();
                        eQuantity = (int) rentalList.get(i).get("quantity");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    invoiceRental.add(new Rental(eName, eId, eQuantity));
                }
                eAdapter = new CustomerHomeInvoiceAdapter(invoiceRental);
                recyclerView.setAdapter(eAdapter);
            }

            @Override
            public void onFailure(Call<CustomerRental> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO: Get current user's idToken
        mUser = mAuth.getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            System.out.println("idToken 1 : " + idToken);
                            // connect api call
//                            connect(idToken);

                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                            task.getException().printStackTrace();
                        }
                    }
                });
        System.out.println("idToken 2 : " + idToken);


    }


    public class Rental {
        private String name;
        private int id;
        private int quantity;

        public Rental(String name, int id, int quantity) {
            this.name = name;
            this.id = id;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
