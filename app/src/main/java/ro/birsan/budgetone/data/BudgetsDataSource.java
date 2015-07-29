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
public class BudgetsDataSource extends DataSourceBase {

    public BudgetsDataSource(Context context) {
        super(context);
    }

    public List<Budget> cursorToList(Cursor cursor) {
        List<Budget> budget = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            budget.add(Budget.cursorToBudget(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return budget;
    }

    public Cursor getCurrentBudget() {
        Calendar c = Calendar.getInstance();
        return _readableDatabase.query(
                Budget.TABLE_BUDGETS,
                Budget.ALL_COLUMNS,
                Budget.TABLE_BUDGETS_COLUMN_MONTH + " == ? AND " + Budget.TABLE_BUDGETS_COLUMN_YEAR + " == ?",
                new String[]{String.valueOf(c.get(Calendar.MONTH)), String.valueOf(c.get(Calendar.YEAR))},
                null, null, null);
    }

    public Budget addBudgetForCurrentMonth(long categoryId, float amount) {
        ContentValues values = new ContentValues();
        Calendar c = Calendar.getInstance();
        values.put(Budget.TABLE_BUDGETS_COLUMN_CATEGORY_ID, categoryId);
        values.put(Budget.TABLE_BUDGETS_COLUMN_AMOUNT, amount);
        values.put(Budget.TABLE_BUDGETS_COLUMN_MONTH, c.get(Calendar.MONTH));
        values.put(Budget.TABLE_BUDGETS_COLUMN_YEAR, c.get(Calendar.YEAR));
        long insertId = _writableDatabase.insert(Budget.TABLE_BUDGETS, null, values);
        Cursor cursor = _writableDatabase.query(Budget.TABLE_BUDGETS, Budget.ALL_COLUMNS, Budget.TABLE_BUDGETS_COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Budget newBudget = Budget.cursorToBudget(cursor);
        cursor.close();
        return newBudget;
    }

    public void close() {
        _dbHelper.close();
    }
}
