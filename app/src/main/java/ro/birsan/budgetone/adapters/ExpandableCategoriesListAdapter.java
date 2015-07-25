package ro.birsan.budgetone.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import ro.birsan.budgetone.R;
import ro.birsan.budgetone.data.Category;

/**
 * Created by Irinel on 7/24/2015.
 */
public class ExpandableCategoriesListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Category> _categories;

    public ExpandableCategoriesListAdapter(Context context, List<Category> categories){
        _context = context;
        _categories = categories;
    }

    @Override
    public int getGroupCount() {
        return _categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((Category)getGroup(groupPosition)).getSubcategories().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ((Category)getGroup(groupPosition)).getSubcategories().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return ((Category)getGroup(groupPosition)).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return ((Category)getChild(groupPosition, childPosition)).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Category category = (Category) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.category_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lbl_category_header);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(category.getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Category subcategory = (Category) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.subcategory_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lbl_subcategory_header);

        txtListChild.setText(subcategory.getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
