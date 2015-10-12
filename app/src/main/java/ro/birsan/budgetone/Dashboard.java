package ro.birsan.budgetone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ro.birsan.budgetone.data.Budget;
import ro.birsan.budgetone.data.BudgetsDataSource;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.GoalsDataSource;
import ro.birsan.budgetone.data.IncomesDataSource;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.services.BalanceService;
import ro.birsan.budgetone.services.BudgetService;
import ro.birsan.budgetone.services.GoalsService;


public class Dashboard extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, CalculatorFragment.OnFragmentInteractionListener, GoalFragment.OnFragmentInteractionListener, GoalsFragment.OnFragmentInteractionListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int mPosition;

    private Fragment _currentFragment;

    IncomesDataSource _incomesDataSource;
    TransactionsDataSource _transactionsDataSource;
    BudgetsDataSource _budgetsDataSource;
    BalanceService _balanceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlaceholderFragment._context = this;
        setContentView(R.layout.activity_dashboard);

        _incomesDataSource = new IncomesDataSource(this);
        _transactionsDataSource = new TransactionsDataSource(this);
        _budgetsDataSource = new BudgetsDataSource(this);
        _balanceService = new BalanceService(_incomesDataSource, _transactionsDataSource);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mPosition = position;
        ShowRefreshFragment(position);
    }

    public void onSectionAttached(int number) {
        restoreActionBar();
    }

    public void restoreActionBar() {

        switch (mPosition) {
            case 0:
                DecimalFormat decimalFormat = new DecimalFormat();
                decimalFormat.setDecimalSeparatorAlwaysShown(false);
                mTitle = "To spend: " + decimalFormat.format(_balanceService.getAvailableAmount());
                break;
            case 1:
                mTitle = getString(R.string.title_section_history);
                break;
            case 2:
                mTitle = getString(R.string.title_section_categories);
                break;
            case 3:
                mTitle = getString(R.string.title_section_chart);
                break;
            default:
                return;
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

            switch (mPosition) {
                case 0:
                    getMenuInflater().inflate(R.menu.dashboard, menu);
                    break;
                case 4:
                    getMenuInflater().inflate(R.menu.goals, menu);
                    break;
            }

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.add_income) {
            startActivity(new Intent(this, AddIncomeActivity.class));
            return true;
        }

        if (id == R.id.add_target) {
            startActivity(new Intent(this, AddGoalActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Refresh() {
        ShowRefreshFragment(mPosition);
    }

    private void ShowRefreshFragment(int position) {
        _currentFragment = PlaceholderFragment.newInstance(position);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, _currentFragment)
                .commit();
    }

    @Override
    public void onTransactionAdded(long categoryId, Double amount) {
        TransactionsDataSource transactionsDataSource = new TransactionsDataSource(this);
        transactionsDataSource.addTransactionForCategory(categoryId, amount);
        transactionsDataSource.close();
        Refresh();
    }

    @Override
    public void onGoalAmountSubmitted(GoalFragment goalFragment, String goalId, Double amount) {
        GoalsService goalsService = new GoalsService(new GoalsDataSource(this), new TransactionsDataSource(this));
        Double newAmount = goalsService.addAmount(UUID.fromString(goalId), amount);
        goalFragment.setProgress(newAmount);
    }

    @Override
    public void onGoalRemoved(UUID goalId) {
        GoalsService goalsService = new GoalsService(new GoalsDataSource(this), new TransactionsDataSource(this));
        goalsService.removeGoal(goalId);
        Refresh();
    }

    @Override
    public void onGoalPageSelected(Integer position, Integer count) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.title_section_goals) + " " + position + "/" + count);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public static Context _context;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
            if (sectionNumber == 1) return new HistoryFragmentTabHost();
            if (sectionNumber == 2) return new CategoryFragment();
            if (sectionNumber == 3) return new ChartFragment();
            if (sectionNumber == 4) return new GoalsFragment();

            IncomesDataSource incomesDataSource = new IncomesDataSource(_context);
            TransactionsDataSource transactionsDataSource = new TransactionsDataSource(_context);
            BalanceService balanceService = new BalanceService(incomesDataSource, transactionsDataSource);
            BudgetService budgetService = new BudgetService(transactionsDataSource, new CategoriesDataSource(_context), new BudgetsDataSource(_context));
            if (balanceService.getAvailableAmount() <= 0) {
                return new NoMonthIncomeFragment();
            }

            List<Budget> budgetItems = budgetService.getMonthBudget(new Date());
            if (budgetItems.size() == 0) {
                return new NoBudgetFragment();
            }

            return new BudgetListFragment();
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Dashboard) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
