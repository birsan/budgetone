package ro.birsan.budgetone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.List;

import ro.birsan.budgetone.R;
import ro.birsan.budgetone.viewmodels.SelectCategoryViewModel;

/**
 * Created by ibirsan on 8/12/2015.
 */
public class CategoriesSelectionListAdapter extends ArrayAdapter<SelectCategoryViewModel> {

    public CategoriesSelectionListAdapter(Context context, int resource, List<SelectCategoryViewModel> objects) {
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

            item.setTag(getItem(position));
            item.setText(getItem(position).getName());
            item.setChecked(getItem(position).getSelected());
            item.setFocusable(false);
            item.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    item.toggle();
                    ((SelectCategoryViewModel)item.getTag()).setSelected(item.isChecked());
                }
            });

        }
        else {
            item = (CheckedTextView) convertView.getTag();
        }

        return convertView;
    }

}
