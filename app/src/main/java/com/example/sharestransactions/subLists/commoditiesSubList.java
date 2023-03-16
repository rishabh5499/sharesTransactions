package com.example.sharestransactions.subLists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sharestransactions.R;
import com.example.sharestransactions.lists.mainList;

public class commoditiesSubList extends AppCompatActivity implements View.OnClickListener {

    Button gold, silver, other;
    String trxnType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodities);

        gold = findViewById(R.id.btn5_1);
        silver = findViewById(R.id.btn5_2);
        other = findViewById(R.id.btn5_3);

        gold.setOnClickListener(this);
        silver.setOnClickListener(this);
        other.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn5_1:
                trxnType = "Gold";
                forwardIntents();
                return;
            case R.id.btn5_2:
                trxnType = "Silver";
                forwardIntents();
                return;
            case R.id.btn5_3:
                trxnType = "Other";
                forwardIntents();
                return;

        }
    }

    private boolean forwardIntents() {
        Intent intent;
        switch (trxnType){
            case "Gold":
            case "Silver":
            case "Other":
                intent = new Intent(commoditiesSubList.this, mainList.class);
                intent.putExtra("src", trxnType);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}