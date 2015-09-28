package ro.birsan.budgetone.services;

import ro.birsan.budgetone.data.IncomesDataSource;
import ro.birsan.budgetone.data.TransactionsDataSource;

/**
 * Created by Irinel on 9/28/2015.
 */
public class BalanceService {
    private IncomesDataSource _incomesDataSource;
    private TransactionsDataSource _transactionsDataSource;

    public BalanceService(IncomesDataSource incomesDataSource, TransactionsDataSource transactionsDataSource) {
        _incomesDataSource = incomesDataSource;
        _transactionsDataSource = transactionsDataSource;
    }

    public Double getAvailableAmount()
    {
        Double incomeAmount = _incomesDataSource.getIncomeAmount();
        Double transactionsAmount = _transactionsDataSource.getTransactionsAmount();
        return incomeAmount - transactionsAmount;
    }
}
