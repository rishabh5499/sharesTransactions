package com.example.sharestransactions.transactions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sharestransactions.R;

public class mfunds_bonds extends AppCompatActivity {

    EditText name, amount, date, units, broker, remarks;
    Spinner status;
    String nameStr, amountStr, dateStr, unitsStr, brokerStr, statusStr, remarksStr;
    Button save, copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfund_bonds);

        name = findViewById(R.id.editTextTextPersonName19);
        amount = findViewById(R.id.editTextTextPersonName20);
        date = findViewById(R.id.editTextTextPersonName21);
        units = findViewById(R.id.editTextTextPersonName22);
        broker = findViewById(R.id.editTextTextPersonName23);
        remarks = findViewById(R.id.editTextTextPersonName24);

        status = findViewById(R.id.spinner4);

        save = findViewById(R.id.button);
        copy = findViewById(R.id.button7);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameStr = name.getText().toString().trim();
                amountStr = amount.getText().toString().trim();
                dateStr = date.getText().toString().trim();
                unitsStr = units.getText().toString().trim();
                brokerStr = broker.getText().toString().trim();
                remarksStr = remarks.getText().toString().trim();


            }
        });
    }
}