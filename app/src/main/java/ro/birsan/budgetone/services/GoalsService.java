package ro.birsan.budgetone.services;

import android.support.annotation.Nullable;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ro.birsan.budgetone.data.Goal;
import ro.birsan.budgetone.data.GoalsDataSource;
import ro.birsan.budgetone.data.Transaction;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.util.CollectionHelper;
import ro.birsan.budgetone.util.DateTimeHelper;
import ro.birsan.budgetone.util.IPredicate;

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
        return new GoalExt(goal, getGoalTransactionsAmount(goal.get_id()));
    }

    public List<GoalExt> getInProgressGoals()
    {
        List<GoalExt> inProgressGoals = new ArrayList<>();
        List<Goal> goals = _goalGoalDataSource.getAllGoals();
        for(Goal goal : goals)
        {
            Double transactionsAmount = getGoalTransactionsAmount(goal.get_id());
            GoalExt goalExt = new GoalExt(goal, transactionsAmount);
            if (!goalExt.getIsDone())
            {
                inProgressGoals.add(goalExt);
            }
        }
        Collections.sort(inProgressGoals, new Comparator<GoalExt>() {
            @Override
            public int compare(GoalExt lhs, GoalExt rhs) {
                return lhs.get_dueDate().compareTo(rhs.get_dueDate());
            }
        });
        return inProgressGoals;
    }

    public List<GoalExt> getAccomplishedGoals()
    {
        List<GoalExt> accomplishedGoals = new ArrayList<>();
        List<Goal> goals = _goalGoalDataSource.getAllGoals();
        for(Goal goal : goals)
        {
            GoalExt goalExt = new GoalExt(goal, getGoalTransactionsAmount(goal.get_id()));
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

    @Nullable
    public Double getRecommendedDepositForCurrentMonth(UUID goalId, Double currentMonthNotBudgetedAmount) {
        if (currentMonthNotBudgetedAmount < 0)
            return null;

        GoalExt targetGoal = getGoal(goalId);
        if (targetGoal.get_dueDate() == null)
            return Double.MAX_VALUE;

        if (targetGoal.getIsDone())
            return null;

        List<GoalExt> goals = (List<GoalExt>) CollectionHelper.filter(getInProgressGoals(), new IPredicate<GoalExt>() {
            @Override
            public boolean apply(GoalExt goalExt) {
                return goalExt.get_dueDate() != null;
            }
        });
        Collections.sort(goals, new Comparator<GoalExt>() {
            @Override
            public int compare(GoalExt lhs, GoalExt rhs) {
                return lhs.get_dueDate().compareTo(rhs.get_dueDate());
            }
        });

        int maxNumberOfMonth = 0;
        Double neededAmount = .0;
        Double currentMonthLeftForGoal = currentMonthNotBudgetedAmount;
        for(GoalExt goal : goals)
        {
            int monthsRemaining = DateTimeHelper.monthsBetween(new Date(),  goal.get_dueDate());
            maxNumberOfMonth = Math.max(monthsRemaining, maxNumberOfMonth);
            neededAmount += goal.getRemaining();
            if (goal.get_id().equals(goalId))
            {
                break;
            }

            currentMonthLeftForGoal -= neededAmount;
        }

        if (maxNumberOfMonth * currentMonthNotBudgetedAmount < neededAmount)
            return null;

        return Math.max(currentMonthLeftForGoal, 0);
    }
}
