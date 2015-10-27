package ro.birsan.budgetone;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
public class GoalsFragment extends Fragment implements ViewPager.OnPageChangeListener{
    private OnFragmentInteractionListener mListener;
    ViewPager _viewPager;
    FragmentStatePagerAdapter _tabsAdapter;
    GoalsService _goalsService;
    BudgetService _budgetService;
    BalanceService _balanceService;
    List<GoalExt> _goals;

    public GoalsFragment() {
        // Required empty public constructor
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.onGoalPageSelected(1, _goals.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        final TransactionsDataSource transactionsDataSource = new TransactionsDataSource(getActivity());
        _goalsService = new GoalsService(new GoalsDataSource(getActivity()), transactionsDataSource);
        _balanceService = new BalanceService(new IncomesDataSource(getActivity()), transactionsDataSource);
        _budgetService = new BudgetService(transactionsDataSource, new CategoriesDataSource(getActivity()), new BudgetsDataSource(getActivity()));
        _goals = _goalsService.getInProgressGoals();
        Double budgetedAmount = _budgetService.getMonthBudgetedAmount(new Date());
        final Double notBudgetedAmount = _balanceService.getAmountToBudgetCurrentMonth() - budgetedAmount;

        _viewPager = (ViewPager) view.findViewById(R.id.pager);
        _tabsAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                GoalExt goal = _goals.get(position);
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
                return _goals.size();
            }
        };

        _viewPager.setAdapter(_tabsAdapter);
        _viewPager.addOnPageChangeListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GoalsFragment.OnFragmentInteractionListener");
        }
    }

    private String getAdviceText(GoalExt goal)
    {
        Double currentMonthNotBudgetedAmount = _balanceService.getCurrentMonthIncome() - _budgetService.getMonthBudgetedAmount(new Date());
        Double recommendedDeposit = _goalsService.getRecommendedDepositForCurrentMonth(goal.get_id(), currentMonthNotBudgetedAmount);
        if (recommendedDeposit == null) return "Please adjust your budget. Based on current month budget, this goal is not doable till this date.";

        if (recommendedDeposit == Double.MAX_VALUE) return "No recommendation";

        if (recommendedDeposit <= 0) return "Good job! You are on track";

        return "Suggestion is to add " + recommendedDeposit.intValue() + " more this month.";
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mListener.onGoalPageSelected(position + 1, _goals.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public interface OnFragmentInteractionListener {
        void onGoalPageSelected(Integer position, Integer count);
    }
}
