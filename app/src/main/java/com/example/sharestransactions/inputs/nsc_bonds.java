package com.example.sharestransactions.transactions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharestransactions.R;
import com.example.sharestransactions.details;
import com.example.sharestransactions.miscFuns;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class nsc_bonds extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    Button save;
    TextView title;
    EditText name, date, amount, quantity, maturityDate, maturityAmount, remarks;
    String nameStr, dateStr, quantityStr, comissionStr, BuySellpriceStr, typeOfPurchaseStr, maturityAmountStr, remarksStr, srcStr, idIntentStr, trxnTypeStr;
    String[] cleared = {"", "Interaday", "Short Selling"};
    com.example.sharestransactions.details details;
    DatabaseReference db;
    FirebaseAuth mFirebaseAuth;
    int count = 0, flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsc_bonds);

        Intent data = getIntent();
        idIntentStr = data.getStringExtra("id");
        srcStr = data.getStringExtra("src");
        trxnTypeStr = data.getStringExtra("trxnType");

        name = findViewById(R.id.editTextTextPersonName9);
        date = findViewById(R.id.editTextTextPersonName14);
        amount = findViewById(R.id.editTextTextPersonName10);
        quantity = findViewById(R.id.editTextTextPersonName11);
        remarks = findViewById(R.id.editTextTextPersonName13);
        maturityDate = findViewById(R.id.editTextTextPersonName17);
        maturityAmount = findViewById(R.id.editTextTextPersonName56);

        title = findViewById(R.id.textView13);
        save = findViewById(R.id.button3);

        details = new details();

//        mFirebaseAuth = FirebaseAuth.getInstance();
//        String uid = FirebaseAuth.getInstance().getUid();
//        db = FirebaseDatabase.getInstance().getReference().child(uid).child(trxnTypeStr);

//        getIndex();

        title.setText(trxnTypeStr);

        if(srcStr != null){
            if (srcStr.equals("add_button")){
                name.setEnabled(true);
                date.setEnabled(true);
                quantity.setEnabled(true);
                amount.setEnabled(true);
                maturityDate.setEnabled(true);
                remarks.setEnabled(true);
                save.setText("Save");
            }
        }

        if (idIntentStr != null) {
            fillData(idIntentStr);
        }

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
                BuySellpriceStr = amount.getText().toString().trim();
                quantityStr = quantity.getText().toString().trim();
                maturityAmountStr = maturityAmount.getText().toString().trim();
                remarksStr = remarks.getText().toString().trim();
                comissionStr = maturityDate.getText().toString().trim();

                if (!srcStr.equals("add_button")) {
                    db.child(idIntentStr).child("name").setValue(nameStr);
                    db.child(idIntentStr).child("date").setValue(dateStr);
                    db.child(idIntentStr).child("amount").setValue(BuySellpriceStr);
                    db.child(idIntentStr).child("quantity").setValue(quantityStr);
                    db.child(idIntentStr).child("maturityAmount").setValue(maturityAmountStr);
                    db.child(idIntentStr).child("remarks").setValue(remarksStr);
                    db.child(idIntentStr).child("typeOfPurchase").setValue(typeOfPurchaseStr);
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    miscFuns.showAlert(nsc_bonds.this, "Data Updated Successfully");
                } else {
                    details.setName(nameStr);
                    details.setDate(dateStr);
                    details.setBuySellPrice(BuySellpriceStr);
                    details.setQuantity(quantityStr);
                    details.setSquareOffPrice(maturityAmountStr);
                    details.setRemarks(remarksStr);
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    flag = '1';
                    db.child(nameStr + miscFuns.getDateTime()).setValue(details);
                    miscFuns.showAlert(nsc_bonds.this, "Data Added Successfully");
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        switch (parent.getId()) {
//            case R.id.spinner2:
//                typeOfPurchaseStr = cleared[position];
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    private void fillData(String childNameStr) {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    nameStr = snapshot.child(childNameStr).child("name").getValue().toString();
                    dateStr = snapshot.child(childNameStr).child("date").getValue().toString();
                    quantityStr = snapshot.child(childNameStr).child("quantity").getValue().toString();
                    BuySellpriceStr = snapshot.child(childNameStr).child("buySellPrice").getValue().toString();
                    typeOfPurchaseStr = snapshot.child(childNameStr).child("typeOfPurchase").getValue().toString();
                    maturityAmountStr = snapshot.child(childNameStr).child("squareOffPrice").getValue().toString();
                    remarksStr = snapshot.child(childNameStr).child("remarks").getValue().toString();

                    name.setText(nameStr);
                    date.setText(dateStr);
                    quantity.setText(quantityStr);
                    amount.setText(BuySellpriceStr);
                    remarks.setText(remarksStr);
                    remarks.setText(remarksStr);
                } catch (Exception e) {
                    if (flag != '1')
                        miscFuns.showAlert(nsc_bonds.this, "Error Fetching Data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private int getSpinIndex(Spinner spinner, String myString) {
//        for (int i = 0; i < spinner.getCount(); i++) {
//            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
//                return i;
//            }
//        }
//        return 0;
//    }


    private void showDatePickerDialog() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (!srcStr.equals("add_button")) {
            inflater.inflate(R.menu.title_menu, menu);
        } else {
            inflater.inflate(R.menu.menu3, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                name.setEnabled(true);
                date.setEnabled(true);
                quantity.setEnabled(true);
                amount.setEnabled(true);
                remarks.setEnabled(true);
                return true;
            case R.id.clear:
                name.setText("");
                date.setText("");
                quantity.setText("");
                amount.setText("");
                remarks.setText("");
                name.requestFocus();
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
}