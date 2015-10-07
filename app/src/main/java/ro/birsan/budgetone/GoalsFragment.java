package ro.birsan.budgetone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

import ro.birsan.budgetone.data.BudgetsDataSource;
import ro.birsan.budgetone.data.CategoriesDataSource;
import ro.birsan.budgetone.data.GoalsDataSource;
import ro.birsan.budgetone.data.IncomesDataSource;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.services.BalanceService;
import ro.birsan.budgetone.services.BudgetService;
import ro.birsan.budgetone.services.GoalExt;
import ro.birsan.budgetone.services.GoalsService;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoalsFragment extends Fragment {
    ViewPager _viewPager;
    FragmentStatePagerAdapter _tabsAdapter;
    //private final GoalsService goalsService = new GoalsService(new GoalsDataSource(getActivity()));

    public GoalsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        final TransactionsDataSource transactionsDataSource = new TransactionsDataSource(getActivity());
        final GoalsService goalsService = new GoalsService(new GoalsDataSource(getActivity()), transactionsDataSource);
        BalanceService balanceService = new BalanceService(new IncomesDataSource(getActivity()), transactionsDataSource);
        BudgetService budgetService = new BudgetService(transactionsDataSource, new CategoriesDataSource(getActivity()), new BudgetsDataSource(getActivity()));
        Double availableAmount = balanceService.getAvailableAmount();
        Double budgetedAmount = budgetService.getMonthBudgetedAmount(new Date());
        final Double notBudgetedAmount = availableAmount - budgetedAmount;

        _viewPager = (ViewPager) view.findViewById(R.id.pager);
        _tabsAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                GoalExt goal = goalsService.getInProgressGoals().get(position);
                String dueDate = goal.get_dueDate() != null ? new SimpleDateFormat("dd MMM yyyy").format(goal.get_dueDate()) : "No due date";
                String availableDesc = "Available (not budgeted) amount is " + notBudgetedAmount.intValue();
                Bundle args = GoalFragment.buildArguments(
                        goal.get_id().toString(),
                        goal.get_name(),
                        goal.get_description(),
                        dueDate,
                        goal.get_targetAmount(),
                        goal.get_progress(),
                        getAdviceText(goal),
                        availableDesc,
                        goal.get_image());
                GoalFragment goalFragment = new GoalFragment();
                goalFragment.setArguments(args);
                return goalFragment;
            }

            @Override
            public int getCount() {
                return goalsService.getInProgressGoals().size();
            }
        };

        _viewPager.setAdapter(_tabsAdapter);
        return view;
    }

    private String getAdviceText(GoalExt goal)
    {
        if (goal.get_advice() <= 0) return "Good job! You are on track";

        return "Suggestion is to add " + goal.get_advice().intValue() + " more this month.";
    }
}
