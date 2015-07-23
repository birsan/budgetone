package ro.birsan.budgetone;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class BudgetListFragment extends ListFragment {

    private SimpleCursorAdapter _adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {R.id.category_name};

        Uri phone_contacts = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[] {ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME };
        Cursor cursor = getActivity().getContentResolver().query(phone_contacts, projection, null, null, ContactsContract.Data.DISPLAY_NAME + " ASC");

        _adapter = new SimpleCursorAdapter(getActivity(), R.layout.budget_list_item, cursor, fromColumns, toViews, 0);
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
