package ro.birsan.budgetone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ro.birsan.budgetone.adapters.BudgetArrayAdapter;
import ro.birsan.budgetone.data.Budget;
import ro.birsan.budgetone.data.BudgetsDataSource;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.viewmodels.BudgetViewModel;

public class BudgetListFragment extends Fragment{

    private ArrayAdapter<BudgetViewModel> _adapter;
    private ListView _listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        ImageButton btnConfigureBudget = (ImageButton) view.findViewById(R.id.image_money_transfer);
        ImageButton btnSelectCategories = (ImageButton) view.findViewById(R.id.image_bullets);

        btnConfigureBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BudgetConfigurationActivity.class);
                startActivity(intent);
            }
        });

        btnSelectCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectCategoriesActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CategoriesDataSource categoriesDataSource = new CategoriesDataSource(getActivity());
        BudgetsDataSource budgetsDataSource = new BudgetsDataSource(getActivity());
        TransactionsDataSource  transactionsDataSource = new TransactionsDataSource(getActivity());

        List<BudgetViewModel> viewModels = new ArrayList<>();
        List<Budget> budgets = budgetsDataSource.cursorToList(budgetsDataSource.getCurrentMonthBudget());
        for(Budget budget: budgets)
        {
            double budgetExpenses = transactionsDataSource.getExpensesAmountForCurrentMonth(budget.getCategoryId());
            String categoryName = categoriesDataSource.getCategory(budget.getCategoryId()).getName();
            viewModels.add(new BudgetViewModel(budget.getCategoryId(), categoryName, budget.getTotalAmount(), budgetExpenses));
        }

        _listView = (ListView)getActivity().findViewById(R.id.list);
        _adapter = new BudgetArrayAdapter(getActivity(), viewModels);
        _listView.setAdapter(_adapter);
        registerForContextMenu(_listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.budget_contextual_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_add_transaction:
                CalculatorFragment calculatorFragment = new CalculatorFragment();
                Bundle args = new Bundle();
                args.putLong("category_id", _adapter.getItem(info.position).get_categoryId());
                calculatorFragment.setArguments(args);
                calculatorFragment.show(getFragmentManager(), "addTransaction");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
