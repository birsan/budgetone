package ro.birsan.budgetone.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ro.birsan.budgetone.data.Budget;
import ro.birsan.budgetone.data.BudgetsDataSource;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.Category;
import ro.birsan.budgetone.data.TransactionsDataSource;

/**
 * Created by Irinel on 9/17/2015.
 */
public class BudgetService {
    TransactionsDataSource _transactionsDataSource;
    CategoriesDataSource _categoriesDataSource;
    BudgetsDataSource _budgetsDataSource;

    public BudgetService(
            TransactionsDataSource transactionsDataSource,
            CategoriesDataSource categoriesDataSource,
            BudgetsDataSource budgetsDataSource){
        _transactionsDataSource = transactionsDataSource;
        _categoriesDataSource = categoriesDataSource;
        _budgetsDataSource = budgetsDataSource;
    }

    public Double getTotalExpensesForCurrentMonth(Long categoryId) {
        List<Category> subcategories = _categoriesDataSource.getSubcategoriesOf(categoryId);
        double budgetExpenses = _transactionsDataSource.getExpensesAmountForCurrentMonth(categoryId);
        for (Category subcategory : subcategories) {
            budgetExpenses += _transactionsDataSource.getExpensesAmountForCurrentMonth(subcategory.getId());
        }
        return budgetExpenses;
    }

    public Double getLastMonthBudget(Long categoryId)
    {
        Calendar c = Calendar.getInstance();
        Budget budget = _budgetsDataSource.getMonthBudget(categoryId, c.get(Calendar.YEAR), c.get(Calendar.MONTH) - 1);
        return budget != null ? budget.get_amount() : 0.0;
    }

    public Double getMonthBudgetedAmount(Date date)
    {
        Double budgetedAmount = .0;
        for (Budget budget : getMonthBudget(date))
        {
            budgetedAmount += budget.getTotalAmount();
        }
        return budgetedAmount;
    }

    public List<Budget> getMonthBudget(Date date)
    {
        List<Budget> currentMonthBudget = _budgetsDataSource.getCurrentMonthBudget();
        if (currentMonthBudget.size() > 0) return currentMonthBudget;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        List<Budget> lastMonthBudget = _budgetsDataSource.getMonthBudget(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        if (lastMonthBudget.size() > 0) return setMonthBudget(date, lastMonthBudget);

        List<Budget> defaultBudget = new ArrayList<>();
        cal.setTime(date);
        List<Category> allCategories = _categoriesDataSource.getCategories(Category.TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " IS NULL ", null);
        for (Category category: allCategories)
        {
            Budget budget = new Budget();
            budget.set_amount(.0);
            budget.set_year(cal.get(Calendar.YEAR));
            budget.set_month(cal.get(Calendar.MONTH));
            budget.setCategoryId(category.getId());
            defaultBudget.add(budget);
        }
        return setMonthBudget(date, defaultBudget);
    }

    private List<Budget> setMonthBudget(Date date, List<Budget> budgets)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        for(Budget budget : budgets)
        {
            _budgetsDataSource.addBudgetForCurrentMonth(budget.getCategoryId(), 0.0f);
        }
        return _budgetsDataSource.getMonthBudget(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
    }
}
