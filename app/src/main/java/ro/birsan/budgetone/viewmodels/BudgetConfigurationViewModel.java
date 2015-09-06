package ro.birsan.budgetone.viewmodels;

/**
 * Created by Irinel on 9/4/2015.
 */
public class BudgetConfigurationViewModel {
    private long _budgetId;
    private long _categoryId;
    private String _categoryName;
    private Double _amount;

    public BudgetConfigurationViewModel(long budgetId, long categoryId, String categoryName, double amount)
    {
        _categoryId = categoryId;
        _categoryName = categoryName;
        _amount = amount;
        _budgetId = budgetId;
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
