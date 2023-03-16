package com.example.sharestransactions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class miscFuns extends BroadcastReceiver {

    public static void showAlert(Context context, String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(data).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onReceive(Context context, Intent intent){
        try {
            if(!isOnline(context)){
                showAlert(context, "Data not Connected");
            } else {

            }
        } catch (NullPointerException e){

        }
    }

    public boolean isOnline(Context context){
        try{
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        } catch (NullPointerException e){
            return false;
        }
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String monthStr = String.valueOf(month+1);
        String dateStr = String.valueOf(dayOfMonth);
        if (month < 9){
            monthStr = "0"+monthStr;
        }
        if (dayOfMonth < 10){
            dateStr = "0"+dateStr;
        }
        String dateSet = year + "-" + monthStr + "-" + dateStr;
//        date.setText(dateSet);
        return dateSet;
    }
}
