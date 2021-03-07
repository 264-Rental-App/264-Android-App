package edu.rentals.frontend;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RentalDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "RentalDB";
    public static final int DB_VERSION = 1;

    public RentalDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE rentalDue (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                            + "userId TEXT, "
                                            + "count integer, "
                                            + "dueDate TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void add(SQLiteDatabase db, String userId, int count, String dueDate) {
        ContentValues  invoicelDue = new ContentValues();
        invoicelDue.put("userId", userId);
        invoicelDue.put("count", count);
        invoicelDue.put("dueDate", dueDate);
        db.insert("rentalDue", null, invoicelDue);
    }
}
