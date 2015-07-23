package ro.birsan.budgetone;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.MySQLiteHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends ListFragment {


    private SimpleCursorAdapter _adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        CategoriesDataSource categoriesDataSource = new CategoriesDataSource(getActivity());
        categoriesDataSource.open();
        String[] fromColumns = {MySQLiteHelper.TABLE_CATEGORIES_COLUMN_NAME};
        int[] toViews = {android.R.id.text1};
        _adapter = new SimpleCursorAdapter(
                getActivity(), android.R.layout.simple_list_item_1,
                categoriesDataSource.getCursor(MySQLiteHelper.TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " IS NULL ", null),
                fromColumns, toViews, 0);
        setListAdapter(_adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id){
        Cursor item = (Cursor)_adapter.getItem(position);
        Toast.makeText(getActivity(), item.getString(1), Toast.LENGTH_SHORT).show();
    }
}
