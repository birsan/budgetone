package ro.birsan.budgetone.data;

import java.util.List;

/**
 * Created by Irinel on 7/21/2015.
 */
public class Category {
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
