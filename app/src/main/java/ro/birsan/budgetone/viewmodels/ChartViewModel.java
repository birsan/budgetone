package ro.birsan.budgetone.viewmodels;

/**
 * Created by Irinel on 9/15/2015.
 */
public class ChartViewModel {
    private String _categoryName;
    private int _amount;

    public ChartViewModel(String categoryName, int amount) {
        _categoryName = categoryName;
        _amount = amount;
    }

    public String get_categoryName() {
        return _categoryName;
    }

    public int get_amount() {
        return _amount;
    }
}
