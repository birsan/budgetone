package ro.birsan.budgetone.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ro.birsan.budgetone.data.Transaction;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.util.CollectionHelper;
import ro.birsan.budgetone.util.IPredicate;

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

    public List<Transaction> getTransactions(Date date, final Long categoryId, final UUID goalId) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        List<Transaction> transactions = _transactionsDataSource.getAllTransactionsByMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
        if (categoryId != null) {
            transactions = (List<Transaction>) CollectionHelper.filter(transactions, new IPredicate<Transaction>() {
                @Override
                public boolean apply(Transaction transaction) {
                    return transaction.get_categoryId() == categoryId;
                }
            });
        }

        if (goalId != null) {
            transactions = (List<Transaction>) CollectionHelper.filter(transactions, new IPredicate<Transaction>() {
                @Override
                public boolean apply(Transaction transaction) {
                    return transaction.get_goalId() == goalId;
                }
            });
        }

        return transactions;
    }

    public void remove(Long transactionId) {
        _transactionsDataSource.remove(transactionId);
    }
}
