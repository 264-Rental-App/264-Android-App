package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OwnerHomeActivity extends AppCompatActivity implements OwnerHomeAdapter.InvoiceListClickListener {
    Button search, manageStore, editInfo;
    TextView tvFirstName;
    String userId, firstName;
    private static List<OwnerInvoice> ownerInvoiceList;
    private RecyclerView recyclerView;
    private OwnerHomeAdapter eAdapter;
    private long storeId;

    static final String TAG = OwnerHomeActivity.class.getSimpleName();
    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;

    private static int positionChosen;

    public static int getInvoiceId() {
        return ownerInvoiceList.get(positionChosen).getInvoiceId();
    }

    public static String getCustomerUserId() {
        return ownerInvoiceList.get(positionChosen).getUserId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);

        // get userId
        userId = "1";
        // get storeId
        storeId = 1;

        // search button
        search = findViewById(R.id.searchPage);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerHomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // store button
        manageStore = findViewById(R.id.manageStore);
        manageStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerHomeActivity.this, OwnerStoreActivity.class);
                startActivity(intent);
            }
        });


        // edit info button
        editInfo = findViewById(R.id.editOwner);
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edu.rentals.frontend.OwnerHomeActivity.this, OwnerEditActivity.class);
                startActivity(intent);
            }
        });

        // mock owner first name
        tvFirstName = findViewById(R.id.userFirstName);
        firstName = "User";
        tvFirstName.setText(firstName + "!");

        // mock invoice list
        ownerInvoiceList = new ArrayList<>();
        ownerInvoiceList.add(new OwnerInvoice(1, "1", 500, java.sql.Timestamp.valueOf("2020-09-23 10:10:10.0")));
        ownerInvoiceList.add(new OwnerInvoice(2, "2", 250, java.sql.Timestamp.valueOf("2020-10-23 10:10:10.0")));
        ownerInvoiceList.add(new OwnerInvoice(3, "3", 300, java.sql.Timestamp.valueOf("2020-11-23 10:10:10.0")));
//        userInvoiceList.add(new Invoice(2, "Ski Shop", 100, java.sql.Timestamp.valueOf("2016-09-23 10:10:10.0")));
//        userInvoiceList.add(new Invoice(3, "Surf Shop", 150, java.sql.Timestamp.valueOf("2015-09-23 10:10:10.0")));
//        userInvoiceList.add(new Invoice(4, "Bear Shop", 1000, java.sql.Timestamp.valueOf("2014-09-23 10:10:10.0")));
//        userInvoiceList.add(new Invoice(5, "Fish Shop", 1050, java.sql.Timestamp.valueOf("2013-09-25 10:10:10.0")));

        // recycleView
        recyclerView = findViewById(R.id.recordRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));


        // connect api call
        connect();

        // adapter
        eAdapter = new OwnerHomeAdapter(ownerInvoiceList, this);
        recyclerView.setAdapter(eAdapter);
    }

    private void connect() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        OwnerApiService ownerApiService = retrofit.create(OwnerApiService.class);

        // api call user info
        Call<Customer> customerInfoCall = ownerApiService.getUserInfo(userId);
        customerInfoCall.enqueue(new Callback<Customer>() {

            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                // get customer info
                JSONObject customerInfo = response.body().getCustomerInfo();

                // get first name
                try {
                    firstName = customerInfo.get("userFirstName").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // set text
                tvFirstName.setText(firstName + "!");

            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });


        // api call for invoice list
        Call<InvoiceList> invoiceListCall = ownerApiService.getInvoiceList(String.valueOf(storeId));
        invoiceListCall.enqueue(new Callback<InvoiceList>() {

            @Override
            public void onResponse(Call<InvoiceList> call, Response<InvoiceList> response) {
                // get invoice list
                List<LinkedTreeMap> invoiceList = response.body().getInvoiceList();

                // set invoice list
                for (int i = 0; i < invoiceList.size(); i++) {
                    int invoiceId = (int) invoiceList.get(i).get("id");
                    String userId = invoiceList.get(i).get("userId").toString();
                    float totalCost = (float) invoiceList.get(i).get("totalCost");
                    Timestamp transactionDate = (Timestamp) invoiceList.get(i).get("transactionDate");
                    ownerInvoiceList.add(new OwnerInvoice(invoiceId, userId, totalCost, transactionDate));
                }
                eAdapter = new OwnerHomeAdapter(ownerInvoiceList, OwnerHomeActivity.this::onInvoiceClick);
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
        Intent intent = new Intent(edu.rentals.frontend.OwnerHomeActivity.this, OwnerHomeInvoiceActivity.class);
        startActivity(intent);
    }


    public static class OwnerInvoice {
        private int invoiceId;
        private String userId;
        private float totalCost;
        private Timestamp transactionDate;

        public OwnerInvoice(int invoiceId, String userId, float totalCost, Timestamp transactionDate) {
            this.invoiceId = invoiceId;
            this.userId = userId;
            this.totalCost = totalCost;
            this.transactionDate = transactionDate;
        }

        public int getInvoiceId() {
            return invoiceId;
        }

        public String getUserId() {
            return userId;
        }

        public float getTotalCost() {
            return totalCost;
        }

        public Timestamp getTransactionDate() {
            return transactionDate;
        }
    }


}
