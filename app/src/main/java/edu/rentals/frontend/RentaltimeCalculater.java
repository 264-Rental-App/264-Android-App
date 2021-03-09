package edu.rentals.frontend;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class RentaltimeCalculater {
    private static Long returnTime;

    public static String getRemainingTime() {
        Long currentTime = System.currentTimeMillis();

        Long diff = returnTime - currentTime;
        Date d = new Date(diff);
        String remainingtime = new SimpleDateFormat("hh:mm:ss").format(d);
        return remainingtime;
    }

    public static void setreturnTime(Long duedate){
        returnTime = duedate;
    }
}
