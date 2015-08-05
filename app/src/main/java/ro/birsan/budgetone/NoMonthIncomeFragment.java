package ro.birsan.budgetone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ro.birsan.budgetone.data.IncomesDataSource;
import ro.birsan.budgetone.util.IncomeParser;
import ro.birsan.budgetone.widgets.ContextualEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoMonthIncomeFragment extends Fragment {

    public NoMonthIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_no_month_income, container, false);
        final ContextualEditText txtNewIncome = (ContextualEditText) view.findViewById(R.id.txtNewIncome);
        final TextView cto = (TextView) view.findViewById(R.id.cto);

        cto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtNewIncome.show();
            }
        });

        txtNewIncome.setOnActionListener(new ContextualEditText.ContextualEditTextActionListener() {
            @Override
            public void onSend(String value) {
                IncomeParser incomeParser = new IncomeParser(value);
                if (!incomeParser.isValid()) {
                    Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
                    return;
                }

                IncomesDataSource incomesDataSource = new IncomesDataSource(getActivity());
                incomesDataSource.addIncome(incomeParser.getAmount(), incomeParser.getSource());
            }
        });

        return view;
    }


}
