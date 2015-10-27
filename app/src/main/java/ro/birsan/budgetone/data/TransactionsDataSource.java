package ro.birsan.budgetone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ro.birsan.budgetone.util.DateTimeHelper;

/**
 * Created by Irinel on 9/8/2015.
 */
public class TransactionsDataSource extends DataSourceBase {
    public TransactionsDataSource(Context context) {
        super(context);
    }

    public void addTransactionForCategory(long categoryId, double amount) {
        ContentValues values = new ContentValues();
        values.put(Transaction.COLUMN_CATEGORY_ID, categoryId);
        values.put(Transaction.COLUMN_AMOUNT, amount);
        values.put(Transaction.COLUMN_CREATED_ON, DateTimeHelper.ISO8601DateFormat.format(new Date()));
        _writableDatabase.insert(Transaction.TABLE_NAME, null, values);
    }

    public void addTransactionForGoal(UUID goalId, double amount) {
        ContentValues values = new ContentValues();
        values.put(Transaction.COLUMN_GOAL_ID, goalId.toString());
        values.put(Transaction.COLUMN_AMOUNT, amount);
        values.put(Transaction.COLUMN_CREATED_ON, DateTimeHelper.ISO8601DateFormat.format(new Date()));
        _writableDatabase.insert(Transaction.TABLE_NAME, null, values);
    }

    public List<Transaction> getTransactionsByGoal(UUID goalId)
    {
        String query = "SELECT * from " + Transaction.TABLE_NAME
                + " WHERE "
                + Transaction.COLUMN_GOAL_ID + " = '" + goalId.toString() + "'"
                + ";";
        Cursor cursor = _readableDatabase.rawQuery(query, null);
        return cursorToList(cursor);
    }

    public double getExpensesAmountForCurrentMonth(long categoryId) {
        Calendar c = Calendar.getInstance();
        String query = "SELECT sum(" + Transaction.COLUMN_AMOUNT + ") from " + Transaction.TABLE_NAME
                + " WHERE "
                + Transaction.COLUMN_CATEGORY_ID + " = " + categoryId
                + " AND CAST(strftime('%Y', " + Transaction.COLUMN_CREATED_ON + ") AS decimal) = " + c.get(Calendar.YEAR)
                + " AND CAST(strftime('%m', " + Transaction.COLUMN_CREATED_ON + ") AS decimal) = " + (c.get(Calendar.MONTH) + 1)
                + ";";
        Cursor cursor = _readableDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        double amount = cursor.getDouble(0);
        cursor.close();
        return amount;
    }

    public List<Transaction> getAllTransactionsByMonth(int year, int month) {
        String query = "SELECT * "
                + " from " + Transaction.TABLE_NAME
                + " WHERE CAST(strftime('%Y', " + Transaction.COLUMN_CREATED_ON + ") AS decimal) = " + year
                + " AND CAST(strftime('%m', " + Transaction.COLUMN_CREATED_ON + ") AS decimal) = " + month
                + " ORDER BY " + Transaction.COLUMN_CREATED_ON + " DESC;";
        Cursor cursor = _readableDatabase.rawQuery(query, null);
        return cursorToList(cursor);
    }

    public void remove(Long transactionId) {
        _writableDatabase.delete(
                Transaction.TABLE_NAME,
                Transaction.COLUMN_ID + " = ? ",
                new String[]{transactionId.toString()});
    }

    public static List<Transaction> cursorToList(Cursor cursor) {
        List<Transaction> transactions = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            transactions.add(Transaction.cursorToTransaction(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return transactions;
    }

    public Map<String, Double> getExpensesAmountPerMonth(Long categoryId, Integer toMonth, Integer toYear) {
        Map<String, Double> expensesPerMonth = new HashMap<>();
        String query = "SELECT sum(" + Transaction.COLUMN_AMOUNT + "), strftime('%Ym', " + Transaction.COLUMN_CREATED_ON + ")"
                + " FROM " + Transaction.TABLE_NAME
                + " WHERE " + Transaction.COLUMN_CATEGORY_ID + " = " + categoryId
                + " AND strftime('%Ym', " + Transaction.COLUMN_CREATED_ON + ") = " + toYear.toString() + toMonth.toString()
                + " GROUP BY strftime('%Ym', " + Transaction.COLUMN_CREATED_ON + ")";
        Cursor cursor = _readableDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            expensesPerMonth.put(cursor.getString(1), cursor.getDouble(0));
            cursor.moveToNext();
        }
        return expensesPerMonth;
    }

    public Double getTransactionsAmount() {
        String query = "SELECT sum(" + Transaction.COLUMN_AMOUNT + ") from " + Transaction.TABLE_NAME + ";";
        Cursor cursor = _readableDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        double amount = 0.0;
        if (!cursor.isAfterLast())
        {
            amount = cursor.getDouble(0);
        }

        cursor.close();
        return amount;
    }

    public Double getTransactionsAmountByMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String query = "SELECT sum(" + Transaction.COLUMN_AMOUNT + ") from " + Transaction.TABLE_NAME
                + " WHERE CAST(strftime('%Y', " + Transaction.COLUMN_CREATED_ON + ") AS decimal) = " + c.get(Calendar.YEAR)
                + " AND CAST(strftime('%m', " + Transaction.COLUMN_CREATED_ON + ") AS decimal) = " + (c.get(Calendar.MONTH) + 1) + ";";
        Cursor cursor = _readableDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        double amount = 0.0;
        if (!cursor.isAfterLast())
        {
            amount = cursor.getDouble(0);
        }

        cursor.close();
        return amount;
    }

    public int getTransactionsCountByCategory(long categoryId) {
        String query = "SELECT COUNT(*) FROM " + Transaction.TABLE_NAME
                + " WHERE " + Transaction.COLUMN_CATEGORY_ID + " = " + categoryId;
        return (int)DatabaseUtils.longForQuery(_readableDatabase, query, null);
    }
}
