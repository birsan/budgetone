package ro.birsan.budgetone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ro.birsan.budgetone.R;
import ro.birsan.budgetone.viewmodels.HistoryViewModel;

/**
 * Created by Irinel on 9/18/2015.
 */
public class HistoryAdapter extends ArrayAdapter<HistoryViewModel> {
    private static class ViewHolder {
        TextView tvTitle;
        TextView tvCreatedOn;
        TextView tvAmount;
    }

    public HistoryAdapter(Context context, List<HistoryViewModel> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryViewModel viewModel = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_history_item, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvCreatedOn = (TextView) convertView.findViewById(R.id.tvCreatedOn);
            viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(viewModel.get_title());
        viewHolder.tvAmount.setText(String.valueOf(viewModel.get_amount()));
        viewHolder.tvCreatedOn.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(viewModel.get_createdOn()));

        return convertView;
    }
}
