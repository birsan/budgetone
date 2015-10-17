package ro.birsan.budgetone;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.widgets.NonSwipeableViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    NonSwipeableViewPager _viewPager;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        _viewPager = (NonSwipeableViewPager) view.findViewById(R.id.pager);
        final FragmentCategoriesList fragmentCategoriesList = FragmentCategoriesList.newInstance(FragmentCategoriesList.TOP_LEVEL_PARENT_ID, "Add Category");
        final FragmentCategoriesList fragmentSubcategoriesList = FragmentCategoriesList.newInstance(FragmentCategoriesList.TOP_LEVEL_PARENT_ID, "Add Subcategory");
        final FragmentStatePagerAdapter tabsAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return position == 0 ? fragmentCategoriesList : fragmentSubcategoriesList;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        FragmentCategoriesList.OnFragmentInteractionListener listener = new FragmentCategoriesList.OnFragmentInteractionListener() {
            @Override
            public void onCategoryAdded(String categoryName, Long parentId) {
                CategoriesDataSource categoriesDataSource = new CategoriesDataSource(getActivity());
                if (parentId == FragmentCategoriesList.TOP_LEVEL_PARENT_ID) {
                    categoriesDataSource.createCategory(categoryName);
                    fragmentCategoriesList.refreshData();
                } else {
                    categoriesDataSource.addSubcategory(categoryName, parentId);
                    fragmentSubcategoriesList.refreshData();
                }
            }

            @Override
            public void onCategorySelected(Long categoryId) {
                if (_viewPager.getCurrentItem() == 1) return;

                fragmentSubcategoriesList.setParentCategoryId(categoryId);
                _viewPager.setCurrentItem(1, true);
            }
        };

        fragmentCategoriesList.setListener(listener);
        fragmentSubcategoriesList.setListener(listener);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (_viewPager.getCurrentItem() == 0) return false;

                    _viewPager.setCurrentItem(0, true);
                    return true;
                }
                return false;
            }
        });

        _viewPager.setAdapter(tabsAdapter);
        return view;
    }
}
