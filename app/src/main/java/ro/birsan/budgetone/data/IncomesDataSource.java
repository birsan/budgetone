package ro.birsan.budgetone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        values.put(Income.TABLE_INCOMES_COLUMN_AMOUNT, amount);
        values.put(Income.TABLE_INCOMES_COLUMN_CATEGORY, source);
        values.put(Income.TABLE_INCOMES_COLUMN_MONTH, c.get(Calendar.MONTH));
        values.put(Income.TABLE_INCOMES_COLUMN_YEAR, c.get(Calendar.YEAR));
        _writableDatabase.insert(Income.TABLE_INCOMES, null, values);
    }

    public Cursor getCurrentMonthIncome() {
        Calendar c = Calendar.getInstance();
        return getIncome(c.get(Calendar.MONTH), c.get(Calendar.YEAR));
    }

    public Cursor getIncome(int month, int year) {
        return _readableDatabase.query(
                Income.TABLE_INCOMES,
                Income.ALL_COLUMNS,
                Income.TABLE_INCOMES_COLUMN_MONTH + " == ? AND " + Income.TABLE_INCOMES_COLUMN_YEAR + " == ?",
                new String[]{Integer.valueOf(month).toString(), Integer.valueOf(year).toString()},
                null, null, null);
    }

    /**
     * @return current available amount computed as sum of all incomes minus sum of all transactions.
     */
    public double getCurrentAmount() {
        double allTransactionsValue = 0;
        Cursor cursor = _readableDatabase.rawQuery("SELECT sum(" + Income.TABLE_INCOMES_COLUMN_AMOUNT + ") from " + Income.TABLE_INCOMES + ";", null);
        cursor.moveToFirst();
        double amount = cursor.getDouble(0) - allTransactionsValue;
        cursor.close();
        return amount;
    }

    public List<Income> cursorToList(Cursor cursor) {
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
