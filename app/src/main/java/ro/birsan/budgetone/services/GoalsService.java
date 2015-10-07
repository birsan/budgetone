package ro.birsan.budgetone.services;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ro.birsan.budgetone.data.Goal;
import ro.birsan.budgetone.data.GoalsDataSource;
import ro.birsan.budgetone.data.Transaction;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.util.DateTimeHelper;

/**
 * Created by Irinel on 9/26/2015.
 */
public class GoalsService implements Closeable {
    private GoalsDataSource _goalGoalDataSource;
    private TransactionsDataSource _transactionsDataSource;

    public GoalsService(GoalsDataSource goalGoalDataSource, TransactionsDataSource transactionsDataSource) {
        _goalGoalDataSource = goalGoalDataSource;
        _transactionsDataSource = transactionsDataSource;
    }

    public void createGoal(String name, String description, byte[] image, Double targetAmount, Date dueDate)
    {
        UUID id = UUID.randomUUID();
        Date createdOn = new Date();
        Goal goal = new Goal(id, name, description, image, targetAmount, dueDate, createdOn);
        _goalGoalDataSource.createGoal(goal);
    }

    public GoalExt getGoal(UUID goalId)
    {
        Goal goal = _goalGoalDataSource.getGoal(goalId);
        return new GoalExt(goal, getGoalTransactionsAmount(goal.get_id()), 0.0);
    }

    public List<GoalExt> getInProgressGoals()
    {
        List<GoalExt> inProgressGoals = new ArrayList<>();
        List<Goal> goals = _goalGoalDataSource.getAllGoals();
        for(Goal goal : goals)
        {
            Double transactionsAmount = getGoalTransactionsAmount(goal.get_id());
            Double advice = 0.0;
            if (goal.get_dueDate() != null) {
                Double currentMonthTransactionAmount = getTransactionAmountForMonth(goal.get_id(), new Date());
                int monthsLeft = Math.abs(DateTimeHelper.monthsBetween(goal.get_dueDate(), new Date()));
                Double amountBeforeThisMonth = transactionsAmount - currentMonthTransactionAmount;
                Double minAmountPerMonth = (goal.get_targetAmount() - amountBeforeThisMonth) / monthsLeft;
                if (minAmountPerMonth > currentMonthTransactionAmount) {
                    advice = Math.ceil(minAmountPerMonth - currentMonthTransactionAmount);
                }
            }

            GoalExt goalExt = new GoalExt(goal, transactionsAmount, advice);
            if (!goalExt.getIsDone())
            {
                inProgressGoals.add(goalExt);
            }
        }
        return inProgressGoals;
    }

    public List<GoalExt> getAccomplishedGoals()
    {
        List<GoalExt> accomplishedGoals = new ArrayList<>();
        List<Goal> goals = _goalGoalDataSource.getAllGoals();
        for(Goal goal : goals)
        {
            GoalExt goalExt = new GoalExt(goal, getGoalTransactionsAmount(goal.get_id()), 0.0);
            if (goalExt.getIsDone())
            {
                accomplishedGoals.add(goalExt);
            }
        }
        return accomplishedGoals;
    }

    public Double addAmount(UUID goalId, Double amountToAdd) {
        _transactionsDataSource.addTransactionForGoal(goalId, amountToAdd);
        return getGoalTransactionsAmount(goalId);
    }

    public void removeGoal(UUID goalId)
    {
        _goalGoalDataSource.deleteGoal(goalId);
        List<Transaction> transactions =  _transactionsDataSource.getTransactionsByGoal(goalId);
        for(Transaction transaction : transactions)
        {
            _transactionsDataSource.remove(transaction.get_id());
        }
    }

    @Override
    public void close() {
        _transactionsDataSource.close();
        _goalGoalDataSource.close();
    }

    private Double getTransactionAmountForMonth(UUID goalId, Date date)
    {
        Double amount = 0.0;
        List<Transaction> transactions =  _transactionsDataSource.getTransactionsByGoal(goalId);
        for(Transaction transaction : transactions)
        {
            if (DateTimeHelper.monthsBetween(transaction.get_createdOn(), date) == 0)
            {
                amount += transaction.get_amount();
            }
        }
        return amount;
    }

    private Double getGoalTransactionsAmount(UUID goalId)
    {
        Double amount = 0.0;
        List<Transaction> transactions =  _transactionsDataSource.getTransactionsByGoal(goalId);
        for(Transaction transaction : transactions)
        {
            amount += transaction.get_amount();
        }
        return amount;
    }
}
