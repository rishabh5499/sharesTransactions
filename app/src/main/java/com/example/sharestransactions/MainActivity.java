package com.example.sharestransactions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.sharestransactions.auth.login;
import com.example.sharestransactions.helperClasses.miscFuns;
import com.example.sharestransactions.lists.otherList;
import com.example.sharestransactions.subLists.bankPOSubList;
import com.example.sharestransactions.subLists.commoditiesSubList;
import com.example.sharestransactions.subLists.insuranceSubList;
import com.example.sharestransactions.subLists.selfTransferSubList;
import com.example.sharestransactions.subLists.stockMarket;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button go;
    RadioGroup rg;
    RadioButton rb;
    Button shareMarket, bank_po, insurance, commodity, selfTransfer, other, bankList;
    String trxnType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shareMarket = findViewById(R.id.btn1);
        bank_po = findViewById(R.id.btn2);
        insurance = findViewById(R.id.btn3);
        commodity = findViewById(R.id.btn4);
        selfTransfer = findViewById(R.id.btn5);
        other = findViewById(R.id.btn6);
        bankList = findViewById(R.id.btn7);

        shareMarket.setOnClickListener(this);
        bank_po.setOnClickListener(this);
        insurance.setOnClickListener(this);
        commodity.setOnClickListener(this);
        selfTransfer.setOnClickListener(this);
        other.setOnClickListener(this);
        bankList.setOnClickListener(this);

//        go = findViewById(R.id.button);
//        rg = findViewById(R.id.rg);

//        go.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    int rbtnId = rg.getCheckedRadioButtonId();
//                    rb = findViewById(rbtnId);
//
//                    trxnType = (String) rb.getText();
//                    forwardIntents();
//                } catch (Exception e) {
//                    miscFuns.showAlert(MainActivity.this, "Make a selection");
//                }
//            }
//        });
    }

    private boolean forwardIntents() {
        Intent intent;
        switch (trxnType) {
            case "Share_Market":
                intent = new Intent(MainActivity.this, stockMarket.class);
                intent.putExtra("src", trxnType);
                 startActivity(intent);
                return true;
            case "Bank_PO":
                intent = new Intent(MainActivity.this, bankPOSubList.class);
                intent.putExtra("src", trxnType);
                startActivity(intent);
                return true;
            case "Insurance":
                intent = new Intent(MainActivity.this, insuranceSubList.class);
                intent.putExtra("src", trxnType);
                startActivity(intent);
                return true;
            case "Commodity":
                intent = new Intent(MainActivity.this, commoditiesSubList.class);
                intent.putExtra("src", trxnType);
                startActivity(intent);
                return true;
            case "Self_Transfer":
                intent = new Intent(MainActivity.this, selfTransferSubList.class);
                intent.putExtra("src", trxnType);
                startActivity(intent);
                return true;
            case "Other":
                intent = new Intent(MainActivity.this, otherList.class);
                intent.putExtra("src", trxnType);
                startActivity(intent);
                return true;
            case "Banks_List":
                intent = new Intent(MainActivity.this, banks.class);
                intent.putExtra("src", trxnType);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu4, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(MainActivity.this, login.class);
                startActivity(intToMain);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                trxnType = "Share_Market";
                forwardIntents();
                return;
            case R.id.btn2:
                trxnType = "Bank_PO";
                forwardIntents();
                return;
            case R.id.btn3:
                trxnType = "Insurance";
                forwardIntents();
                return;
            case R.id.btn4:
                trxnType = "Commodity";
                forwardIntents();
                return;
            case R.id.btn5:
                trxnType = "Self_Transfer";
                forwardIntents();
                return;
            case R.id.btn6:
                trxnType = "Other";
                forwardIntents();
                return;
            case R.id.btn7:
                trxnType = "Banks_List";
                forwardIntents();
                return;
            default:
                Intent bank = new Intent(MainActivity.this, MainActivity.class);
                miscFuns.showAlert(MainActivity.this, "Make a selection");
                startActivity(bank);
        }
    }
}
