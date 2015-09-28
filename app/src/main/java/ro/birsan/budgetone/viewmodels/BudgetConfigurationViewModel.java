package ro.birsan.budgetone.viewmodels;

/**
 * Created by Irinel on 9/4/2015.
 */
public class BudgetConfigurationViewModel {
    private long _budgetId;
    private long _categoryId;
    private String _categoryName;
    private Double _amount;
    private Double _suggestedMinAmount;
    private Double _suggestedMaxAmount;
    private String _monthAverage;
    private String _lastMonthBudgetedAmount;

    public BudgetConfigurationViewModel(
            long budgetId,
            long categoryId,
            String categoryName,
            double amount,
            Double suggestedMinAmount,
            Double suggestedMaxAmount,
            String monthAverage,
            String lastMonthBudgetedAmount)
    {
        _categoryId = categoryId;
        _categoryName = categoryName;
        _amount = amount;
        _budgetId = budgetId;
        _suggestedMinAmount = suggestedMinAmount;
        _suggestedMaxAmount = suggestedMaxAmount;
        _monthAverage = monthAverage;
        _lastMonthBudgetedAmount = lastMonthBudgetedAmount;
    }

    public String get_lastMonthBudgetedAmount() {
        return _lastMonthBudgetedAmount;
    }

    public String get_monthAverage() {
        return _monthAverage;
    }

    public Double get_suggestedMaxAmount() {
        return _suggestedMaxAmount;
    }

    public Double get_suggestedMinAmount() {
        return _suggestedMinAmount;
    }

    public long get_categoryId() {
        return _categoryId;
    }

    public long get_budgetId() {
        return _budgetId;
    }

    public String get_categoryName() {
        return _categoryName;
    }

    public Double get_amount() {
        return _amount;
    }

    public void set_amount(Double amount) {
        _amount = amount;
    }
}
