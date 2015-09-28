package ro.birsan.budgetone.services;

import java.util.Calendar;
import java.util.Map;

import ro.birsan.budgetone.data.TransactionsDataSource;

/**
 * Created by Irinel on 9/25/2015.
 */
public class ExpensesService {
    private TransactionsDataSource _transactionsDataSource;

    public ExpensesService(TransactionsDataSource transactionsDataSource) {
        _transactionsDataSource = transactionsDataSource;
    }

    public Double getAverageExpensesPerMonth(Long categoryId) {
        Calendar c = Calendar.getInstance();
        Map<String, Double> expensesAmountPerMonth = _transactionsDataSource.getExpensesAmountPerMonth(categoryId, c.get(Calendar.MONTH), c.get(Calendar.YEAR));
        Double averagePerMonth = 0.0;
        int monthCount = 0;
        for (Double amountPerMonth : expensesAmountPerMonth.values()) {
            monthCount++;
            averagePerMonth = (averagePerMonth + amountPerMonth) / monthCount;
        }
        return averagePerMonth;
    }
}
