package com.example.sharestransactions.subLists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sharestransactions.R;
import com.example.sharestransactions.lists.mainList;

public class bankPOSubList extends AppCompatActivity implements View.OnClickListener {

    Button banks, poInstruments, pf, other;
    String trxnType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_po_sublist);

        banks = findViewById(R.id.btn3_1);
        poInstruments = findViewById(R.id.btn3_2);
        pf = findViewById(R.id.btn3_3);
        other = findViewById(R.id.btn3_4);

        banks.setOnClickListener(this);
        poInstruments.setOnClickListener(this);
        pf.setOnClickListener(this);
        other.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn3_1:
                trxnType = "Banks";
                forwardIntents();
                return;
            case R.id.btn3_2:
                trxnType = "PO Instruments";
                forwardIntents();
                return;
            case R.id.btn3_3:
                trxnType = "PF";
                forwardIntents();
                return;
            case R.id.btn3_4:
                trxnType = "Other";
                forwardIntents();
                return;
        }
    }

    private boolean forwardIntents() {
        Intent intent;
        switch (trxnType){
            case "Banks":
            case "PO Instruments":
            case "PF":
            case "Other":
                intent = new Intent(bankPOSubList.this, mainList.class);
                intent.putExtra("src", trxnType);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}