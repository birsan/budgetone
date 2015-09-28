package ro.birsan.budgetone.adapters;

import android.content.Context;
import android.text.Html;
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
        TextView tvSuggestedMin;
        TextView tvSuggestedMax;
        TextView tvExpensesAverage;
        TextView tvLastMonth;
        TextView tvLastMonthLabel;
        TextView tvMonthAverageLabel;
        TextView tvAdviceLabel;
        TextView tvTo;
        SeekBar progress;
        ImageView plus;
        ImageView minus;

        public void showButtons(){
            setVisibility(View.VISIBLE);
        }

        public void hideButtons(){
            setVisibility(View.GONE);
        }

        private void setVisibility(int visibility) {
            plus.setVisibility(visibility);
            minus.setVisibility(visibility);
            tvSuggestedMin.setVisibility(visibility);
            tvSuggestedMax.setVisibility(visibility);
            tvExpensesAverage.setVisibility(visibility);
            tvLastMonth.setVisibility(visibility);
            tvLastMonthLabel.setVisibility(visibility);
            tvMonthAverageLabel.setVisibility(visibility);
            tvAdviceLabel.setVisibility(visibility);
            tvTo.setVisibility(visibility);


            plus.requestLayout();
            minus.requestLayout();
            tvSuggestedMin.requestLayout();
            tvSuggestedMax.requestLayout();
            tvExpensesAverage.requestLayout();
            tvLastMonth.requestLayout();
            tvLastMonthLabel.requestLayout();
            tvMonthAverageLabel.requestLayout();
            tvAdviceLabel.requestLayout();
            tvTo.requestLayout();
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
            viewHolder.tvSuggestedMin = (TextView) convertView.findViewById(R.id.tvSuggestedMin);
            viewHolder.tvSuggestedMax = (TextView) convertView.findViewById(R.id.tvSuggestedMax);
            viewHolder.tvExpensesAverage = (TextView) convertView.findViewById(R.id.tvExpensesAverage);
            viewHolder.tvLastMonth = (TextView) convertView.findViewById(R.id.tvLastMonth);
            viewHolder.tvAdviceLabel = (TextView) convertView.findViewById(R.id.tvAdviceLabel);
            viewHolder.tvMonthAverageLabel = (TextView) convertView.findViewById(R.id.tvMonthAverageLabel);
            viewHolder.tvLastMonthLabel = (TextView) convertView.findViewById(R.id.tvLastMonthLabel);
            viewHolder.tvTo = (TextView) convertView.findViewById(R.id.tvTo);
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
        String tvSuggestedMinText = "<u>" + viewModel.get_suggestedMinAmount().toString() + "</u>";
        String tvSuggestedMaxText = "<u>" + viewModel.get_suggestedMaxAmount().toString() + "</u>";
        String expensesAverageText = "<u>" + viewModel.get_monthAverage().toString() + "</u>";
        String lastMonthText = "<u>" + viewModel.get_lastMonthBudgetedAmount().toString() + "</u>";
        viewHolder.tvSuggestedMin.setText(Html.fromHtml(tvSuggestedMinText));
        viewHolder.tvSuggestedMax.setText(Html.fromHtml(tvSuggestedMaxText));
        viewHolder.tvExpensesAverage.setText(Html.fromHtml(expensesAverageText));
        viewHolder.tvLastMonth.setText(Html.fromHtml(lastMonthText));
        viewHolder.progress.setMax(_amountLeftForBudget.intValue() + viewModel.get_amount().intValue());
        viewHolder.progress.setProgress(viewModel.get_amount().intValue());
        viewHolder.progress.setEnabled(viewHolder.progress.getMax() > 0);
        if (_currentConfiguredViewHolder != viewHolder) {
            viewHolder.hideButtons();
        }
        else {
            viewHolder.tvLastMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        setAmount(viewModel, Double.valueOf(viewModel.get_lastMonthBudgetedAmount()));
                    } catch (NumberFormatException e) {
                    }
                }
            });

            viewHolder.tvExpensesAverage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        setAmount(viewModel, Double.valueOf(viewModel.get_monthAverage()));
                    } catch (NumberFormatException e) {
                    }
                }
            });

            viewHolder.tvSuggestedMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAmount(viewModel, viewModel.get_suggestedMinAmount());
                }
            });

            viewHolder.tvSuggestedMax.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAmount(viewModel, viewModel.get_suggestedMaxAmount());
                }
            });

            viewHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAmount(viewModel, viewModel.get_amount() + 1);
                }
            });

            viewHolder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAmount(viewModel, viewModel.get_amount() - 1);
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

    private void setAmount(BudgetConfigurationViewModel viewModel, Double amount) {
        viewModel.set_amount(Math.max(0, Math.min(amount, _amountLeftForBudget.intValue() + viewModel.get_amount().intValue())));
        refreshSeekBars();
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
