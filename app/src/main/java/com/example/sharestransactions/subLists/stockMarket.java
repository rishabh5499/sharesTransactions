package com.example.sharestransactions.subLists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sharestransactions.R;
import com.example.sharestransactions.lists.mainList;

public class stockMarket extends AppCompatActivity implements View.OnClickListener {

    Button longTerm, shortTerm, mutualFunds, fno, nps, bonds;
    String trxnType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_market);

        longTerm = findViewById(R.id.btn2_1);
        shortTerm = findViewById(R.id.btn2_2);
        mutualFunds = findViewById(R.id.btn2_3);
        fno = findViewById(R.id.btn2_4);
        nps = findViewById(R.id.btn2_5);
        bonds = findViewById(R.id.btn2_6);

        longTerm.setOnClickListener(this);
        shortTerm.setOnClickListener(this);
        mutualFunds.setOnClickListener(this);
        fno.setOnClickListener(this);
        nps.setOnClickListener(this);
        bonds.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn2_1:
                trxnType = "Long Term";
                forwardIntents();
                return;
            case R.id.btn2_2:
                trxnType = "Short Term";
                forwardIntents();
                return;
            case R.id.btn2_3:
                trxnType = "Mutual Funds";
                forwardIntents();
                return;
            case R.id.btn2_4:
                trxnType = "Futures Options";
                forwardIntents();
                return;
            case R.id.btn2_5:
                trxnType = "NPS";
                forwardIntents();
                return;
            case R.id.btn2_6:
                trxnType = "Bonds";
                forwardIntents();
                return;
        }
    }

    private boolean forwardIntents() {
        Intent intent;
        switch (trxnType){
            case "Long Term":
            case "Short Term":
            case "Mutual Funds":
            case "Futures Options":
            case "NPS":
            case "Bonds":
                intent = new Intent(stockMarket.this, mainList.class);
                intent.putExtra("src", trxnType);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}