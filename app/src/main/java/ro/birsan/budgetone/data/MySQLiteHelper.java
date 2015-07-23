package ro.birsan.budgetone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Irinel on 7/21/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_CATEGORIES_COLUMN_ID = "_id";
    public static final String TABLE_CATEGORIES_COLUMN_NAME = "name";
    public static final String TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY = "parent";
    public static final String TABLE_CATEGORIES_COLUMN_MIN_PERCENTAGE = "min_percentage";
    public static final String TABLE_CATEGORIES_COLUMN_MAX_PERCENTAGE = "max_percentage";

    private static final String TABLE_CATEGORIES_CREATE = "create table "
            + TABLE_CATEGORIES + "(" + TABLE_CATEGORIES_COLUMN_ID
            + " integer primary key autoincrement, "
            + TABLE_CATEGORIES_COLUMN_NAME + " text not null, "
            + TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " integer null, "
            + TABLE_CATEGORIES_COLUMN_MIN_PERCENTAGE + " integer null, "
            + TABLE_CATEGORIES_COLUMN_MAX_PERCENTAGE + " integer null "
            + ");";

    private static final String POPULATE_TABLE_CATEGORIES =
            "INSERT INTO " + TABLE_CATEGORIES + " (" + TABLE_CATEGORIES_COLUMN_ID + "," + TABLE_CATEGORIES_COLUMN_NAME + "," + TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + "," + TABLE_CATEGORIES_COLUMN_MIN_PERCENTAGE + "," + TABLE_CATEGORIES_COLUMN_MAX_PERCENTAGE + ") VALUES "
                    + "(1, 'Housing', NULL, 25, 35), "
                    + "(2, 'Mortgage/Rent', 1, NULL, NULL), "
                    + "(3, 'Household Repairs', 1, NULL, NULL), "

                    + "(4, 'Utilities', NULL, 5, 10), "
                    + "(5, 'Water', 4, NULL, NULL), "
                    + "(6, 'Electricity', 4, NULL, NULL), "
                    + "(7, 'Heating', 4, NULL, NULL), "
                    + "(8, 'Garbage', 4, NULL, NULL), "
                    + "(9, 'Phones', 4, NULL, NULL), "
                    + "(10, 'Cable', 4, NULL, NULL), "
                    + "(11, 'Internet', 4, NULL, NULL), "

                    + "(12, 'Food', NULL, 5, 15), "
                    + "(13, 'Groceries', 12, NULL, NULL), "
                    + "(14, 'Restaurants', 12, NULL, NULL), "

                    + "(15, 'Clothing', NULL, 2, 4), "
                    + "(16, 'Children’s Clothing', 15, NULL, NULL), "
                    + "(17, 'Adults’ Clothing', 15, NULL, NULL), "

                    + "(18, 'Medical', NULL, 5, 10), "
                    + "(19, 'Primary Care', 18, NULL, NULL), "
                    + "(20, 'Dental Care', 18, NULL, NULL), "
                    + "(21, 'Specialty Care', 18, NULL, NULL), "
                    + "(22, 'Medications', 18, NULL, NULL), "
                    + "(23, 'Medical Devices', 18, NULL, NULL), "

                    + "(24, 'Giving', NULL, 5, 15), "
                    + "(25, 'Offerings', 24, NULL, NULL), "
                    + "(26, 'Charities', 24, NULL, NULL), "

                    + "(27, 'Savings', NULL, 5, 10), "
                    + "(28, 'Emergency Fund', 27, NULL, NULL), "
                    + "(29, 'Hill and Valley Fund', 27, NULL, NULL), "
                    + "(30, 'Other Savings', 27, NULL, NULL), "

                    + "(31, 'Fun Money', NULL, 5, 9), "
                    + "(32, 'Entertainment', 31, NULL, NULL), "
                    + "(33, 'Games', 31, NULL, NULL), "
                    + "(34, 'Eating Out', 31, NULL, NULL), "
                    + "(35, 'Spontaneous Giving', 31, NULL, NULL), "
                    + "(36, 'Vacations', 31, NULL, NULL), "
                    + "(37, 'Subscriptions', 31, NULL, NULL), "

                    + "(38, 'Transportation', NULL, 10, 15), "
                    + "(39, 'Fuel', 38, NULL, NULL), "
                    + "(40, 'Maintenance', 38, NULL, NULL), "
                    + "(41, 'Parking Fees', 38, NULL, NULL), "
                    + "(42, 'Repairs', 38, NULL, NULL), "

                    + "(43, 'Personal/Misc', NULL, 2, 7), "
                    + "(44, 'Gym Memberships', 43, NULL, NULL), "
                    + "(45, 'Hair Cuts', 43, NULL, NULL)";


    private static final String DATABASE_NAME = "budgetone.db";
    private static final int DATABASE_VERSION = 1;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CATEGORIES_CREATE);
        db.execSQL(POPULATE_TABLE_CATEGORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
