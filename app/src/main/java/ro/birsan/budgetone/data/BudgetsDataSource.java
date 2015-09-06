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

    public static List<Budget> cursorToList(Cursor cursor) {
        List<Budget> budget = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            budget.add(Budget.cursorToBudget(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return budget;
    }

    public ArrayList GetMonthSelectedCategories(int year, int month) {
        ArrayList<Long> selectedCategories = new ArrayList<>();
        List<Budget> budgetItems = cursorToList(getCurrentMonthBudget());
        for (Budget budgetItem : budgetItems) {
            selectedCategories.add(budgetItem.getCategoryId());
        }
        return selectedCategories;
    }

    public Cursor getCurrentMonthBudget() {
        Calendar c = Calendar.getInstance();
        return getMonthBudget(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
    }

    public Cursor getMonthBudget(int year, int month)
    {
        return _readableDatabase.query(
                Budget.TABLE_BUDGETS,
                Budget.ALL_COLUMNS,
                Budget.TABLE_BUDGETS_COLUMN_MONTH + " == ? AND " + Budget.TABLE_BUDGETS_COLUMN_YEAR + " == ?",
                new String[]{String.valueOf(month), String.valueOf(year)},
                null, null, null);
    }

    public double getCurrentMonthBudgetedAmount() {
        Calendar c = Calendar.getInstance();
        Cursor cursor = _readableDatabase.rawQuery("SELECT sum(" + Budget.TABLE_BUDGETS_COLUMN_AMOUNT + ") from " + Budget.TABLE_BUDGETS + " WHERE " + Budget.TABLE_BUDGETS_COLUMN_MONTH + " = " + c.get(Calendar.MONTH) + " AND " + Budget.TABLE_BUDGETS_COLUMN_YEAR + " = " + c.get(Calendar.YEAR) + ";", null);
        cursor.moveToFirst();
        double amount = cursor.getDouble(0);
        cursor.close();
        return amount;
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

    public void removeCurrentMonthBudgetCategory(long categoryId) {
        Calendar c = Calendar.getInstance();
        removeMonthBudgetCategory(c.get(Calendar.YEAR), c.get(Calendar.MONTH), categoryId);
    }

    public void removeMonthBudgetCategory(int year, int month, long categoryId) {
        _writableDatabase.delete(
                Budget.TABLE_BUDGETS,
                Budget.TABLE_BUDGETS_COLUMN_YEAR + " = ? AND " + Budget.TABLE_BUDGETS_COLUMN_MONTH + " = ? AND " + Budget.TABLE_BUDGETS_COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(categoryId)});
    }

    public void close() {
        _dbHelper.close();
    }

    public void updateBudget(long budgetId, Double amount) {
        ContentValues values = new ContentValues();
        values.put(Budget.TABLE_BUDGETS_COLUMN_AMOUNT, amount);
        _writableDatabase.update(
                Budget.TABLE_BUDGETS,
                values,
                Budget.TABLE_BUDGETS_COLUMN_ID + "= ? ",
                new String[]{String.valueOf(budgetId)});
    }
}
