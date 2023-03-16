package com.example.sharestransactions.subLists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sharestransactions.R;
import com.example.sharestransactions.lists.mainList;

public class insuranceSubList extends AppCompatActivity implements View.OnClickListener {

    Button generalInsurance, lifeInsurance, termInsurance, others;
    String trxnType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_sublist);

        generalInsurance = findViewById(R.id.btn4_1);
        lifeInsurance = findViewById(R.id.btn4_2);
        termInsurance = findViewById(R.id.btn4_3);
        others = findViewById(R.id.btn4_4);

        generalInsurance.setOnClickListener(this);
        lifeInsurance.setOnClickListener(this);
        termInsurance.setOnClickListener(this);
        others.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn4_1:
                trxnType = "General Insurance";
                forwardIntents();
                return;
            case R.id.btn4_2:
                trxnType = "Life Insurance";
                forwardIntents();
                return;
            case R.id.btn4_3:
                trxnType = "Term Insurance";
                forwardIntents();
                return;
            case R.id.btn4_4:
                trxnType = "Others";
                forwardIntents();
                return;
        }
    }


    private boolean forwardIntents() {
        Intent intent;
        switch (trxnType){
            case "General Insurance":
            case "Life Insurance":
            case "Term Insurance":
            case "Others":
                intent = new Intent(insuranceSubList.this, mainList.class);
                intent.putExtra("src", trxnType);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}