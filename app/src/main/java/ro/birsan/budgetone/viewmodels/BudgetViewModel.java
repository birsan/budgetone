package ro.birsan.budgetone.viewmodels;

/**
 * Created by Irinel on 9/3/2015.
 */
public class BudgetViewModel {
    private long _categoryId;
    private String _categoryName;
    private Double _totalAmount;
    private Double _consumedAmount;

    public BudgetViewModel(long categoryId, String categoryName, Double totalAmount, Double consumedAmount)
    {
        _categoryName = categoryName;
        _totalAmount = totalAmount;
        _consumedAmount = consumedAmount;
        _categoryId = categoryId;
    }

    public long get_categoryId() {
        return _categoryId;
    }

    public String get_categoryName() {
        return _categoryName;
    }

    public Double get_totalAmount() {
        return _totalAmount;
    }

    public Double get_consumedAmount() {
        return _consumedAmount;
    }

    /**
     * Gets the difference between the total amount and what is consumed.
     */
    public Double get_leftAmount() {
        return _totalAmount - _consumedAmount;
    }

    /**
     * Gets the left amount formatted. e.g. 100 Left or -100 Over
     */
    public String get_leftAmountDisplay() {
        if (get_leftAmount() >= 0) return get_leftAmount() + " Left";
        else return get_leftAmount() + " Over";
    }
}
