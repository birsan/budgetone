package ro.birsan.budgetone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ro.birsan.budgetone.util.DateTimeHelper;

/**
 * Created by Irinel on 9/8/2015.
 */
public class TransactionsDataSource extends DataSourceBase {
    public TransactionsDataSource(Context context) {
        super(context);
    }

    public void addTransaction(long categoryId, double amount) {
        ContentValues values = new ContentValues();
        values.put(Transaction.COLUMN_CATEGORY_ID, categoryId);
        values.put(Transaction.COLUMN_AMOUNT, amount);
        values.put(Transaction.COLUMN_CREATED_ON, DateTimeHelper.ISO8601DateFormat.format(new Date()));
        _writableDatabase.insert(Transaction.TABLE_NAME, null, values);
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

    public static List<Transaction> cursorToList(Cursor cursor) throws ParseException {
        List<Transaction> transactions = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            transactions.add(Transaction.cursorToTransaction(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return transactions;
    }
}
