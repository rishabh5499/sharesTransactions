package com.example.sharestransactions.lists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sharestransactions.MainActivity;
import com.example.sharestransactions.R;
import com.example.sharestransactions.miscFuns;
import com.example.sharestransactions.transactions.nsc_bonds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class shortTermList extends AppCompatActivity {

    ListView list;
    Button add;
    TextView textView;
    EditText total;
    ProgressBar loading;
    ArrayAdapter<String> adapter;
    ArrayList<String> productList, keysList;
    ArrayList<Float> pricesList;
    ScrollView scrollView;
    LinearLayout linearLayout;
    BroadcastReceiver broadcastReceiver;
    DatabaseReference db;
    FirebaseAuth mFirebaseAuth;
    Float sum = 0F;
    int retVal;
    private static final int REQUEST_CODE = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_term_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent getData = getIntent();
        String srcStr = getData.getStringExtra("src");

        list = findViewById(R.id.shortTermList);
        registerForContextMenu(list);
        add = findViewById(R.id.button5);
        textView = findViewById(R.id.textView21);
        total = findViewById(R.id.editTextTextPersonName15);
        productList = new ArrayList<>();
        keysList = new ArrayList<>();
        pricesList = new ArrayList<>();
        pricesList.clear();

        scrollView = findViewById(R.id.shortTermScroll);
        linearLayout = findViewById(R.id.LinearLayoutShortTermList);

        loading = findViewById(R.id.progressBar4);

        broadcastReceiver = new miscFuns();

        mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = FirebaseAuth.getInstance().getUid();
        try {
            db = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child(uid).child(srcStr);
        } catch (Exception e){
            miscFuns.showAlert(shortTermList.this, "No data in DB");
        }

        textView.setText(srcStr+" List");

        scrollView.setVisibility(View.GONE);
        retVal = execute(db, srcStr);
        loadStop(retVal);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                forwardIntent(keysList.get(i), srcStr);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIntent(srcStr);
            }
        });
    }

    private int execute(DatabaseReference db, String srcStr) {
        try {
            db.orderByChild("date").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String test = ds.getKey().toString().trim();
                            keysList.add(test);
                            String nameStr = ds.child("name").getValue().toString();
                            String initialPriceStr = ds.child("buySellPrice").getValue().toString();
                            String dateStr = ds.child("date").getValue().toString();
                            String comissionStr = ds.child("commission").getValue().toString();
                            String quantityStr = ds.child("quantity").getValue().toString();
                            String typeOfPurchaseStr = ds.child("typeOfPurchase").getValue().toString();
                            String squareOffPriceStr = ds.child("squareOffPrice").getValue().toString();
                            String resultStr;

                            Float difference = (Float.valueOf(squareOffPriceStr)*Float.valueOf(quantityStr)) - (Float.valueOf(initialPriceStr)*Float.valueOf(quantityStr));
                            Float result = difference - Float.valueOf(comissionStr);
                            resultStr = String.format("%.2f", Math.abs(result));
                            pricesList.add(difference - Float.valueOf(comissionStr));
                            if (result > 0.0f) {
                                productList.add(nameStr + " - " + typeOfPurchaseStr + "\nProfit: " + resultStr + " Dt: " + dateStr + " Qty: " + quantityStr);
                            } else {
                                productList.add(nameStr + " - " + typeOfPurchaseStr + "\nLoss: " + resultStr + " Dt: " + dateStr + " Qty: " + quantityStr);
                            }
                        }
                        for (int i = 0; i < pricesList.size(); i++) {
                            sum += pricesList.get(i);
                        }
                        Float totalGain = sum;
                        total.setText(String.format("%.2f", totalGain));

                        Collections.reverse(productList);
                        Collections.reverse(keysList);
                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, productList);
                        list.setAdapter(adapter);
                    } else {
                        miscFuns.showAlert(shortTermList.this, "No Records");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } catch (Exception e){

        }
        return 1;
    }

    private boolean forwardIntent(String selectedItem, @NonNull String srcStr) {
        Intent add;
        add = new Intent(shortTermList.this, nsc_bonds.class);
        add.putExtra("id", selectedItem);
        add.putExtra("src", "list");
        add.putExtra("trxnType", srcStr);
        startActivityForResult(add, REQUEST_CODE);
        return true;
    }

    private boolean addIntent(String srcStr) {
        Intent add;
        add = new Intent(shortTermList.this, nsc_bonds.class);
        add.putExtra("src", "add_button");
        add.putExtra("trxnType", srcStr);
        startActivityForResult(add, REQUEST_CODE);
        return true;
    }

    public void loadStop(int retValue){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(retValue ==1) {
                    loading.setVisibility(View.GONE);
                    add.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    add.setVisibility(View.VISIBLE);
                } else {
                    miscFuns.showAlert(shortTermList.this, "Error");
                    registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                    Intent back = new Intent(shortTermList.this, MainActivity.class);
                    startActivity(back);
                }
            }
        },2000);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(shortTermList.this, MainActivity.class);
        startActivity(myIntent);
        switch (item.getItemId()){
            case R.id.refresh:
                finish();
                startActivity(getIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE == (resultCode == RESULT_OK && data != null)) {
            finish();
            startActivity(getIntent());
        }
    }
}