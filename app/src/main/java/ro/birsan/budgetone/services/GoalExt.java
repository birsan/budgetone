package ro.birsan.budgetone.services;

import ro.birsan.budgetone.data.Goal;

/**
 * Created by Irinel on 9/28/2015.
 */
public class GoalExt extends Goal {
    private Double _progress;

    public GoalExt(Goal goal, Double progress) {
        super(goal.get_id(), goal.get_name(), goal.get_description(), goal.get_image(), goal.get_targetAmount(), goal.get_dueDate(), goal.get_createdOn());
        _progress = progress;
    }

    public Double get_progress() {
        return _progress;
    }

    public boolean getIsDone()
    {
        return _progress >= get_targetAmount();
    }

    public Double getRemaining()
    {
        return get_targetAmount() - get_progress();
    }
}
