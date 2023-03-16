package com.example.sharestransactions.transactions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.app.DatePickerDialog;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.example.sharestransactions.R;
import com.example.sharestransactions.details;
import com.example.sharestransactions.miscFuns;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class stockMarketTrxn extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    Button save, caluclate, copy;
    TextView title, sipNumberTitle;
    EditText name, date, quantity, price, dateOfSale, quantityOfSale, squareOffPrice, gttTrigger, sipNumber, remarks, totalInvestment;
    String nameStr, dateStr, quantityStr, priceStr, exchangeStr, quantityOfSaleStr, squareOffPriceStr, gttSetStr, gttTriggerStr, commissionStr, remarksStr, srcStr, idIntentStr, trxnTypeStr, totalInvestmentStr, indexStr;
    TextView textView;
    com.example.sharestransactions.details details;
    DatabaseReference db;
    FirebaseAuth mFirebaseAuth;
    Spinner spin, spin2;
    String[] exchange = {"", "BSE", "NSE"};
    int count = 0, flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_market_trxn);

        Intent data = getIntent();
        idIntentStr = data.getStringExtra("id");
        srcStr = data.getStringExtra("src");
        trxnTypeStr = data.getStringExtra("trxnType");
        indexStr = data.getStringExtra("index");

        textView = findViewById(R.id.textView27);

        name = findViewById(R.id.editTextTextPersonName);
        quantity = findViewById(R.id.editTextTextPersonName3);
        price = findViewById(R.id.editTextTextPersonName4);
        spin = findViewById(R.id.spinner);
        date = findViewById(R.id.editTextTextPersonName2);
        dateOfSale = findViewById(R.id.editTextTextPersonName5);
        quantityOfSale = findViewById(R.id.editTextTextPersonName55);
        squareOffPrice = findViewById(R.id.editTextTextPersonName6);
        spin2 = findViewById(R.id.spinner6);
        sipNumber = findViewById(R.id.editTextTextPersonName7);
        totalInvestment = findViewById(R.id.editTextTextPersonName18);
        remarks = findViewById(R.id.editTextTextPersonName8);
        title = findViewById(R.id.textView2);
        sipNumberTitle = findViewById(R.id.textView11);

        caluclate = findViewById(R.id.button6);
        save = findViewById(R.id.button2);
        copy = findViewById(R.id.button15);

        details = new details();
        spin.setEnabled(false);
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        String uid = FirebaseAuth.getInstance().getUid();
//        db = FirebaseDatabase.getInstance().getReference().child(uid).child(trxnTypeStr);

