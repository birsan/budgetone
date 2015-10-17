package ro.birsan.budgetone;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.Category;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCategoriesList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCategoriesList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCategoriesList extends Fragment
implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final long TOP_LEVEL_PARENT_ID = -1;

    private static final String ARG_PARENT_CATEGORY_ID = "parent_id";
    private static final String ARG_ADD_DIALOG_TITLE = "dialog_title";

    private long _parentCategoryId;
    private String _addDialogTitle;

    private ListView _listView;
    private ArrayAdapter _listAdapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parentCategoryId Parent category id.
     * @return A new instance of fragment FragmentCategoriesList.
     */
    public static FragmentCategoriesList newInstance(long parentCategoryId, String addDialogTitle) {
        FragmentCategoriesList fragment = new FragmentCategoriesList();
        Bundle args = new Bundle();
        args.putLong(ARG_PARENT_CATEGORY_ID, parentCategoryId);
        args.putString(ARG_ADD_DIALOG_TITLE, addDialogTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentCategoriesList() {
        // Required empty public constructor
    }

    public void setListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    public void setParentCategoryId(long parentCategoryId) {
        Bundle args = getArguments();
        args.putLong(ARG_PARENT_CATEGORY_ID, parentCategoryId);
        _parentCategoryId = parentCategoryId;
        refreshData();
    }

    public void refreshData() {
        _listAdapter.clear();
        _listAdapter.addAll(getData(_parentCategoryId));
        _listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _parentCategoryId = getArguments().getLong(ARG_PARENT_CATEGORY_ID);
            _addDialogTitle = getArguments().getString(ARG_ADD_DIALOG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories_list, container, false);

        ImageButton btnAdd = (ImageButton)view.findViewById(R.id.add);
        btnAdd.setOnClickListener(this);
        _listAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getData(_parentCategoryId));
        _listView = (ListView) view.findViewById(R.id.listView);
        _listView.setAdapter(_listAdapter);
        _listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onCategorySelected(((Category) _listView.getItemAtPosition(position)).getId());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add)
        {
            final EditText txtInput = new EditText(getActivity());
            new AlertDialog.Builder(getActivity())
                    .setTitle(_addDialogTitle)
                    .setView(txtInput)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            onCategoryAdded(txtInput.getText().toString(), _parentCategoryId);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    public void onCategorySelected(Long categoryId) {
        if (mListener != null) {
            mListener.onCategorySelected(categoryId);
        }
    }

    public void onCategoryAdded(String categoryName, Long parentId) {
        if (mListener != null) {
            mListener.onCategoryAdded(categoryName, parentId);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private List<Category> getData(long parentId)
    {
        CategoriesDataSource categoriesDataSource = new CategoriesDataSource(getActivity());
        List<Category> categories;
        if (parentId == -1)
            categories = categoriesDataSource.getCategories(Category.TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " IS NULL ", null);
        else
            categories = categoriesDataSource.getCategories(Category.TABLE_CATEGORIES_COLUMN_PARENT_CATEGORY + " = ? ", new String[]{String.valueOf(parentId)});

        for (int i = 0; i < categories.size(); i++)
        {
            Category category = categories.get(i);
            List<Category> subcategories = categoriesDataSource.getSubcategoriesOf(category.getId());
            category.setSubcategories(subcategories);
        }

        return categories;
    }

    public interface OnFragmentInteractionListener {
        void onCategoryAdded(String categoryName, Long parentId);
        void onCategorySelected(Long categoryId);
    }

}
