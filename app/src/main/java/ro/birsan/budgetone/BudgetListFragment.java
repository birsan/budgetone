package ro.birsan.budgetone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import ro.birsan.budgetone.data.Budget;
import ro.birsan.budgetone.data.BudgetsDataSource;

public class BudgetListFragment extends Fragment {

    private SimpleCursorAdapter _adapter;
    private ListView _listView;
    ImageButton _fabImageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        _fabImageButton = (ImageButton) view.findViewById(R.id.fab_image_button);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BudgetsDataSource budgetsDataSource = new BudgetsDataSource(getActivity());

        String[] fromColumns = {Budget.TABLE_BUDGETS_COLUMN_AMOUNT};
        int[] toViews = {R.id.category_name};

        _listView = (ListView)getActivity().findViewById(R.id.list);
        _adapter = new SimpleCursorAdapter(getActivity(), R.layout.budget_list_item, budgetsDataSource.getCurrentMonthBudget(), fromColumns, toViews, 0);
        _listView.setAdapter(_adapter);

        _fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddBudget dialogAddBudget = new DialogAddBudget();
                dialogAddBudget.show(getActivity().getFragmentManager(), "add_budget");
            }
        });
    }
}
