package ro.birsan.budgetone;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ro.birsan.budgetone.adapters.CategoriesSelectionListAdapter;
import ro.birsan.budgetone.data.BudgetsDataSource;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.Category;
import ro.birsan.budgetone.viewmodels.SelectCategoryViewModel;


public class SelectCategoriesActivity extends ActionBarActivity {

    CategoriesSelectionListAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_categories);

        Calendar c = Calendar.getInstance();
        CategoriesDataSource categoriesDataSource = new CategoriesDataSource(this);
        BudgetsDataSource bds = new BudgetsDataSource(this);
        ArrayList<SelectCategoryViewModel> models = new ArrayList<>();
        List<Category> categories = categoriesDataSource.getCategories(Category.TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " IS NULL ", null);
        ArrayList selectedCategories = bds.GetMonthSelectedCategories(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
        for (Category category : categories) {
            models.add(new SelectCategoryViewModel(category.getId(), category.getName(), selectedCategories.contains(category.getId())));
        }

        _adapter = new CategoriesSelectionListAdapter(this, android.R.layout.activity_list_item, models);

        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.title_activity_select_categories);
        actionBar.setHomeAsUpIndicator(R.drawable.check);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            BudgetsDataSource bds = new BudgetsDataSource(this);
            for (int i = 0; i < _adapter.getCount(); i++) {
                SelectCategoryViewModel model = _adapter.getItem(i);
                if (model.getSelected() && !model.getOriginalSelected()) {
                    bds.addBudgetForCurrentMonth(model.getId(), 0);
                }

                if (!model.getSelected() && model.getOriginalSelected()) {
                    bds.removeCurrentMonthBudgetCategory(model.getId());
                }
            }
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
