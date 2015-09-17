package ro.birsan.budgetone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ro.birsan.budgetone.adapters.ChartListAdapter;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.Category;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.viewmodels.ChartViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {


    public ChartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        ListView listView = (ListView)view.findViewById(R.id.listView);

        CategoriesDataSource categoriesDataSource = new CategoriesDataSource(getActivity());
        TransactionsDataSource transactionsDataSource = new TransactionsDataSource(getActivity());

        int maxProgress = 0;
        HashMap<Long, ChartViewModel> expensesPerMajorCategory = new HashMap<>();
        List<Category> categories = categoriesDataSource.getCategories(null, null);
        for(Category category : categories) {
            Double amount = transactionsDataSource.getExpensesAmountForCurrentMonth(category.getId());
            Long majorCategoryId = category.get_parentId() != 0 ? category.get_parentId() : category.getId();

            ChartViewModel newModel = null;
            if (expensesPerMajorCategory.containsKey(majorCategoryId)) {
                ChartViewModel existingModel = expensesPerMajorCategory.get(majorCategoryId);
                newModel = new ChartViewModel(existingModel.get_categoryName(), existingModel.get_amount() + amount.intValue());
            } else if (amount > 0) {
                Category majorCategory = categoriesDataSource.getCategory(majorCategoryId);
                newModel = new ChartViewModel(majorCategory.getName(), amount.intValue());
            }

            if (newModel != null) {
                expensesPerMajorCategory.put(majorCategoryId, newModel);
                maxProgress = Math.max(maxProgress, newModel.get_amount());
            }
        }

        ChartViewModel[] viewModels = expensesPerMajorCategory.values().toArray(new ChartViewModel[expensesPerMajorCategory.values().size()]);
        Arrays.sort(viewModels, new Comparator<ChartViewModel>() {
            @Override
            public int compare(ChartViewModel viewModel1, ChartViewModel viewModel2) {
                if (viewModel1.get_amount() < viewModel2.get_amount()) return 1;
                else if (viewModel1.get_amount() > viewModel2.get_amount()) return -1;

                return 0;
            }
        });

        ChartListAdapter adapter = new ChartListAdapter(getActivity(), viewModels, maxProgress);
        listView.setAdapter(adapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);

        return view;
    }
}
