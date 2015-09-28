package ro.birsan.budgetone.services;

import java.util.Calendar;
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
}
