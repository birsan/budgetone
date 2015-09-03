package ro.birsan.budgetone.data;

import android.database.Cursor;

/**
 * Created by Irinel on 7/25/2015.
 */
public class Budget {
    public static final String TABLE_BUDGETS = "budgets";
    public static final String TABLE_BUDGETS_COLUMN_ID = "_id";
    public static final String TABLE_BUDGETS_COLUMN_CATEGORY_ID = "category_id";
    public static final String TABLE_BUDGETS_COLUMN_MONTH = "month";
    public static final String TABLE_BUDGETS_COLUMN_YEAR = "year";
    public static final String TABLE_BUDGETS_COLUMN_AMOUNT = "amount";
    public static final String TABLE_BUDGETS_COLUMN_ROLLOVER_AMOUNT = "rollover_amount";

    public static final String TABLE_BUDGETS_CREATE = "create table "
            + TABLE_BUDGETS + "(" + TABLE_BUDGETS_COLUMN_ID + " integer primary key autoincrement, "
            + TABLE_BUDGETS_COLUMN_CATEGORY_ID + " integer not null, "
            + TABLE_BUDGETS_COLUMN_MONTH + " integer not null, "
            + TABLE_BUDGETS_COLUMN_YEAR + " integer not null, "
            + TABLE_BUDGETS_COLUMN_AMOUNT + " real not null, "
            + TABLE_BUDGETS_COLUMN_ROLLOVER_AMOUNT + " real null "
            + ");";

    private long id;
    private long _categoryId;
    private long _month;
    private long _year;
    private double _amount;
    private double _rolloverAmount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return _categoryId;
    }

    public void setCategoryId(long categoryId) {
        _categoryId = categoryId;
    }

    public long get_month() {
        return _month;
    }

    public void set_month(long month) {
        _month = month;
    }

    public long get_year() {
        return _year;
    }

    public void set_year(long year) {
        _year = year;
    }

    public double get_amount() {
        return _amount;
    }

    public void set_amount(double amount) {
        _amount = amount;
    }

    public double getTotalAmount() {
        return _amount + _rolloverAmount;
    }

    public double get_rolloverAmount() {
        return _rolloverAmount;
    }

    public void set_rolloverAmount(double rolloverAmount) {
        _rolloverAmount = rolloverAmount;
    }

    public static final String[] ALL_COLUMNS = {
            TABLE_BUDGETS_COLUMN_ID,
            TABLE_BUDGETS_COLUMN_CATEGORY_ID,
            TABLE_BUDGETS_COLUMN_MONTH,
            TABLE_BUDGETS_COLUMN_YEAR,
            TABLE_BUDGETS_COLUMN_AMOUNT,
            TABLE_BUDGETS_COLUMN_ROLLOVER_AMOUNT
    };

    public static final Budget cursorToBudget(Cursor cursor) {
        Budget budget = new Budget();
        budget.setId(cursor.getLong(0));
        budget.setCategoryId(cursor.getLong(1));
        budget.set_month(cursor.getInt(2));
        budget.set_year(cursor.getInt(3));
        budget.set_amount(cursor.getFloat(4));
        budget.set_rolloverAmount(cursor.getFloat(5));
        return budget;
    }
}
