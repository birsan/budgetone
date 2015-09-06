package ro.birsan.budgetone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import ro.birsan.budgetone.R;
import ro.birsan.budgetone.viewmodels.BudgetConfigurationViewModel;

/**
 * Created by Irinel on 9/4/2015.
 */
public class BudgetConfigurationListAdapter extends ArrayAdapter<BudgetConfigurationViewModel> {

    private static class ViewHolder {
        TextView name;
        TextView amount;
        SeekBar progress;
    }

    private Double _income;
    private List<BudgetConfigurationViewModel> _objects;
    Double _amountLeftForBudget = 0.0;

    public BudgetConfigurationListAdapter(Context context, List<BudgetConfigurationViewModel> objects, Double income) {
        super(context, 0, objects);
        _income = income;
        _objects = objects;
        ComputeAmountLeftForBudget();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BudgetConfigurationViewModel viewModel = getItem(position);

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.budget_configuration_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.category_name);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
            viewHolder.progress = (SeekBar) convertView.findViewById(R.id.seekBar);

            viewHolder.progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        viewModel.set_amount(Double.parseDouble(String.valueOf(progress)));
                        viewHolder.amount.setText(String.valueOf(progress));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    ComputeAmountLeftForBudget();
                    notifyDataSetChanged();
                }
            });

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(viewModel.get_categoryName());
        viewHolder.amount.setText(String.valueOf(viewModel.get_amount().intValue()));
        viewHolder.progress.setMax(_amountLeftForBudget.intValue() + viewModel.get_amount().intValue());
        viewHolder.progress.setProgress(viewModel.get_amount().intValue());
        viewHolder.progress.setEnabled(viewHolder.progress.getMax() > 0);

        return convertView;
    }

    private void ComputeAmountLeftForBudget() {
        _amountLeftForBudget = 0.0;
        for(BudgetConfigurationViewModel object: _objects)
        {
            _amountLeftForBudget += object.get_amount();
        }
        _amountLeftForBudget = _income - _amountLeftForBudget;
    }
}
