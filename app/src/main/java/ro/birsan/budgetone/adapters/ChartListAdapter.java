package ro.birsan.budgetone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import ro.birsan.budgetone.R;
import ro.birsan.budgetone.viewmodels.ChartViewModel;

/**
 * Created by Irinel on 9/15/2015.
 */
public class ChartListAdapter extends ArrayAdapter<ChartViewModel> {
    private static class ViewHolder {
        TextView tvCategory;
        TextView tvAmount;
        ProgressBar pbAmount;
    }

    private int _maxProgress;

    public ChartListAdapter(Context context, ChartViewModel[] objects, int maxProgress) {
        super(context, 0, objects);

        _maxProgress = maxProgress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChartViewModel viewModel = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_chart_item, parent, false);
            viewHolder.tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);
            viewHolder.pbAmount = (ProgressBar) convertView.findViewById(R.id.pbAmount);
            viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvCategory.setText(viewModel.get_categoryName());
        viewHolder.tvAmount.setText(String.valueOf(viewModel.get_amount()));
        viewHolder.pbAmount.setMax(_maxProgress);
        viewHolder.pbAmount.setProgress(viewModel.get_amount());

        return convertView;
    }
}
