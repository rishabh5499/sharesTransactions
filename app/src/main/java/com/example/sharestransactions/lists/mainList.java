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
import android.widget.SearchView;
import android.widget.TextView;

import com.example.sharestransactions.MainActivity;
import com.example.sharestransactions.R;
import com.example.sharestransactions.miscFuns;
import com.example.sharestransactions.transactions.stockMarketTrxn;
import com.example.sharestransactions.transactions.nsc_bonds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class mainList extends AppCompatActivity {

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
    String srcStr;
    private static final int REQUEST_CODE = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent getData = getIntent();
        srcStr = getData.getStringExtra("src");

        list = findViewById(R.id.list);
        registerForContextMenu(list);
        add = findViewById(R.id.button4);
        textView = findViewById(R.id.textView3);
        total = findViewById(R.id.editTextPersonName10);
        productList = new ArrayList<>();
        keysList = new ArrayList<>();
        pricesList = new ArrayList<>();
        pricesList.clear();

        scrollView = findViewById(R.id.ScrollViewList);
        linearLayout = findViewById(R.id.LinearLayoutList);

        loading = findViewById(R.id.progressBar3);

        broadcastReceiver = new miscFuns();

        mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = FirebaseAuth.getInstance().getUid();
        try {
            db = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child(uid).child(srcStr);
        } catch (Exception e){
            miscFuns.showAlert(mainList.this, "No data in DB");
        }
//        if (srcStr.equals("Share_Market")){
//            textView.setText("Share Market List");
//        } else if (srcStr.equals("Bank_PO")){
//            textView.setText("Bank and PO List");
//        }
        textView.setText(srcStr+" List");

        scrollView.setVisibility(View.GONE);
//        add.setVisibility(View.GONE);

        retVal = execute(db);
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

    private int execute(DatabaseReference db) {
        try {
            db.orderByChild("date").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String test = ds.getKey().toString().trim();
                            keysList.add(test);
                            String nameStr = ds.child("name").getValue().toString();
                            String priceStr = ds.child("totalInvestment").getValue().toString();
                            String dateStr = ds.child("date").getValue().toString();
                            String quantityStr = ds.child("quantity").getValue().toString();
                            pricesList.add(Float.valueOf(priceStr));
                            productList.add(nameStr + "\nAmt: " + String.format("%.2f", Float.parseFloat(priceStr)) + " Dt: " + dateStr + " Qty: " + quantityStr);
                        }
                        for (int i = 0; i < pricesList.size(); i++) {
                            sum += pricesList.get(i);
                        }
                        total.setText(sum.toString());

                        Collections.reverse(productList);
                        Collections.reverse(keysList);
                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, productList);
                        list.setAdapter(adapter);
                    } else {
                        miscFuns.showAlert(mainList.this, "No Records");
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
        switch (srcStr){
            case "Purchases":
            case "Sale":
                add = new Intent(mainList.this, stockMarketTrxn.class);
                add.putExtra("id", selectedItem);
                add.putExtra("src", "list");
                add.putExtra("trxnType", srcStr);
                startActivityForResult(add, REQUEST_CODE);
                return true;
            default:
                return false;
        }
    }

    private boolean addIntent(String srcStr) {
        Intent add;
        switch (srcStr){
            case "Long Term":
            case "Short Term":
            case "Mutual Funds":
            case "Futures Options":
                add = new Intent(mainList.this, stockMarketTrxn.class);
                add.putExtra("src", "add_button");
                add.putExtra("trxnType", srcStr);
                startActivityForResult(add, REQUEST_CODE);
                return true;
            case "NPS":
            case "Bonds":
                add = new Intent(mainList.this, nsc_bonds.class);
                add.putExtra("src", "add_button");
                add.putExtra("trxnType", srcStr);
                startActivityForResult(add, REQUEST_CODE);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> filteredList = new ArrayList<>();
                ArrayList<String> filteredKeyList = new ArrayList<>();

                for (int j = 0; j < productList.size(); j++) {
                    if (productList.get(j).toLowerCase().contains(newText.toLowerCase())) {
                        String volUid = keysList.get(j);
                        filteredList.add(productList.get(j));
                        filteredKeyList.add(volUid);
                    }
                }

                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, filteredList);
                list.setAdapter(adapter);


                itemSelectListener(filteredKeyList);

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void itemSelectListener(ArrayList<String> listOfKeys) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                forwardIntent(listOfKeys.get(i), srcStr);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //for back button on left side
        Intent myIntent = new Intent(mainList.this, MainActivity.class);
        startActivity(myIntent);

        //for buttons on right side
        switch (item.getItemId()){
            case R.id.refresh:
                finish();
                startActivity(getIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadStop(int retValue){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(retValue ==1) {
//                    loading.setVisibility(View.GONE);
                    add.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    add.setVisibility(View.VISIBLE);
                } else {
                    miscFuns.showAlert(mainList.this, "Error");
                    registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                    Intent back = new Intent(mainList.this, MainActivity.class);
                    startActivity(back);
                }
            }
        },2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
        startActivity(getIntent());
//        loading.setVisibility(View.VISIBLE);
        if (requestCode == REQUEST_CODE == (resultCode == RESULT_OK && data != null)) {
//                String id = data.getStringExtra("deletedId");
//                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
            // Add logic to remove item from your list
//                adapter.remove(id);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.GONE);
                }
            }, 1000);
            // Notify adapter of new changes
//                adapter.notifyDataSetChanged();
        }
    }

    protected void unregisterNetwork(){
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }
}