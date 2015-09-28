package ro.birsan.budgetone.data;

import android.database.Cursor;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import ro.birsan.budgetone.util.DateTimeHelper;

/**
 * Created by Irinel on 9/8/2015.
 */
public class Transaction {
    public static final String TABLE_NAME = "transactions";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_GOAL_ID = "goal_id";
    public static final String COLUMN_CREATED_ON = "created_on";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_CATEGORY_ID,
            COLUMN_CREATED_ON,
            COLUMN_AMOUNT
    };

    public static final String TABLE_CREATE = "create table "
            + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CATEGORY_ID + " integer null, "
            + COLUMN_GOAL_ID + " text null, "
            + COLUMN_CREATED_ON + " text null, " //ISO8601 string format
            + COLUMN_AMOUNT + " real null"
            + ");";

    private long _categoryId;
    private UUID _goalId;
    private Date _createdOn;
    private Double _amount;
    private long _id;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_categoryId() {
        return _categoryId;
    }

    public void set_categoryId(long _categoryId) {
        this._categoryId = _categoryId;
    }

    public UUID get_goalId() {
        return _goalId;
    }

    public void set_goalId(UUID _goalId) {
        this._goalId = _goalId;
    }

    public Date get_createdOn() {
        return _createdOn;
    }

    public void set_createdOn(Date _createdOn) {
        this._createdOn = _createdOn;
    }

    public Double get_amount() {
        return _amount;
    }

    public void set_amount(Double _amount) {
        this._amount = _amount;
    }

    public static final Transaction cursorToTransaction(Cursor cursor) throws ParseException {
        Transaction transaction = new Transaction();
        transaction.set_id(cursor.getLong(0));
        transaction.set_categoryId(cursor.getLong(1));
        transaction.set_goalId(UUID.fromString(cursor.getString(2)));
        transaction.set_createdOn(DateTimeHelper.ISO8601DateFormat.parse(cursor.getString(3)));
        transaction.set_amount(cursor.getDouble(4));
        return transaction;
    }
}