//        getIndex();

        title.setText(trxnTypeStr);

        if(srcStr != null){
            if (srcStr.equals("add_button")){
                name.setEnabled(true);
                quantity.setEnabled(true);
                price.setEnabled(true);
                spin.setEnabled(true);
                date.setEnabled(true);
                dateOfSale.setEnabled(true);
                quantityOfSale.setEnabled(true);
                squareOffPrice.setEnabled(true);
                spin2.setEnabled(true);
                sipNumber.setEnabled(true);
                totalInvestment.setEnabled(true);
                remarks.setEnabled(true);
                if (!trxnTypeStr.equals("Mutual Funds")){
                    sipNumberTitle.setVisibility(View.GONE);
                    sipNumber.setVisibility(View.GONE);
                }
                save.setText("Save");
            }
        }

        if (idIntentStr != null) {
            fillData(idIntentStr);
        }
        spin.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_list_item_1, exchange);
        spin.setAdapter(ad);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameStr = name.getText().toString().trim();
                dateStr = date.getText().toString().trim();
                quantityStr = quantity.getText().toString().trim();
                priceStr = price.getText().toString().trim();
                gttSetStr = dateOfSale.getText().toString().trim();
                gttTriggerStr = gttTrigger.getText().toString().trim();
                commissionStr = sipNumber.getText().toString().trim();
                remarksStr = remarks.getText().toString().trim();

                try {
                    totalInvestmentStr = String.valueOf(calculateTotal(trxnTypeStr));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!srcStr.equals("add_button")) {
                    db.child(idIntentStr).child("name").setValue(nameStr);
                    db.child(idIntentStr).child("date").setValue(dateStr);
                    db.child(idIntentStr).child("quantity").setValue(quantityStr);
                    db.child(idIntentStr).child("price").setValue(priceStr);
                    db.child(idIntentStr).child("gttSet").setValue(gttSetStr);
                    db.child(idIntentStr).child("gttTrigger").setValue(gttTriggerStr);
                    db.child(idIntentStr).child("commission").setValue(commissionStr);
                    db.child(idIntentStr).child("remarks").setValue(remarksStr);
                    db.child(idIntentStr).child("exchange").setValue(exchangeStr);
                    db.child(idIntentStr).child("totalInvestment").setValue(totalInvestmentStr);
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    miscFuns.showAlert(stockMarketTrxn.this, "Data Updated Successfully");
                } else {
                    details.setName(nameStr);
                    details.setDate(dateStr);
                    details.setQuantity(quantityStr);
                    details.setPrice(priceStr);
                    
                    details.setGttSet(gttSetStr);
                    details.setGttTrigger(gttTriggerStr);
                    details.setCommission(commissionStr);
                    details.setRemarks(remarksStr);
                    details.setExchange(exchangeStr);
                    details.setTotalInvestment(totalInvestmentStr);
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    flag = '1';
                    db.child(nameStr + " " + miscFuns.getDateTime()).setValue(details);
                    miscFuns.showAlert(stockMarketTrxn.this, "Data Added Successfully");
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        caluclate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    calculateTotal(trxnTypeStr);
                } catch (Exception e){
                    e.printStackTrace();
                }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner:
                exchangeStr = exchange[position];
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date.setText(miscFuns.onDateSet(view, year, month, dayOfMonth));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void fillData(String childNameStr) {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    nameStr = snapshot.child(childNameStr).child("name").getValue().toString();
                    dateStr = snapshot.child(childNameStr).child("date").getValue().toString();
                    quantityStr = snapshot.child(childNameStr).child("quantity").getValue().toString();
                    priceStr = snapshot.child(childNameStr).child("price").getValue().toString();
                    exchangeStr = snapshot.child(childNameStr).child("exchange").getValue().toString();
                    gttSetStr = snapshot.child(childNameStr).child("gttSet").getValue().toString();
                    gttTriggerStr = snapshot.child(childNameStr).child("gttTrigger").getValue().toString();
                    commissionStr = snapshot.child(childNameStr).child("commission").getValue().toString();
                    remarksStr = snapshot.child(childNameStr).child("remarks").getValue().toString();
                    totalInvestmentStr = snapshot.child(childNameStr).child("totalInvestment").getValue().toString();

                    name.setText(nameStr);
                    date.setText(dateStr);
                    quantity.setText(quantityStr);
                    price.setText(priceStr);
                    dateOfSale.setText(gttSetStr);
                    gttTrigger.setText(gttTriggerStr);
                    sipNumber.setText(commissionStr);
                    remarks.setText(remarksStr);
                    spin.setSelection(getSpinIndex(spin, exchangeStr));
                    remarks.setText(remarksStr);
                    totalInvestment.setText(totalInvestmentStr);
                } catch (Exception e) {
                    if (flag != '1')
                        miscFuns.showAlert(stockMarketTrxn.this, "Error Fetching Data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private int getSpinIndex(Spinner spinner, String myString){
        for (int i=0; i<spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (!srcStr.equals("add_button")){
            inflater.inflate(R.menu.title_menu, menu);
        } else {
            inflater.inflate(R.menu.menu3, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                name.setEnabled(true);
                date.setEnabled(true);
                quantity.setEnabled(true);
                price.setEnabled(true);
                spin.setEnabled(true);
                dateOfSale.setEnabled(true);
                gttTrigger.setEnabled(true);
                sipNumber.setEnabled(true);
                remarks.setEnabled(true);
                return true;
            case R.id.clear:
                name.setText("");
                date.setText("");
                quantity.setText("");
                price.setText("");
                spin.setSelection(0);
                gttTrigger.setText("");
                dateOfSale.setText("");
                sipNumber.setText("");
                remarks.setText("");
                name.requestFocus();
                return true;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Sure to delete " + nameStr ).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.child(idIntentStr).getRef().removeValue();
                        Toast.makeText(getApplicationContext(), nameStr + " deleted", Toast.LENGTH_SHORT).show();
                        flag = '1';
                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
                        dialog.cancel();
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        Toast.makeText(getApplicationContext(), "Action Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Float calculateTotal(String trxnTypeStr) {
        quantityStr = quantity.getText().toString().trim();
        priceStr = price.getText().toString().trim();
        commissionStr = sipNumber.getText().toString().trim();
        Float totalInvestmentValue=0.0f;
        if (trxnTypeStr.equals("Sale")){
            totalInvestmentValue = (Float.valueOf(quantityStr)*Float.valueOf(priceStr))-Float.valueOf(commissionStr);
        } else if (trxnTypeStr.equals("Purchases")){
            totalInvestmentValue = (Float.valueOf(quantityStr)*Float.valueOf(priceStr))+Float.valueOf(commissionStr);
        }
        totalInvestment.setText(totalInvestmentValue.toString());

        return  totalInvestmentValue;
    }
}