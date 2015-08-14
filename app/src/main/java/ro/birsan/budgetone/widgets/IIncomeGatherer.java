package ro.birsan.budgetone.widgets;

/**
 * Created by ibirsan on 8/10/2015.
 */
public interface IIncomeGatherer {
    void showIncomeTextBox();
    void setOnIncomeAddedListener(IIncomeGathererListener toAdd);

    interface IIncomeGathererListener
    {
        void onAddIncome(String income);
    }

}