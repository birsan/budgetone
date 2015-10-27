package ro.birsan.budgetone;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import ro.birsan.budgetone.adapters.HistoryAdapter;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.Category;
import ro.birsan.budgetone.data.GoalsDataSource;
import ro.birsan.budgetone.data.Income;
import ro.birsan.budgetone.data.IncomesDataSource;
import ro.birsan.budgetone.data.Transaction;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.services.CategoriesService;
import ro.birsan.budgetone.services.ExpensesService;
import ro.birsan.budgetone.services.GoalExt;
import ro.birsan.budgetone.services.GoalsService;
import ro.birsan.budgetone.viewmodels.HistoryViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment
implements AdapterView.OnItemLongClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private Spinner _spinnerYear;
    private Spinner _spinnerMonth;
    private Spinner _spinnerCategory;
    private RadioButton _rbExpenses;
    private View _search_panel;
    private View _expenses_panel;

    private HistoryAdapter _historyAdapter;
    private ITransactionHistoryService _transactionHistoryService;
    private List<String> _categoryIds = new ArrayList<>();
    private int _goalsStartingPosition;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        View view =  inflater.inflate(R.layout.fragment_history, container, false);

        _expenses_panel = view.findViewById(R.id.expenses_panel);
        _search_panel = view.findViewById(R.id.search_panel);
        _search_panel.setVisibility(View.GONE);

        _spinnerYear = (Spinner)view.findViewById(R.id.spinner_year);
        ArrayAdapter<String> yearSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getYearOptions());
        _spinnerYear.setAdapter(yearSpinnerAdapter);
        _spinnerYear.setOnItemSelectedListener(this);

        _spinnerMonth = (Spinner)view.findViewById(R.id.spinner_month);
        ArrayAdapter<String> monthSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getMonthOptions());
        _spinnerMonth.setAdapter(monthSpinnerAdapter);
        _spinnerMonth.setSelection(calendar.get(Calendar.MONTH));
        _spinnerMonth.setOnItemSelectedListener(this);

        _spinnerCategory = (Spinner)view.findViewById(R.id.spinner_category);
        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getCategoryOptions());
        _spinnerCategory.setAdapter(categorySpinnerAdapter);
        _spinnerCategory.setOnItemSelectedListener(this);

        _historyAdapter = new HistoryAdapter(getActivity(), new ArrayList<HistoryViewModel>());
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(_historyAdapter);
        listView.setOnItemLongClickListener(this);

        _rbExpenses = (RadioButton) view.findViewById(R.id.rbExpenses);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        updateListView();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            _search_panel.setVisibility(_search_panel.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Removing transaction")
                .setMessage("Are you sure you want to remove this transaction?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HistoryViewModel viewModel = _historyAdapter.getItem(position);
                        Long transactionId = viewModel.get_transactionId();
                        _transactionHistoryService.remove(transactionId);
                        _historyAdapter.remove(viewModel);
                        _historyAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", null)
                .show();

        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        _expenses_panel.setVisibility(R.id.rbExpenses == checkedId ? View.VISIBLE : View.GONE);
        updateListView();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateListView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateListView() {
        int year = Integer.valueOf(_spinnerYear.getSelectedItem().toString());
        int month = Integer.valueOf(_spinnerMonth.getSelectedItem().toString());

        if (_rbExpenses.isChecked()) {
            TransactionsDataSource transactionsDataSource = new TransactionsDataSource(getActivity());
            GoalsDataSource goalsDataSource = new GoalsDataSource(getActivity());
            CategoriesDataSource categoriesDataSource = new CategoriesDataSource(getActivity());
            ExpensesService expensesService = new ExpensesService(transactionsDataSource);
            GoalsService goalsService = new GoalsService(goalsDataSource, transactionsDataSource);
            _transactionHistoryService = new ExpensesHistoryService(expensesService, goalsService, categoriesDataSource);
        }
        else {
            _transactionHistoryService = new IncomeHistoryService(new IncomesDataSource(getActivity()));
        }

        int selectedItemPosition = _spinnerCategory.getSelectedItemPosition();
        Long selectedCategoryId = getSelectedCategoryId(selectedItemPosition);
        UUID selectedGoal = getSelectedGoalId(selectedItemPosition);

        _historyAdapter.clear();
        _historyAdapter.addAll(_transactionHistoryService.getAll(year, month, selectedCategoryId, selectedGoal));
        _historyAdapter.notifyDataSetChanged();
    }

    private UUID getSelectedGoalId(int selectedItemPosition) {
        if (selectedItemPosition >= _goalsStartingPosition)
            return UUID.fromString(_categoryIds.get(selectedItemPosition));

        return null;
    }

    private Long getSelectedCategoryId(int selectedItemPosition) {
        if (selectedItemPosition < _goalsStartingPosition && selectedItemPosition > 0)
            return Long.valueOf(_categoryIds.get(selectedItemPosition));

        return null;
    }

    private  ArrayList<String> getCategoryOptions() {
        CategoriesService categoriesService = new CategoriesService(new CategoriesDataSource(getActivity()));
        List<Category> categories = categoriesService.getAllCategories();
        ArrayList<String> spinnerData = new ArrayList<>();
        _categoryIds.add("");
        spinnerData.add("All");
        for (Category category : categories) {
            spinnerData.add(category.getName());
            _categoryIds.add(category.getId().toString());
        }

        _goalsStartingPosition = spinnerData.size();

        GoalsService goalsService = new GoalsService(new GoalsDataSource(getActivity()), new TransactionsDataSource(getActivity()));
        List<GoalExt> goals = goalsService.getInProgressGoals();
        for (GoalExt goal : goals) {
            spinnerData.add(goal.get_name());
            _categoryIds.add(goal.get_id().toString());
        }

        return spinnerData;
    }

    private  ArrayList<String> getSubcategoryOptions() {
        ArrayList<String> options = new ArrayList<>();
        return options;
    }

    private  ArrayList<String> getYearOptions() {
        ArrayList<String> options = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for (int i = 0; i < 20; i++) {
            options.add(String.valueOf(calendar.get(Calendar.YEAR) - i));
        }
        return options;
    }

    private  ArrayList<String> getMonthOptions() {
        ArrayList<String> options = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            options.add(String.valueOf(i));
        }
        return options;
    }

    private interface ITransactionHistoryService {
        void remove(Long transactionId);
        List<HistoryViewModel> getAll(int year, int month, Long categoryId, UUID goalId);
    }

    private class IncomeHistoryService implements ITransactionHistoryService
    {
        private IncomesDataSource _incomesDataSource;

        private IncomeHistoryService(IncomesDataSource incomesDataSource) {
            _incomesDataSource = incomesDataSource;
        }

        @Override
        public void remove(Long transactionId) {
            _incomesDataSource.remove(transactionId);
        }

        @Override
        public List<HistoryViewModel> getAll(int year, int month, Long categoryId, UUID goalId) {
            List<HistoryViewModel> viewModels = new ArrayList<>();
            List<Income> transactions = _incomesDataSource.getIncome(month, year);
            for (Income transaction : transactions) {
                viewModels.add(new HistoryViewModel(transaction.get_id(), transaction.get_category(), transaction.get_createdOn(), transaction.get_amount()));
            }
            return viewModels;
        }
    }

    private class ExpensesHistoryService implements ITransactionHistoryService{
        private ExpensesService _expensesService;
        private GoalsService _goalsService;
        private CategoriesDataSource _categoriesDataSource;

        public ExpensesHistoryService(
                ExpensesService expensesService,
                GoalsService goalsService,
                CategoriesDataSource categoriesDataSource) {
            _expensesService = expensesService;
            _goalsService = goalsService;
            _categoriesDataSource = categoriesDataSource;
        }

        @Override
        public void remove(Long transactionId) {
            _expensesService.remove(transactionId);
        }

        @Override
        public List<HistoryViewModel> getAll(int year, int month, Long categoryId, UUID goalId) {
            List<HistoryViewModel> viewModels = new ArrayList<>();
            List<Transaction> allTransactions = _expensesService.getTransactions(new GregorianCalendar(year, month, 1).getTime(), categoryId, goalId);
            for (Transaction transaction : allTransactions) {
                String title = "";
                if (transaction.get_categoryId() != null)
                {
                    title = _categoriesDataSource.getCategory(transaction.get_categoryId()).getName();
                }
                else if (transaction.get_goalId() != null)
                {
                    title = _goalsService.getGoal(transaction.get_goalId()).get_name();
                }

                viewModels.add(new HistoryViewModel(transaction.get_id(), title, transaction.get_createdOn(), transaction.get_amount()));
            }
            return viewModels;
        }
    }
}
