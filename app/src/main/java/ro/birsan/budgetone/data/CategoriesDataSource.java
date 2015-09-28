package ro.birsan.budgetone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irinel on 7/22/2015.
 */
public class CategoriesDataSource extends DataSourceBase {

    public CategoriesDataSource(Context context) {
        super(context);
    }

    public Category createCategory(String categoryName) {
        ContentValues values = new ContentValues();
        values.put(Category.TABLE_CATEGORIES_COLUMN_NAME, categoryName);
        long insertId = _writableDatabase.insert(Category.TABLE_CATEGORIES, null, values);
        Cursor cursor = _writableDatabase.query(Category.TABLE_CATEGORIES, Category.ALL_COLUMNS, Category.TABLE_CATEGORIES_COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Category newComment = cursorToCategory(cursor);
        cursor.close();
        return newComment;
    }

    public Cursor getCursor(String selection, String[] selectionArgs) {
        return _readableDatabase.query(Category.TABLE_CATEGORIES, Category.ALL_COLUMNS, selection, selectionArgs, null, null, null);
    }

    public List<Category> getCategories(String selection, String[] selectionArgs) {
        List<Category> categories = new ArrayList<>();
        Cursor cursor = getCursor(selection, selectionArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            categories.add(cursorToCategory(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return categories;
    }

    public List<Category> getSubcategoriesOf(Long categoryId)
    {
        return getCategories(Category.TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " == " + categoryId, null);
    }

    public Category getCategory(long categoryId)
    {
        Cursor cursor = getCursor(Category.TABLE_CATEGORIES_COLUMN_ID + " = ?", new String[]{String.valueOf(categoryId)});
        cursor.moveToFirst();
        if (cursor.isAfterLast()) return null;

        return cursorToCategory(cursor);
    }

    private Category cursorToCategory(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getLong(0));
        category.setName(cursor.getString(1));
        category.set_parentId(cursor.getLong(2));
        category.set_minPercentage(cursor.getInt(3));
        category.set_maxPercentage(cursor.getInt(4));
        return category;
    }
}
