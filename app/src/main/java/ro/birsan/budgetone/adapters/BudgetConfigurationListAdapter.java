package ro.birsan.budgetone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        ImageView plus;
        ImageView minus;

        public void showButtons(){
            plus.getLayoutParams().width = (int) (16 * scale + 0.5f);
            minus.getLayoutParams().width = (int) (16 * scale + 0.5f);
            plus.getLayoutParams().height = (int) (16 * scale + 0.5f);
            minus.getLayoutParams().height = (int) (16 * scale + 0.5f);
            plus.requestLayout();
            minus.requestLayout();
        }

        public void hideButtons(){
            plus.getLayoutParams().width = 0;
            minus.getLayoutParams().width = 0;
            plus.getLayoutParams().height = 0;
            minus.getLayoutParams().height = 0;
            plus.requestLayout();
            minus.requestLayout();
        }
    }

    static float scale;
    private Double _income;
    private List<BudgetConfigurationViewModel> _objects;
    Double _amountLeftForBudget = 0.0;
    private ViewHolder _currentConfiguredViewHolder = null;
    private AdapterCallbacks _adapterCallbacks;

    public BudgetConfigurationListAdapter(Context context, List<BudgetConfigurationViewModel> objects, Double income, AdapterCallbacks adapterCallbacks) {
        super(context, 0, objects);
        _income = income;
        _objects = objects;
        ComputeAmountLeftForBudget();
        scale = getContext().getResources().getDisplayMetrics().density;
        _adapterCallbacks = adapterCallbacks;
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
            viewHolder.plus = (ImageView) convertView.findViewById(R.id.btnPlus);
            viewHolder.minus = (ImageView) convertView.findViewById(R.id.btnMinus);
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
        if (_currentConfiguredViewHolder != viewHolder) {
            viewHolder.hideButtons();
        }
        else {
            viewHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.set_amount(viewModel.get_amount() + 1);
                    refreshSeekBars();
                }
            });

            viewHolder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.set_amount(viewModel.get_amount() - 1);
                    refreshSeekBars();
                }
            });
        }

        viewHolder.progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    viewModel.set_amount(Double.parseDouble(String.valueOf(progress)));
                    viewHolder.amount.setText(String.valueOf(progress));
                    if (_currentConfiguredViewHolder != viewHolder) {
                        if (_currentConfiguredViewHolder != null) {
                            _currentConfiguredViewHolder.hideButtons();
                        }
                        _currentConfiguredViewHolder = viewHolder;
                        _currentConfiguredViewHolder.showButtons();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                refreshSeekBars();
            }
        });

        return convertView;
    }

    private void refreshSeekBars(){
        ComputeAmountLeftForBudget();
        notifyDataSetChanged();
        _adapterCallbacks.onBudgetChaged(_income - _amountLeftForBudget);
    }

    private void ComputeAmountLeftForBudget() {
        _amountLeftForBudget = 0.0;
        for(BudgetConfigurationViewModel object: _objects)
        {
            _amountLeftForBudget += object.get_amount();
        }
        _amountLeftForBudget = _income - _amountLeftForBudget;
    }

    public interface AdapterCallbacks
    {
        void onBudgetChaged(Double budgetedAmount);
    }
}
