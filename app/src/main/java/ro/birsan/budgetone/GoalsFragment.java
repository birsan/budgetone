package ro.birsan.budgetone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ro.birsan.budgetone.data.GoalsDataSource;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.services.GoalWithProgress;
import ro.birsan.budgetone.services.GoalsService;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoalsFragment extends Fragment {
    ViewPager _viewPager;
    FragmentPagerAdapter _tabsAdapter;
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

        _viewPager = (ViewPager) view.findViewById(R.id.pager);
        _tabsAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                GoalWithProgress goal = goalsService.getInProgressGoals().get(position);
                String dueDate = goal.get_dueDate() != null ? goal.get_dueDate().toString() : "No due date";
                String progressDescription = goal.get_progress() + " out of " + goal.get_targetAmount();
                Bundle args = GoalFragment.buildArguments(
                        goal.get_id().toString(),
                        goal.get_name(),
                        goal.get_description(),
                        dueDate,
                        progressDescription,
                        goal.get_targetAmount(),
                        goal.get_progress(),
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


}
