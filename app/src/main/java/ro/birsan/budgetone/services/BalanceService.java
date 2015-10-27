package ro.birsan.budgetone.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ro.birsan.budgetone.data.Income;
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

    public Double getAmountToBudgetCurrentMonth()
    {
        return getAvailableAmount() + _transactionsDataSource.getTransactionsAmountByMonth(new Date());
    }

    public Double getAvailableAmount()
    {
        Double incomeAmount = _incomesDataSource.getIncomeAmount();
        Double transactionsAmount = _transactionsDataSource.getTransactionsAmount();
        return incomeAmount - transactionsAmount;
    }

    public Double getCurrentMonthIncome()
    {
        Calendar c = Calendar.getInstance();
        Double monthIncome = 0.0;
        List<Income> incomeList = _incomesDataSource.getIncome(c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
        for(Income income : incomeList)
        {
            monthIncome += income.get_amount();
        }
        return monthIncome;
    }
}
