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

public class OwnerHomeInvoiceActivity extends AppCompatActivity {
    static final String TAG = EquipmentListActivity.class.getSimpleName();
    static final String BASE_URL = "http://35.222.193.76/";
    static Retrofit retrofit = null;
    Button back;
    private int invoiceId;
    private String customerUserId;
    TextView tvCustomerName, tvStartDate, tvDueDate, tvTotalCost;
    OwnerHomeInvoiceAdapter eAdapter;
    private RecyclerView recyclerView;
    List<OwnerRental> invoiceRental;
    FirebaseUser mUser;

    private String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home_invoice);

        // get invoiceId
        invoiceId = OwnerHomeActivity.getInvoiceId();
        Log.d("invoiceId", String.valueOf(invoiceId));

        // get userId
        customerUserId = OwnerHomeActivity.getCustomerUserId();
        Log.d("customerUserId", String.valueOf(customerUserId));

        // back button
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerHomeInvoiceActivity.this, OwnerHomeActivity.class);
                startActivity(intent);
            }
        });

//        // connect api call
//        connect();


        // text view
        tvCustomerName = findViewById(R.id.iCustomerName);
        tvStartDate = findViewById(R.id.iStartDate);
        tvDueDate = findViewById(R.id.iDueDate);
        tvTotalCost = findViewById(R.id.iTotalCost);

        // mock date
        Timestamp rentalStartDate = java.sql.Timestamp.valueOf("2020-12-23 10:10:10.0");
        Timestamp dueDate = java.sql.Timestamp.valueOf("2021-01-23 10:10:10.0");
        // date transform
        Date dateStartDate = new Date(rentalStartDate.getTime());
        String toStartDate = new SimpleDateFormat("MM/dd/yyyy").format(dateStartDate);
        Date dateDueDate = new Date(dueDate.getTime());
        String toDueDate = new SimpleDateFormat("MM/dd/yyyy").format(dateDueDate);
        // set text
        tvCustomerName.setText("Rock");
        tvStartDate.setText(toStartDate);
        tvDueDate.setText(toDueDate);
        tvTotalCost.setText("$500");
        //
        invoiceRental = new ArrayList<>();
        invoiceRental.add(new OwnerRental("Ski", 1, 3));
        invoiceRental.add(new OwnerRental("Helmet", 2, 3));
        invoiceRental.add(new OwnerRental("Snow Pants", 3, 2));


        // recycleView
        recyclerView = findViewById(R.id.rentalRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
//        // adapter
        eAdapter = new OwnerHomeInvoiceAdapter(invoiceRental);
        recyclerView.setAdapter(eAdapter);
    }

    private void connect(String idToken) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        OwnerApiService ownerApiService = retrofit.create(OwnerApiService.class);

        // get user firstName
        Call<User> customerInfoCall = ownerApiService.getUserInfo(idToken, customerUserId);
        customerInfoCall.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // get customer info
                String firstName = response.body().getFirst_name();
//                JSONObject customerInfo = response.body().getCustomerInfo();
//
//                // get first name
//                String firstName = "";
//                try {
//                    firstName = customerInfo.get("userFirstName").toString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                // set text
                tvCustomerName.setText(firstName + "!");

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        // api rental info
        Call<CustomerRental> ownerRenaltInfoCall = ownerApiService.getRentalInfo(idToken, invoiceId);
        ownerRenaltInfoCall.enqueue(new Callback<CustomerRental>() {

            @Override
            public void onResponse(Call<CustomerRental> call, Response<CustomerRental> response) {
                // get invoice info
                JSONObject invoiceInfo = response.body().getCustomerRental();

                // get info
                Timestamp rentalStartDate = null;
                Timestamp dueDate = null;
                float totalCost = 0;
                List<JSONObject> rentalList = new ArrayList<JSONObject>();

                try {
                    rentalStartDate = (Timestamp) invoiceInfo.get("rentalStartDate");
                    dueDate = (Timestamp) invoiceInfo.get("dueDate");
                    totalCost = (float) invoiceInfo.get("totalCost");
                    rentalList = (List<JSONObject>) invoiceInfo.get("equipment");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // date transform
                Date dateStartDate = new Date(rentalStartDate.getTime());
                String toStartDate = new SimpleDateFormat("MM/dd/yyyy").format(dateStartDate);
                Date dateDueDate = new Date(dueDate.getTime());
                String toDueDate = new SimpleDateFormat("MM/dd/yyyy").format(dateDueDate);


                // set text
                tvStartDate.setText(toStartDate);
                tvDueDate.setText(toDueDate);
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
                    invoiceRental.add(new OwnerRental(eName, eId, eQuantity));
                }
                eAdapter = new OwnerHomeInvoiceAdapter(invoiceRental);
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
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            connect(idToken);
                        } else {
                            // Handle error -> task.getException();
                            task.getException().printStackTrace();
                        }
                    }
                });
        System.out.println("idToken: " + idToken);
    }



    public class OwnerRental {
        private String name;
        private int id;
        private int quantity;

        public OwnerRental(String name, int id, int quantity) {
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
