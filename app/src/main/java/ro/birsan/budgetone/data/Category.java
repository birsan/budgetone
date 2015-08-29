package ro.birsan.budgetone.data;

import java.util.List;

/**
 * Created by Irinel on 7/21/2015.
 */
public class Category {
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_CATEGORIES_COLUMN_ID = "_id";
    public static final String TABLE_CATEGORIES_COLUMN_NAME = "name";
    public static final String TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY = "parent";
    public static final String TABLE_CATEGORIES_COLUMN_MIN_PERCENTAGE = "min_percentage";
    public static final String TABLE_CATEGORIES_COLUMN_MAX_PERCENTAGE = "max_percentage";
    public static final String[] ALL_COLUMNS = {
            TABLE_CATEGORIES_COLUMN_ID,
            TABLE_CATEGORIES_COLUMN_NAME,
            TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY,
            TABLE_CATEGORIES_COLUMN_MIN_PERCENTAGE,
            TABLE_CATEGORIES_COLUMN_MAX_PERCENTAGE
    };

    public static final String TABLE_CATEGORIES_CREATE = "create table "
            + TABLE_CATEGORIES + "(" + TABLE_CATEGORIES_COLUMN_ID
            + " integer primary key autoincrement, "
            + TABLE_CATEGORIES_COLUMN_NAME + " text not null, "
            + TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " integer null, "
            + TABLE_CATEGORIES_COLUMN_MIN_PERCENTAGE + " integer null, "
            + TABLE_CATEGORIES_COLUMN_MAX_PERCENTAGE + " integer null "
            + ");";

    private long id;
    private String name;
    private List<Category> _subcategories;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getSubcategories() {
        return _subcategories;
    }

    public void setSubcategories(List<Category> subcategories) {
        _subcategories = subcategories;
    }

    @Override
    public String toString() {
        return name;
    }
}
