package ro.birsan.budgetone.data;

import android.database.Cursor;

import java.text.ParseException;
import java.util.Date;

import ro.birsan.budgetone.util.DateTimeHelper;

/**
 * Created by Irinel on 7/29/2015.
 */
public class Income {

    public static final String TABLE_NAME = "incomes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_CREATED_ON = "created_on";
    public static final String COLUMN_AMOUNT = "amount";

    public static final String TABLE_INCOMES_CREATE = "create table "
            + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CATEGORY + " text not null, "
            + COLUMN_CREATED_ON + " text not null, "
            + COLUMN_AMOUNT + " real not null"
            + ");";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_CATEGORY,
            COLUMN_CREATED_ON,
            COLUMN_AMOUNT
    };

    private long _id;
    private Date createdOn;
    private String _category;
    private double _amount;

    public Date get_createdOn() {
        return createdOn;
    }

    public void set_createdOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String get_category() {
        return _category;
    }

    public void set_category(String _category) {
        this._category = _category;
    }

    public double get_amount() {
        return _amount;
    }

    public void set_amount(double _amount) {
        this._amount = _amount;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public static final Income cursorToIncome(Cursor cursor) {
        Income income = new Income();
        income.set_id(cursor.getLong(0));
        income.set_category(cursor.getString(1));

        try {
            income.set_createdOn(DateTimeHelper.ISO8601DateFormat.parse(cursor.getString(2)));
        } catch (ParseException e) {
            e.printStackTrace();
            income.set_createdOn(new Date());
        }

        income.set_amount(cursor.getFloat(3));
        return income;
    }
}
