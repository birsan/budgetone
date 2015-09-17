package ro.birsan.budgetone.services;

import java.util.List;

import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.Category;
import ro.birsan.budgetone.data.TransactionsDataSource;

/**
 * Created by Irinel on 9/17/2015.
 */
public class BudgetService {
    TransactionsDataSource _transactionsDataSource;
    CategoriesDataSource _categoriesDataSource;

    public BudgetService(TransactionsDataSource transactionsDataSource, CategoriesDataSource categoriesDataSource){
        _transactionsDataSource = transactionsDataSource;
        _categoriesDataSource = categoriesDataSource;
    }

    public Double getTotalExpensesForCurrentMonth(Long categoryId) {
        List<Category> subcategories = _categoriesDataSource.getSubcategoriesOf(categoryId);
        double budgetExpenses = _transactionsDataSource.getExpensesAmountForCurrentMonth(categoryId);
        for (Category subcategory : subcategories) {
            budgetExpenses += _transactionsDataSource.getExpensesAmountForCurrentMonth(subcategory.getId());
        }
        return budgetExpenses;
    }
}
