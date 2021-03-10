package edu.rentals.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class OwnerHomeActivity extends AppCompatActivity implements OwnerHomeAdapter.InvoiceListClickListener {
    Button logOut, manageStore, editInfo;
    TextView tvFirstName;
    String userId, firstName;
    private static List<OwnerInvoice> ownerInvoiceList;
    private static List<OwnerInvoice> tmpOwnerInvoiceList;
    private RecyclerView recyclerView;
    private OwnerHomeAdapter eAdapter;
    private long storeId;

    static final String TAG = OwnerHomeActivity.class.getSimpleName();
    static final String BASE_URL = "http://35.222.193.76/";
    static Retrofit retrofit = null;

    private List<String> customerIdList = new ArrayList<>();
    private HashMap<String, String> customerIdNameMap = new HashMap<>();
    private List<Long> invoiceIdList = new ArrayList<>();
    private HashMap<Long, String[]> invoiceIdDateMap = new HashMap<>();
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private static int positionChosen;

    private String idToken;

    public static Long getInvoiceId() {
        return ownerInvoiceList.get(positionChosen).getInvoiceId();
    }

    public static String getCustomerUserId() {
        return ownerInvoiceList.get(positionChosen).getUserId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);

        mAuth = FirebaseAuth.getInstance();
        // get storeId

        // search button
        logOut = findViewById(R.id.s_logout);
        logOut.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(OwnerHomeActivity.this, MainActivity.class));
        });

        // store button
        manageStore = findViewById(R.id.manageStore);
        manageStore.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerHomeActivity.this, OwnerStoreActivity.class);
            startActivity(intent);
        });


        // edit info button
        editInfo = findViewById(R.id.editOwner);
        editInfo.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerHomeActivity.this, OwnerEditActivity.class);
            startActivity(intent);
        });

        // mock owner first name
        tvFirstName = findViewById(R.id.userFirstName);
//        firstName = "User";
//        tvFirstName.setText(firstName + "!");

        // mock invoice list
        ownerInvoiceList = new ArrayList<>();
//        ownerInvoiceList.add(new OwnerInvoice(1, "1", 500, "09/23/2020", "Debby", "10/24/2020", "10/25/2020"));
//        ownerInvoiceList.add(new OwnerInvoice(2, "3", 250, "11/23/2020", "Ken", "11/24/2020", "12/25/2020"));
//        ownerInvoiceList.add(new OwnerInvoice(3, "5", 300, "12/20/2020", "John", "12/29/2020", "01/25/2021"));

