package ro.birsan.budgetone.data;

import android.database.Cursor;

/**
 * Created by Irinel on 7/29/2015.
 */
public class Income {

    public static final String TABLE_INCOMES = "incomes";
    public static final String TABLE_INCOMES_COLUMN_ID = "_id";
    public static final String TABLE_INCOMES_COLUMN_CATEGORY = "category";
    public static final String TABLE_INCOMES_COLUMN_MONTH = "month";
    public static final String TABLE_INCOMES_COLUMN_YEAR = "year";
    public static final String TABLE_INCOMES_COLUMN_AMOUNT = "amount";

    public static final String TABLE_INCOMES_CREATE = "create table "
            + TABLE_INCOMES + "(" + TABLE_INCOMES_COLUMN_ID + " integer primary key autoincrement, "
            + TABLE_INCOMES_COLUMN_CATEGORY + " text not null, "
            + TABLE_INCOMES_COLUMN_MONTH + " integer not null, "
            + TABLE_INCOMES_COLUMN_YEAR + " integer not null, "
            + TABLE_INCOMES_COLUMN_AMOUNT + " real not null"
            + ");";

    public static final String[] ALL_COLUMNS = {
            TABLE_INCOMES_COLUMN_ID,
            TABLE_INCOMES_COLUMN_CATEGORY,
            TABLE_INCOMES_COLUMN_MONTH,
            TABLE_INCOMES_COLUMN_YEAR,
            TABLE_INCOMES_COLUMN_AMOUNT
    };

    private long _id;
    private int _month;
    private int _year;
    private String _category;
    private double _amount;

    public int get_month() {
        return _month;
    }

    public void set_month(int _month) {
        this._month = _month;
    }

    public int get_year() {
        return _year;
    }

    public void set_year(int _year) {
        this._year = _year;
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
        income.set_month(cursor.getInt(2));
        income.set_year(cursor.getInt(3));
        income.set_amount(cursor.getFloat(4));
        return income;
    }
}
