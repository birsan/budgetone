package ro.birsan.budgetone.data;

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
