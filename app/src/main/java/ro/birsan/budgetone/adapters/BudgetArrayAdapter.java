package ro.birsan.budgetone.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ro.birsan.budgetone.R;
import ro.birsan.budgetone.viewmodels.BudgetViewModel;

/**
 * Created by Irinel on 9/4/2015.
 */
public class BudgetArrayAdapter extends ArrayAdapter<BudgetViewModel> {

    private static class ViewHolder {
        TextView name;
        TextView amount;
        ProgressBar progress;
    }

    public BudgetArrayAdapter(Context context, List<BudgetViewModel> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BudgetViewModel viewModel = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.budget_list_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.category_name);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.amount_left);
            viewHolder.progress = (ProgressBar) convertView.findViewById(R.id.progress);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(viewModel.get_categoryName());
        viewHolder.amount.setText(viewModel.get_leftAmountDisplay());
        viewHolder.progress.setProgress((int) (viewModel.get_consumedAmount() * 100 / viewModel.get_totalAmount()));
        viewHolder.progress.getProgressDrawable().setColorFilter(viewModel.get_color(), PorterDuff.Mode.SRC_IN);

        return convertView;
    }
}
