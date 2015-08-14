package ro.birsan.budgetone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Toast;

import java.util.List;

import ro.birsan.budgetone.R;
import ro.birsan.budgetone.data.Category;

/**
 * Created by ibirsan on 8/12/2015.
 */
public class CategoriesSelectionListAdapter extends ArrayAdapter<Category> {

    public CategoriesSelectionListAdapter(Context context, int resource, List<Category> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CheckedTextView item;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.checkbox_category_item, null);
            item = (CheckedTextView) convertView.findViewById(R.id.text1);
            convertView.setTag(item);

            item.setText(getItem(position).getName());
            item.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    item.toggle();
                }
            });

        }
        else {
            item = (CheckedTextView) convertView.getTag();
        }

        return convertView;
    }

}
