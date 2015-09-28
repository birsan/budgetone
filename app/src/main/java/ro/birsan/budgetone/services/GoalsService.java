package ro.birsan.budgetone.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ro.birsan.budgetone.data.Goal;
import ro.birsan.budgetone.data.GoalsDataSource;
import ro.birsan.budgetone.data.TransactionsDataSource;

/**
 * Created by Irinel on 9/26/2015.
 */
public class GoalsService {
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

    public List<GoalWithProgress> getInProgressGoals()
    {
        List<GoalWithProgress> inProgressGoals = new ArrayList<>();
        List<Goal> goals = _goalGoalDataSource.getAllGoals();
        for(Goal goal : goals)
        {
            GoalWithProgress goalWithProgress = new GoalWithProgress(goal, _transactionsDataSource.getGoalTransactionsAmount(goal.get_id()));
            if (!goalWithProgress.getIsDone())
            {
                inProgressGoals.add(goalWithProgress);
            }
        }
        return inProgressGoals;
    }

    public List<GoalWithProgress> getAccomplishedGoals()
    {
        List<GoalWithProgress> accomplishedGoals = new ArrayList<>();
        List<Goal> goals = _goalGoalDataSource.getAllGoals();
        for(Goal goal : goals)
        {
            GoalWithProgress goalWithProgress = new GoalWithProgress(goal, _transactionsDataSource.getGoalTransactionsAmount(goal.get_id()));
            if (!goalWithProgress.getIsDone())
            {
                accomplishedGoals.add(goalWithProgress);
            }
        }
        return accomplishedGoals;
    }

    public Double addAmount(UUID goalId, Double amountToAdd) {
        _transactionsDataSource.addTransactionForGoal(goalId, amountToAdd);
        return _transactionsDataSource.getGoalTransactionsAmount(goalId);
    }
}
