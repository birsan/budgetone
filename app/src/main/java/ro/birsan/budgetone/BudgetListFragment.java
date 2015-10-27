package ro.birsan.budgetone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ro.birsan.budgetone.adapters.BudgetArrayAdapter;
import ro.birsan.budgetone.data.Budget;
import ro.birsan.budgetone.data.BudgetsDataSource;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.Category;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.services.BudgetService;
import ro.birsan.budgetone.services.TransactionsService;
import ro.birsan.budgetone.viewmodels.BudgetViewModel;

public class BudgetListFragment extends Fragment{

    private ListView _listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        ImageButton btnConfigureBudget = (ImageButton) view.findViewById(R.id.image_drag);
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
        final TransactionsDataSource  transactionsDataSource = new TransactionsDataSource(getActivity());

        BudgetService budgetService = new BudgetService(transactionsDataSource, categoriesDataSource, budgetsDataSource);
        final TransactionsService transactionsService = new TransactionsService(transactionsDataSource, categoriesDataSource);

        List<BudgetViewModel> viewModels = new ArrayList<>();
        List<Budget> budgets = budgetService.getMonthBudget(new Date());
        Collections.sort(budgets, new Comparator<Budget>() {
            @Override
            public int compare(Budget lhs, Budget rhs) {
                Integer lAmount = transactionsService.getTransactionsCountByCategory(lhs.getCategoryId());
                Integer rAmount = transactionsService.getTransactionsCountByCategory(rhs.getCategoryId());
                return rAmount.compareTo(lAmount);
            }
        });
        for(Budget budget: budgets)
        {
            double budgetExpenses = budgetService.getTotalExpensesForCurrentMonth(budget.getCategoryId());
            String categoryName = categoriesDataSource.getCategory(budget.getCategoryId()).getName();
            viewModels.add(new BudgetViewModel(budget.getCategoryId(), categoryName, budget.getTotalAmount(), budgetExpenses));
        }

        _listView = (ListView)getActivity().findViewById(R.id.list);
        ArrayAdapter<BudgetViewModel> _adapter = new BudgetArrayAdapter(getActivity(), viewModels);
        _listView.setAdapter(_adapter);
        registerForContextMenu(_listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        BudgetViewModel budgetViewModel = (BudgetViewModel) _listView.getItemAtPosition(info.position);

        CategoriesDataSource categoriesDataSource = new CategoriesDataSource(getActivity());
        List<Category> subcategories = categoriesDataSource.getSubcategoriesOf(budgetViewModel.get_categoryId());

        menu.clear();
        menu.setHeaderTitle("Add transaction to ...");
        menu.add(0, budgetViewModel.get_categoryId().intValue(), 0, budgetViewModel.get_categoryName() + " (General)");
        for (Category subcategory : subcategories)
        {
            menu.add(0, subcategory.getId().intValue(), 0, subcategory.getName());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        CalculatorFragment calculatorFragment = new CalculatorFragment();
        Bundle args = new Bundle();
        args.putLong("category_id", item.getItemId());
        calculatorFragment.setArguments(args);
        calculatorFragment.show(getFragmentManager(), "addTransactionForCategory");
        return true;
    }
}
