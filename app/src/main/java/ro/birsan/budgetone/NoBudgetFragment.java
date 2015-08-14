package ro.birsan.budgetone;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoBudgetFragment extends Fragment {

    public NoBudgetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_budget, container, false);
        TextView lnkSelectCategories = (TextView)view.findViewById(R.id.lnkSelectCategories);
        TextView lnkCopyLastMonth = (TextView)view.findViewById(R.id.lnkCopyLastMonth);

        lnkSelectCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectCategoriesActivity.class);
                startActivity(intent);
            }
        });

        lnkCopyLastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CopyLastMonthBudgetActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


}
