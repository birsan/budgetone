package ro.birsan.budgetone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ro.birsan.budgetone.util.DateTimeHelper;

/**
 * Created by Irinel on 7/25/2015.
 */
public class IncomesDataSource extends DataSourceBase {

    public IncomesDataSource(Context context) {
        super(context);
    }

    public void addIncome(double amount, String source) {
        Calendar c = Calendar.getInstance();
        ContentValues values = new ContentValues();
        values.put(Income.COLUMN_AMOUNT, amount);
        values.put(Income.COLUMN_CATEGORY, source);
        values.put(Income.COLUMN_CREATED_ON, DateTimeHelper.ISO8601DateFormat.format(new Date()));
        _writableDatabase.insert(Income.TABLE_NAME, null, values);
    }

    public List<Income> getCurrentMonthIncome() {
        Calendar c = Calendar.getInstance();
        return getIncome(c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
    }

    public List<Income> getIncome(int month, int year) {
        String query = "SELECT * FROM " + Income.TABLE_NAME
                + " WHERE CAST(strftime('%Y', " + Income.COLUMN_CREATED_ON + ") AS decimal) = " + year
                + " AND CAST(strftime('%m', " + Income.COLUMN_CREATED_ON + ") AS decimal) = " + month
                + " ORDER BY " + Income.COLUMN_CREATED_ON + " DESC;";
        return cursorToList(_readableDatabase.rawQuery(query, null));
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList();
        Cursor cursor = _readableDatabase.rawQuery("SELECT DISTINCT " + Income.COLUMN_CATEGORY + " from " + Income.TABLE_NAME + ";", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (!categories.contains(cursor.getString(0))) {
                categories.add(cursor.getString(0));
            }
            cursor.moveToNext();
        }
        cursor.close();
        return categories;
    }

    public double getIncomeAmount() {
        Cursor cursor = _readableDatabase.rawQuery("SELECT sum(" + Income.COLUMN_AMOUNT + ") from " + Income.TABLE_NAME + ";", null);
        cursor.moveToFirst();
        double amount = cursor.getDouble(0);
        cursor.close();
        return amount;
    }

    public void remove(Long transactionId) {
        _writableDatabase.delete(
                Income.TABLE_NAME,
                Income.COLUMN_ID + " = ? ",
                new String[]{transactionId.toString()});
    }

    public static List<Income> cursorToList(Cursor cursor) {
        List<Income> incomes = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            incomes.add(Income.cursorToIncome(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return incomes;
    }

    public void close() {
        _dbHelper.close();
    }
}
