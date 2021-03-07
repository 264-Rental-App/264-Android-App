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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;

public class OwnerHomeActivity extends AppCompatActivity implements OwnerHomeAdapter.InvoiceListClickListener {
    Button search, manageStore, editInfo;
    TextView tvFirstName;
    String userId, firstName;
    private static List<OwnerInvoice> ownerInvoiceList;
    private static List<OwnerInvoice> tmpOwnerInvoiceList;
    private RecyclerView recyclerView;
    private OwnerHomeAdapter eAdapter;
    private long storeId;

    static final String TAG = OwnerHomeActivity.class.getSimpleName();
    static final String BASE_URL = "http://localhost:8080/";
    static Retrofit retrofit = null;

    private List<String> customerIdList = new ArrayList<>();
    private HashMap<String, String> customerIdNameMap = new HashMap<>();
    private List<Integer> invoiceIdList = new ArrayList<>();
    private HashMap<Integer, String[]> invoiceIdDateMap = new HashMap<>();

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
        ownerInvoiceList.add(new OwnerInvoice(1, "1", 500, "09/23/2020", "Debby", "10/24/2020", "10/25/2020"));
        ownerInvoiceList.add(new OwnerInvoice(2, "3", 250, "11/23/2020", "Ken", "11/24/2020", "12/25/2020"));
        ownerInvoiceList.add(new OwnerInvoice(3, "5", 300, "12/20/2020", "John", "12/29/2020", "01/25/2021"));

//        ownerInvoiceList.add(new OwnerInvoice(1, "1", 500, java.sql.Timestamp.valueOf("2020-09-23 10:10:10.0")));
//        ownerInvoiceList.add(new OwnerInvoice(2, "2", 250, java.sql.Timestamp.valueOf("2020-10-23 10:10:10.0")));
//        ownerInvoiceList.add(new OwnerInvoice(3, "3", 300, java.sql.Timestamp.valueOf("2020-11-23 10:10:10.0")));


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
                tmpOwnerInvoiceList = new ArrayList<>();
                for (int i = 0; i < invoiceList.size(); i++) {
                    int invoiceId = (int) invoiceList.get(i).get("id");
                    invoiceIdList.add(invoiceId);
                    String userId = invoiceList.get(i).get("userId").toString();
                    customerIdList.add(userId);
                    float totalCost = (float) invoiceList.get(i).get("totalCost");
                    Timestamp transactionDate = (Timestamp) invoiceList.get(i).get("transactionDate");
                    // date transform
                    Date dateTransactionDate = new Date(transactionDate.getTime());
                    String toTransactionDate = new SimpleDateFormat("MM/dd/yyyy").format(dateTransactionDate);

                    // add to tmp OwnerInvoiceList
                    tmpOwnerInvoiceList.add(new OwnerInvoice(invoiceId, userId, totalCost, toTransactionDate, "", "", ""));
                }


            }

            @Override
            public void onFailure(Call<InvoiceList> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        // use customerIdList to put {userId, userFirstName} pair to customerIdNameMap hashmap
        for (String customerUserId: customerIdList) {
            Call<Customer> userInfoCall = ownerApiService.getUserInfo(customerUserId);
            userInfoCall.enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    // get user
                    JSONObject user = response.body().getCustomerInfo();
                    try {
                        customerIdNameMap.put(customerUserId, user.get("userFirstName").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        }

        // use invoiceIdList to put {invoiceId, [rentalStartDate, dueDate]} pair to invoiceIdRentalDate HashMap
        for (int invoiceId: invoiceIdList) {
            Call<CustomerRental> rentalInfoCall =  ownerApiService.getRentalInfo(invoiceId);
            rentalInfoCall.enqueue(new Callback<CustomerRental>() {

                @Override
                public void onResponse(Call<CustomerRental> call, Response<CustomerRental> response) {
                    // get rentalInfo
                    JSONObject invoiceInfo = response.body().getCustomerRental();
                    // get info
                    Timestamp rentalStartDate = null;
                    Timestamp dueDate = null;

                    try {
                        rentalStartDate = (Timestamp) invoiceInfo.get("rentalStartDate");
                        dueDate = (Timestamp) invoiceInfo.get("dueDate");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // date transform
                    Date dateStartDate = new Date(rentalStartDate.getTime());
                    String toStartDate = new SimpleDateFormat("MM/dd/yyyy").format(dateStartDate);
                    Date dateDueDate = new Date(dueDate.getTime());
                    String toDueDate = new SimpleDateFormat("MM/dd/yyyy").format(dateDueDate);

                    // put to hash map
                    invoiceIdDateMap.put(invoiceId, new String[]{toStartDate, toDueDate});

                }

                @Override
                public void onFailure(Call<CustomerRental> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });

            // loop through tmpOwnerInvoiceList
            for (int i=0; i < tmpOwnerInvoiceList.size(); i++) {
                int tmpInvoiceId = tmpOwnerInvoiceList.get(i).getInvoiceId();
                String impUserId = tmpOwnerInvoiceList.get(i).getUserId();
                ownerInvoiceList.add(new OwnerInvoice(tmpInvoiceId,
                                                        impUserId,
                                                        tmpOwnerInvoiceList.get(i).getTotalCost(),
                                                        tmpOwnerInvoiceList.get(i).getTransactionDate(),
                                                        customerIdNameMap.get(impUserId),
                                                        invoiceIdDateMap.get(tmpInvoiceId)[0],
                                                        invoiceIdDateMap.get(tmpInvoiceId)[1]));
            }

            eAdapter = new OwnerHomeAdapter(ownerInvoiceList, OwnerHomeActivity.this::onInvoiceClick);
            recyclerView.setAdapter(eAdapter);
        }
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
        private String transactionDate;
        private String userFirstName;
        private String rentalStartDate;
        private String dueDate;

        public OwnerInvoice(int invoiceId, String userId, float totalCost, String transactionDate, String userFirstName, String rentalStartDate, String dueDate) {
            this.invoiceId = invoiceId;
            this.userId = userId;
            this.totalCost = totalCost;
            this.transactionDate = transactionDate;
            this.userFirstName = userFirstName;
            this.rentalStartDate = rentalStartDate;
            this.dueDate = dueDate;
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

        public String getTransactionDate() {
            return transactionDate;
        }

        public String getUserFirstName() {
            return userFirstName;
        }

        public String getRentalStartDate() {
            return rentalStartDate;
        }

        public String getDueDate() {
            return dueDate;
        }
    }


}
