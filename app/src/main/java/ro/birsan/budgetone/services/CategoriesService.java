package ro.birsan.budgetone.services;

import java.util.List;

import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.Category;

/**
 * Created by Irinel on 10/19/2015.
 */
public class CategoriesService {
    private CategoriesDataSource _categoriesDataSource;

    public CategoriesService(CategoriesDataSource categoriesDataSource) {
        _categoriesDataSource = categoriesDataSource;
    }

    public List<Category> getAllCategories() {
        return _categoriesDataSource.getCategories(Category.TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " IS NULL ", null);
    }

    public List<Category> getAllSubcategories(Long parentId) {
        return _categoriesDataSource.getCategories(Category.TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " = ? ", new String[]{String.valueOf(parentId)});
    }
}
