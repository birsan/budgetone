package ro.birsan.budgetone.services;

import java.util.List;

import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.Category;
import ro.birsan.budgetone.data.TransactionsDataSource;

/**
 * Created by ibirsan on 10/27/2015.
 */
public class TransactionsService {
    private TransactionsDataSource _transactionsDataSource;
    private CategoriesDataSource _categoriesDataSource;

    public TransactionsService(TransactionsDataSource transactionsDataSource, CategoriesDataSource categoriesDataSource) {
        _transactionsDataSource = transactionsDataSource;
        _categoriesDataSource = categoriesDataSource;
    }

    public Integer getTransactionsCountByCategory(Long categoryId)
    {
        List<Category> subcategories = _categoriesDataSource.getSubcategoriesOf(categoryId);
        Integer count = _transactionsDataSource.getTransactionsCountByCategory(categoryId);
        for(Category category : subcategories)
        {
            count += _transactionsDataSource.getTransactionsCountByCategory(category.getId());
        }
        return count;
    }
}
