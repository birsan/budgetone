package ro.birsan.budgetone;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.List;

import ro.birsan.budgetone.adapters.ExpandableCategoriesListAdapter;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.Category;
import ro.birsan.budgetone.data.MySQLiteHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {


    private ExpandableCategoriesListAdapter _adapter;
    private ExpandableListView expListView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        expListView = (ExpandableListView)getActivity().findViewById(R.id.categories_expandable_list);
        _adapter = new ExpandableCategoriesListAdapter(getActivity(), getData());
        expListView.setAdapter(_adapter);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cateories_list, container, false);
    }

    private List<Category> getData()
    {
        CategoriesDataSource categoriesDataSource = new CategoriesDataSource(getActivity());
        List<Category> categories = categoriesDataSource.getCategories(Category.TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " IS NULL ", null);
        for (int i = 0; i < categories.size(); i++)
        {
            Category category = categories.get(i);
            List<Category> subcategories = categoriesDataSource.getSubcategoriesOf(category.getId());
            category.setSubcategories(subcategories);
        }

        return categories;
    }
}
