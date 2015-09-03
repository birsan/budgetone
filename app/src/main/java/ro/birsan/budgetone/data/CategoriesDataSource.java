package ro.birsan.budgetone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irinel on 7/22/2015.
 */
public class CategoriesDataSource {
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;
    private MySQLiteHelper dbHelper;

    public CategoriesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        writableDatabase = dbHelper.getWritableDatabase();
        readableDatabase = dbHelper.getReadableDatabase();
    }

    public Category createCategory(String categoryName) {
        ContentValues values = new ContentValues();
        values.put(Category.TABLE_CATEGORIES_COLUMN_NAME, categoryName);
        long insertId = writableDatabase.insert(Category.TABLE_CATEGORIES, null, values);
        Cursor cursor = writableDatabase.query(Category.TABLE_CATEGORIES, Category.ALL_COLUMNS, Category.TABLE_CATEGORIES_COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Category newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    public Cursor getCursor(String selection, String[] selectionArgs) {
        return readableDatabase.query(Category.TABLE_CATEGORIES, Category.ALL_COLUMNS, selection, selectionArgs, null, null, null);
    }

    public List<Category> getCategories(String selection, String[] selectionArgs) {
        List<Category> comments = new ArrayList<>();
        Cursor cursor = getCursor(selection, selectionArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            comments.add(cursorToComment(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return comments;
    }

    public Category getCategory(long categoryId)
    {
        Cursor cursor = getCursor(Category.TABLE_CATEGORIES_COLUMN_ID + " = ?", new String[]{String.valueOf(categoryId)});
        cursor.moveToFirst();
        if (cursor.isAfterLast()) return null;

        return cursorToComment(cursor);
    }

    public void close() {
        dbHelper.close();
    }

    private Category cursorToComment(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getLong(0));
        category.setName(cursor.getString(1));
        return category;
    }
}
