package edu.rentals.frontend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class RentalTimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                //String remainingtime = RentaltimeCalculater.getRemainingTime();
                //Toast.makeText(context, "SCREEN ON", Toast.LENGTH_SHORT).show();

                Long diff = Long.valueOf("1615237200000") - System.currentTimeMillis();
                Date d = new Date(diff);
                String remainingtime = new SimpleDateFormat("hh:mm:ss").format(d);

                Toast toast = Toast.makeText(context, "Your Equipments Remaining: " + remainingtime, Toast.LENGTH_LONG);
                toast.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}