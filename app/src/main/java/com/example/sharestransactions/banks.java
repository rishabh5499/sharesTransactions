package com.example.sharestransactions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class banks extends AppCompatActivity {

    DatabaseReference db;
    Button button;
    ArrayList<String> dataList;
    ArrayAdapter<String> adapter;
    ListView bankList;
    EditText bankNameInput;
    BroadcastReceiver broadcastReceiver;
    details details;
    ProgressBar loading;
    int retVal;
    int index;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banks);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        mFirebaseAuth = FirebaseAuth.getInstance();
        String uid = FirebaseAuth.getInstance().getUid();
        db = FirebaseDatabase.getInstance().getReference().child(uid).child("banks");

        dataList = new ArrayList<String>();
        bankList = findViewById(R.id.bankNames);
        bankNameInput = findViewById(R.id.editTextTextPersonName2);

        button = findViewById(R.id.button3);
        loading = findViewById(R.id.progressBar4);

        broadcastReceiver = new miscFuns();

        details = new details();

        getIndex();

        retVal = execute(db);
        loadStop(retVal);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = bankNameInput.getText().toString().trim();
//                String temp = String.valueOf(button.getText());

                details.setBankName(value);
                db.child("Bank " + count).setValue(details);
                bankNameInput.setText("");
                dataList.add(value);
                refresh();
//                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
            }
        });

        bankList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                String bank = dataList.get(i);
                bankNameInput.setText(dataList.get(i));
                button.setText("Edit");
                index = i;
            }
        });
    }

    private void getIndex() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = (int) snapshot.getChildrenCount();
                count += 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private int execute(DatabaseReference db){
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String data = ds.child("bankName").getValue().toString().trim();
                        dataList.add(data);
                    }
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, dataList);
                    bankList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    miscFuns.showAlert(banks.this, "Enter names to list");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return 1;
    }

    public void loadStop(int retValue){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(retValue == 1) {
                    loading.setVisibility(View.GONE);
                    bankList.setVisibility(View.VISIBLE);
                } else {
                    miscFuns.showAlert(banks.this, "Error");
                    registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                }
            }
        },2000);
    }

    protected void unregisterNetwork(){
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e){

        }
    }

    protected void refresh() {
        finish();
        startActivity(getIntent());
    }
}