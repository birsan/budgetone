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
import ro.birsan.budgetone.widgets.IIncomeGatherer;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoMonthIncomeFragment extends Fragment implements IIncomeGatherer {

    private IIncomeGathererListener _listener;
    ContextualEditText _txtNewIncome;

    public NoMonthIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_no_month_income, container, false);
        _txtNewIncome = (ContextualEditText) view.findViewById(R.id.txtNewIncome);
        final TextView cto = (TextView) view.findViewById(R.id.cto);

        cto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showIncomeTextBox();
            }
        });

        _txtNewIncome.setOnActionListener(new ContextualEditText.ContextualEditTextActionListener() {
            @Override
            public void onSend(String value) {
                if (_listener != null) {
                    _listener.onAddIncome(value);
                    return;
                }

                IncomeParser incomeParser = new IncomeParser(value);
                if (!incomeParser.isValid()) {
                    Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_LONG).show();
                    return;
                }

                IncomesDataSource incomesDataSource = new IncomesDataSource(getActivity());
                incomesDataSource.addIncome(incomeParser.getAmount(), incomeParser.getSource());
            }

            @Override
            public void onHide() {
                // do nothing
            }
        });

        return view;
    }


    @Override
    public void showIncomeTextBox() {
        _txtNewIncome.show();
    }

    @Override
    public void setOnIncomeAddedListener(IIncomeGathererListener listener) {
        _listener = listener;
    }
}
