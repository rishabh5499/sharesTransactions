package com.example.sharestransactions;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sharestransactions.lists.longTermList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class filters extends AppCompatActivity {

    ListView list;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        list = findViewById(R.id.filtersList);
        registerForContextMenu(list);
        total = findViewById(R.id.editTextTextPersonName16);
        productList = new ArrayList<>();
        keysList = new ArrayList<>();
        pricesList = new ArrayList<>();
        pricesList.clear();

        scrollView = findViewById(R.id.ScrollViewList);
        linearLayout = findViewById(R.id.LinearLayoutList);

        loading = findViewById(R.id.progressBar);

        broadcastReceiver = new miscFuns();

        try {
//            db = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child(uid).child(srcStr);
            db = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Purchases");
            db = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Sale");
        } catch (Exception e){
            miscFuns.showAlert(filters.this, "No data in DB");
        }
    }
}