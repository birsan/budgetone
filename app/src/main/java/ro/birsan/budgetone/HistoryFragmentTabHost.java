package ro.birsan.budgetone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragmentTabHost extends Fragment {

    public HistoryFragmentTabHost() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_history_fragment_tab_host, container, false);

        FragmentTabHost tabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
        tabHost.addTab(tabHost.newTabSpec("fragment1").setIndicator("Expenses"), ExpensesHistoryFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("fragment2").setIndicator("Income"), IncomeHistoryFragment.class, null);

        return view;
    }
}
