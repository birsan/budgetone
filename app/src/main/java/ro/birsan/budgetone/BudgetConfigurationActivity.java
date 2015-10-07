package ro.birsan.budgetone;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.birsan.budgetone.adapters.BudgetConfigurationListAdapter;
import ro.birsan.budgetone.data.Budget;
import ro.birsan.budgetone.data.BudgetsDataSource;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.Category;
import ro.birsan.budgetone.data.IncomesDataSource;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.services.BalanceService;
import ro.birsan.budgetone.services.BudgetService;
import ro.birsan.budgetone.services.ExpensesService;
import ro.birsan.budgetone.viewmodels.BudgetConfigurationViewModel;


public class BudgetConfigurationActivity extends ActionBarActivity
implements BudgetConfigurationListAdapter.AdapterCallbacks {

    BudgetConfigurationListAdapter _adapter;
    private Double _availableAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_configuration);

        TransactionsDataSource transactionsDataSource = new TransactionsDataSource(this);
        CategoriesDataSource categoriesDataSource = new CategoriesDataSource(this);
        BudgetsDataSource budgetsDataSource = new BudgetsDataSource(this);
        IncomesDataSource incomesDataSource = new IncomesDataSource(this);

        BalanceService balanceService = new BalanceService(incomesDataSource, transactionsDataSource);
        ExpensesService expensesService = new ExpensesService(transactionsDataSource);
        BudgetService budgetService = new BudgetService(transactionsDataSource, categoriesDataSource, budgetsDataSource);
        ArrayList<BudgetConfigurationViewModel> objects = new ArrayList<>();
        List<Budget> budgets = budgetService.getMonthBudget(new Date());
        _availableAmount = balanceService.getAvailableAmount();
        Double monthIncome = balanceService.getCurrentMonthIncome();
        for(Budget budget: budgets)
        {
            Category category = categoriesDataSource.getCategory(budget.getCategoryId());
            Double monthAverage = expensesService.getAverageExpensesPerMonth(budget.getCategoryId());
            Double lastMonthBudget = budgetService.getLastMonthBudget(budget.getCategoryId());
            objects.add(new BudgetConfigurationViewModel(
                    budget.getCategoryId(),
                    category.getName(),
                    budget.getTotalAmount(),
                    (category.get_minPercentage() * monthIncome / 100),
                    (category.get_maxPercentage() * monthIncome / 100),
                    monthAverage > 0 ? monthAverage.toString() : "N/A",
                    lastMonthBudget > 0 ? lastMonthBudget.toString() : "N/A"));
        }
        _adapter = new BudgetConfigurationListAdapter(this, objects, _availableAmount, this);

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
                budgetsDataSource.updateBudget(model.get_categoryId(), new Date(), model.get_amount());
            }
            budgetsDataSource.close();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    _adapter.increaseBudget();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    _adapter.decreaseBudget();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onBudgetChaged(Double budgetedAmount) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.title_activity_budget_configuration) + " " + budgetedAmount.toString() + "/" + _availableAmount);
    }
}
