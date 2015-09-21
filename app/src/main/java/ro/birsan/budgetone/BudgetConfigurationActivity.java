package ro.birsan.budgetone;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ro.birsan.budgetone.adapters.BudgetConfigurationListAdapter;
import ro.birsan.budgetone.data.Budget;
import ro.birsan.budgetone.data.BudgetsDataSource;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.IncomesDataSource;
import ro.birsan.budgetone.viewmodels.BudgetConfigurationViewModel;


public class BudgetConfigurationActivity extends ActionBarActivity {

    BudgetConfigurationListAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_configuration);

        ArrayList<BudgetConfigurationViewModel> objects = new ArrayList<>();
        BudgetsDataSource budgetsDataSource = new BudgetsDataSource(this);
        CategoriesDataSource categoriesDataSource = new CategoriesDataSource(this);
        IncomesDataSource incomesDataSource = new IncomesDataSource(this);
        List<Budget> budgets = BudgetsDataSource.cursorToList(budgetsDataSource.getCurrentMonthBudget());
        for(Budget budget: budgets)
        {
            objects.add(new BudgetConfigurationViewModel(budget.getId(), budget.getCategoryId(), categoriesDataSource.getCategory(budget.getCategoryId()).getName(), budget.getTotalAmount()));
        }
        _adapter = new BudgetConfigurationListAdapter(this, objects, incomesDataSource.getCurrentAmount());

        final ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(_adapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_activity_budget_configuration);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.check);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            BudgetsDataSource budgetsDataSource = new BudgetsDataSource(this);

            for (int i = 0; i < _adapter.getCount(); i++) {
                BudgetConfigurationViewModel model = _adapter.getItem(i);
                budgetsDataSource.updateBudget(model.get_budgetId(), model.get_amount());
            }
            budgetsDataSource.close();
        }

        return super.onOptionsItemSelected(item);
    }
}