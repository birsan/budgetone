package ro.birsan.budgetone;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import ro.birsan.budgetone.adapters.CategoriesSelectionListAdapter;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.MySQLiteHelper;


public class SelectCategoriesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_categories);

        CategoriesDataSource categoriesDataSource = new CategoriesDataSource(this);
        CategoriesSelectionListAdapter adapter = new CategoriesSelectionListAdapter(this, android.R.layout.activity_list_item, categoriesDataSource.getCategories(MySQLiteHelper.TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " IS NULL ", null));

        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.title_activity_select_categories);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
