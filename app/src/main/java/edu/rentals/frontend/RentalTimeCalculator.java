package edu.rentals.frontend;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RentalTimeCalculator {
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