//        ownerInvoiceList.add(new OwnerInvoice(1, "1", 500, java.sql.Timestamp.valueOf("2020-09-23 10:10:10.0")));
//        ownerInvoiceList.add(new OwnerInvoice(2, "2", 250, java.sql.Timestamp.valueOf("2020-10-23 10:10:10.0")));
//        ownerInvoiceList.add(new OwnerInvoice(3, "3", 300, java.sql.Timestamp.valueOf("2020-11-23 10:10:10.0")));


        // recycleView
        recyclerView = findViewById(R.id.recordRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));

        // adapter
        eAdapter = new OwnerHomeAdapter(ownerInvoiceList, this);
        recyclerView.setAdapter(eAdapter);
    }


    private void getStoreIdCall(String idToken) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        OwnerApiService ownerApiService = retrofit.create(OwnerApiService.class);

        // api call for storeId
        Call<Store> storeIdCall = ownerApiService.getStoreId(idToken);
        storeIdCall.enqueue(new Callback<Store>() {

            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                // get store info
                storeId = response.body().getStoreId();
                System.out.println("storeId in getStoreId in owner home: " + storeId);
                connect(idToken, storeId);
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }


    // this is to retrieve the Owner's name to show on the home page
    // TODO: get rid of this. Just use the FirebaseAuth to get displayName
    private void connect(String idToken, long storeId) {
        System.out.println("owner home api call............");
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        OwnerApiService ownerApiService = retrofit.create(OwnerApiService.class);

        // api call user info
//        Call<Customer> customerInfoCall = ownerApiService.getUserInfo(idToken, userId);
//        customerInfoCall.enqueue(new Callback<Customer>() {
//
//            @Override
//            public void onResponse(Call<Customer> call, Response<Customer> response) {
//                // get customer info
//                JSONObject customerInfo = response.body().getCustomerInfo();
//
//                // get first name
//                try {
//                    firstName = customerInfo.get("userFirstName").toString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                // set text
//                tvFirstName.setText(firstName + "!");
//
//            }
//
//            @Override
//            public void onFailure(Call<Customer> call, Throwable t) {
//                Log.e(TAG, t.toString());
//            }
//        });


        // api call for invoice list
        tmpOwnerInvoiceList = new ArrayList<>();


        Call<InvoiceList> invoiceListCall = ownerApiService.getInvoiceList(idToken, storeId);
        invoiceListCall.enqueue(new Callback<InvoiceList>() {

            @Override
            public void onResponse(Call<InvoiceList> call, Response<InvoiceList> response) {
                // get invoice list
                List<LinkedTreeMap> invoiceList = response.body().getInvoiceList();

                // set invoice list
                for (int i = 0; i < invoiceList.size(); i++) {
                    System.out.println("id in invoice list: " + invoiceList.get(i).get("id"));
                    long invoiceId = (long) Math.round((Double) invoiceList.get(i).get("id"));
                    invoiceIdList.add(invoiceId);
                    System.out.println("userId in invoice list: " + invoiceList.get(i).get("userId").toString());
                    String userId = invoiceList.get(i).get("userId").toString();
                    customerIdList.add(userId);
                    Double totalCost = (Double) invoiceList.get(i).get("totalCost");
                    String transactionDate = invoiceList.get(i).get("transactionDate").toString().split(" ")[0];
                    // date transform
//                    Date dateTransactionDate = new Date(transactionDate.getTime());
//                    String toTransactionDate = new SimpleDateFormat("MM/dd/yyyy").format(dateTransactionDate);

                    // add to tmp OwnerInvoiceList
                    tmpOwnerInvoiceList.add(new OwnerInvoice(invoiceId, userId, totalCost, transactionDate, "", "", ""));
                }

                // loop through tmpOwnerInvoiceList
                for (int i=0; i < tmpOwnerInvoiceList.size(); i++) {
                    // get invoiceId and tmpUserId
                    System.out.println("tmpInvoiceId : " + tmpOwnerInvoiceList.get(i).getInvoiceId());
                    System.out.println("tmpUserId : " + tmpOwnerInvoiceList.get(i).getUserId());
                    Long tmpInvoiceId = tmpOwnerInvoiceList.get(i).getInvoiceId();
                    String tmpUserId = tmpOwnerInvoiceList.get(i).getUserId();
                    Double tmpTotalCost = tmpOwnerInvoiceList.get(i).getTotalCost();
                    String tmpTransactionDate = tmpOwnerInvoiceList.get(i).getTransactionDate();

                    // use customerIdList to put {userId, userFirstName} pair to customerIdNameMap hashmap
                    Call<User> userInfoCall = ownerApiService.getUserInfo(idToken, tmpUserId);
                    userInfoCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            // get user
//                    JSONObject user = response.body().getCustomerInfo();
                            System.out.println("userFirstName check....: " + response.body().getFirst_name());
                            String userFirstName = response.body().getFirst_name();
                            //                        customerIdNameMap.put(tmpUserId, user.get("userFirstName").toString());
                            customerIdNameMap.put(tmpUserId, userFirstName);
                            ownerInvoiceList.add(new OwnerInvoice(tmpInvoiceId,
                                            tmpUserId,
                                            tmpTotalCost,
                                            tmpTransactionDate,
                                            customerIdNameMap.get(tmpUserId),
                                            "",
                                            ""));

//                            // use invoiceIdList to put {invoiceId, [rentalStartDate, dueDate]} pair to invoiceIdRentalDate HashMap
//                            Call<CustomerRental> rentalInfoCall =  ownerApiService.getRentalInfo(idToken, tmpInvoiceId);
//                            rentalInfoCall.enqueue(new Callback<CustomerRental>() {
//
//                                @Override
//                                public void onResponse(Call<CustomerRental> call, Response<CustomerRental> response) {
//                                    // get rentalInfo
//                                    JSONObject invoiceInfo = response.body().getCustomerRental();
//                                    // get info
//                                    Timestamp rentalStartDate = null;
//                                    Timestamp dueDate = null;
//
//                                    try {
//                                        rentalStartDate = (Timestamp) invoiceInfo.get("rentalStartDate");
//                                        dueDate = (Timestamp) invoiceInfo.get("dueDate");
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    // date transform
//                                    Date dateStartDate = new Date(rentalStartDate.getTime());
//                                    String toStartDate = new SimpleDateFormat("MM/dd/yyyy").format(dateStartDate);
//                                    Date dateDueDate = new Date(dueDate.getTime());
//                                    String toDueDate = new SimpleDateFormat("MM/dd/yyyy").format(dateDueDate);
//
//                                    // put to hash map
//                                    invoiceIdDateMap.put(tmpInvoiceId, new String[]{toStartDate, toDueDate});
//
//                                    // append all data to ownerInvoiceList
//
//                                    System.out.println("check rentalStartDate.....: " + invoiceIdDateMap.get(tmpInvoiceId)[0]);
//                                    ownerInvoiceList.add(new OwnerInvoice(tmpInvoiceId,
//                                            tmpUserId,
//                                            tmpOwnerInvoiceList.get(i).getTotalCost(),
//                                            tmpOwnerInvoiceList.get(i).getTransactionDate(),
//                                            customerIdNameMap.get(tmpUserId),
//                                            invoiceIdDateMap.get(tmpInvoiceId)[0],
//                                            invoiceIdDateMap.get(tmpInvoiceId)[1]));
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<CustomerRental> call, Throwable t) {
//                                    Log.e(TAG, t.toString());
//                                }
//                            });
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }

                //ownerInvoiceList
                eAdapter = new OwnerHomeAdapter(tmpOwnerInvoiceList, OwnerHomeActivity.this::onInvoiceClick);
                recyclerView.setAdapter(eAdapter);

//                // loop through tmpOwnerInvoiceList
//                for (int i=0; i < tmpOwnerInvoiceList.size(); i++) {
//                    // get invoiceId and tmpUserId
//                    System.out.println("tmpInvoiceId : " + tmpOwnerInvoiceList.get(i).getInvoiceId());
//                    System.out.println("tmpUserId : " + tmpOwnerInvoiceList.get(i).getUserId());
//                    Long tmpInvoiceId = tmpOwnerInvoiceList.get(i).getInvoiceId();
//                    String tmpUserId = tmpOwnerInvoiceList.get(i).getUserId();
//
//                    // use customerIdList to put {userId, userFirstName} pair to customerIdNameMap hashmap
//                    Call<User> userInfoCall = ownerApiService.getUserInfo(idToken, tmpUserId);
//                    userInfoCall.enqueue(new Callback<User>() {
//                        @Override
//                        public void onResponse(Call<User> call, Response<User> response) {
//                            // get user
////                    JSONObject user = response.body().getCustomerInfo();
//                            System.out.println("userFirstName check....: " + response.body().getFirst_name());
//                            String userFirstName = response.body().getFirst_name();
//                            //                        customerIdNameMap.put(tmpUserId, user.get("userFirstName").toString());
//                            customerIdNameMap.put(tmpUserId, userFirstName);
//
//                            // use invoiceIdList to put {invoiceId, [rentalStartDate, dueDate]} pair to invoiceIdRentalDate HashMap
//                            Call<CustomerRental> rentalInfoCall =  ownerApiService.getRentalInfo(idToken, tmpInvoiceId);
//                            rentalInfoCall.enqueue(new Callback<CustomerRental>() {
//
//                                @Override
//                                public void onResponse(Call<CustomerRental> call, Response<CustomerRental> response) {
//                                    // get rentalInfo
//                                    JSONObject invoiceInfo = response.body().getCustomerRental();
//                                    // get info
//                                    Timestamp rentalStartDate = null;
//                                    Timestamp dueDate = null;
//
//                                    try {
//                                        rentalStartDate = (Timestamp) invoiceInfo.get("rentalStartDate");
//                                        dueDate = (Timestamp) invoiceInfo.get("dueDate");
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    // date transform
//                                    Date dateStartDate = new Date(rentalStartDate.getTime());
//                                    String toStartDate = new SimpleDateFormat("MM/dd/yyyy").format(dateStartDate);
//                                    Date dateDueDate = new Date(dueDate.getTime());
//                                    String toDueDate = new SimpleDateFormat("MM/dd/yyyy").format(dateDueDate);
//
//                                    // put to hash map
//                                    invoiceIdDateMap.put(tmpInvoiceId, new String[]{toStartDate, toDueDate});
//
//                                    // append all data to ownerInvoiceList
//
//                                    System.out.println("check rentalStartDate.....: " + invoiceIdDateMap.get(tmpInvoiceId)[0]);
//                                    ownerInvoiceList.add(new OwnerInvoice(tmpInvoiceId,
//                                            tmpUserId,
//                                            tmpOwnerInvoiceList.get(i).getTotalCost(),
//                                            tmpOwnerInvoiceList.get(i).getTransactionDate(),
//                                            customerIdNameMap.get(tmpUserId),
//                                            invoiceIdDateMap.get(tmpInvoiceId)[0],
//                                            invoiceIdDateMap.get(tmpInvoiceId)[1]));
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<CustomerRental> call, Throwable t) {
//                                    Log.e(TAG, t.toString());
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onFailure(Call<User> call, Throwable t) {
//                            Log.e(TAG, t.toString());
//                        }
//                    });
//
//
//
//                }



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
        private Long invoiceId;
        private String userId;
        private Double totalCost;
        private String transactionDate;
        private String userFirstName;
        private String rentalStartDate;
        private String dueDate;

        public OwnerInvoice(Long invoiceId, String userId, Double totalCost, String transactionDate, String userFirstName, String rentalStartDate, String dueDate) {
            this.invoiceId = invoiceId;
            this.userId = userId;
            this.totalCost = totalCost;
            this.transactionDate = transactionDate;
            this.userFirstName = userFirstName;
            this.rentalStartDate = rentalStartDate;
            this.dueDate = dueDate;
        }

        public Long getInvoiceId() {
            return invoiceId;
        }

        public String getUserId() {
            return userId;
        }

        public Double getTotalCost() {
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

    @Override
    public void onStart() {
        super.onStart();

        // TODO: Get current user's idToken
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        // get userId
        userId = mUser.getUid();
        Log.d("userId in owner home: ", userId);

        String name = mUser.getDisplayName();
        TextView tvFirstName = findViewById(R.id.userFirstName);
        tvFirstName.setText(name);
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();
                            System.out.println("idToken in owner home: " + idToken);
                            getStoreIdCall(idToken);

                        } else {
                            // Handle error -> task.getException();
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

}
