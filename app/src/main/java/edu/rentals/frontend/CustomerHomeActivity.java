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

import com.google.gson.internal.LinkedTreeMap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerHomeActivity extends AppCompatActivity implements CustomerHomeAdapter.InvoiceListClickListener {
    Button search, editCustomer, logoutBtn;
    static final String TAG = CustomerHomeActivity.class.getSimpleName();
    static final String BASE_URL = "http://35.222.193.76:80/";
    static Retrofit retrofit = null;
    private String userId;
    private String firstName;
    private static List<Invoice> userInvoiceList;
    private RecyclerView recyclerView;
    TextView tvFirstName;
    private CustomerHomeAdapter eAdapter;
    private static int positionChosen;

    private FirebaseAuth mAuth;
    private String idToken;
    FirebaseUser mUser;

    public static int getInvoiceId() {
        return userInvoiceList.get(positionChosen).getInvoiceId();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);


        // search
        search = findViewById(R.id.s_logout);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.CustomerHomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // edit info
        editCustomer = findViewById(R.id.editCustomer);
        editCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.CustomerHomeActivity.this, CustomerEditActivity.class);
                startActivity(intent);
            }
        });

        // text view user first name
        tvFirstName = findViewById(R.id.userFirstName);
//        firstName = "yoo";
        tvFirstName.setText(firstName + "!");

        // invoice list
        userInvoiceList = new ArrayList<>();
        userInvoiceList.add(new Invoice(1, "Bike Shop", 500, java.sql.Timestamp.valueOf("2017-09-23 10:10:10.0")));
//        userInvoiceList.add(new Invoice(2, "Ski Shop", 100, java.sql.Timestamp.valueOf("2016-09-23 10:10:10.0")));
//        userInvoiceList.add(new Invoice(3, "Surf Shop", 150, java.sql.Timestamp.valueOf("2015-09-23 10:10:10.0")));
//        userInvoiceList.add(new Invoice(4, "Bear Shop", 1000, java.sql.Timestamp.valueOf("2014-09-23 10:10:10.0")));
//        userInvoiceList.add(new Invoice(5, "Fish Shop", 1050, java.sql.Timestamp.valueOf("2013-09-25 10:10:10.0")));

        // recycleView
        recyclerView = findViewById(R.id.invoiceRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));



        // adapter
        eAdapter = new CustomerHomeAdapter(userInvoiceList, this);
        recyclerView.setAdapter(eAdapter);

        logoutBtn = findViewById(R.id.c_logout);
        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(CustomerHomeActivity.this, MainActivity.class));
        });

    }

    private void connect(String idToken) {
//        System.out.println("idToken inside connect() :" + idToken);
//        System.out.println("userId inside connect() : " + userId);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        CustomerApiService customerApiService = retrofit.create(CustomerApiService.class);

        // api call user info
        Call<GetUserById> customerInfoCall = customerApiService.getUserInfo(idToken, userId);
        customerInfoCall.enqueue(new Callback<GetUserById>() {

            @Override
            public void onResponse(Call<GetUserById> call, Response<GetUserById> response) {
                // get customer info
//                JSONObject customerInfo = response.body().getCustomerInfo();

                // get first name
//                try {
//                    firstName = customerInfo.get("userFirstName").toString();
                    firstName = response.body().getUserFirstName();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                // set text
                tvFirstName.setText(firstName + "!");

            }

            @Override
            public void onFailure(Call<GetUserById> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });


        // api call for invoice list
        Call<InvoiceList> invoiceListCall = customerApiService.getInvoiceList(idToken);
        invoiceListCall.enqueue(new Callback<InvoiceList>() {

            @Override
            public void onResponse(Call<InvoiceList> call, Response<InvoiceList> response) {
                // get invoice list
                List<LinkedTreeMap> invoiceList = response.body().getInvoiceList();

                // set invoice list
                for (int i = 0; i < invoiceList.size(); i++) {
                    int invoiceId = (int) invoiceList.get(i).get("id");
                    String storeName = invoiceList.get(i).get("storeName").toString();
                    float totalCost = (float) invoiceList.get(i).get("totalCost");
                    Timestamp transactionDate = (Timestamp) invoiceList.get(i).get("transactionDate");
                    userInvoiceList.add(new Invoice(invoiceId, storeName, totalCost, transactionDate));
                }
                eAdapter = new CustomerHomeAdapter(userInvoiceList, CustomerHomeActivity.this::onInvoiceClick);
                recyclerView.setAdapter(eAdapter);

            }

            @Override
            public void onFailure(Call<InvoiceList> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });


    }

    @Override
    public void onInvoiceClick(int position) {
        positionChosen = position;
        Intent intent = new Intent(edu.rentals.frontend.CustomerHomeActivity.this, CustomerHomeInvoiceActivity.class);
        startActivity(intent);
    }

    public static class Invoice {
        private int invoiceId;
        private String storeName;
        private float totalCost;
        private Timestamp transactionDate;

        public Invoice(int invoiceId, String storeName, float totalCost, Timestamp transactionDate) {
            this.invoiceId = invoiceId;
            this.storeName = storeName;
            this.totalCost = totalCost;
            this.transactionDate = transactionDate;
        }

        public int getInvoiceId() {
            return invoiceId;
        }

        public String getStoreName() {
            return storeName;
        }

        public float getTotalCost() {
            return totalCost;
        }

        public Timestamp getTransactionDate() {
            return transactionDate;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        String name = mAuth.getCurrentUser().getDisplayName();
//        System.out.println("@@@@@@@@@" + name);
        tvFirstName.setText(name);

        // TODO: Get current user's idToken
        mUser = mAuth.getCurrentUser();
        userId = mUser.getUid();
        Log.d("userId", userId);
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            System.out.println("idToken 1 : " + idToken);
                            // connect api call
                            connect(idToken);

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

}
