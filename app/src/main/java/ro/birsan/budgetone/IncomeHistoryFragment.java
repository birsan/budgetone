package ro.birsan.budgetone;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ro.birsan.budgetone.adapters.HistoryAdapter;
import ro.birsan.budgetone.data.Income;
import ro.birsan.budgetone.data.IncomesDataSource;
import ro.birsan.budgetone.viewmodels.HistoryViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeHistoryFragment extends Fragment {

    public IncomeHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        final IncomesDataSource incomesDataSource = new IncomesDataSource(getActivity());
        List<HistoryViewModel> viewModels = new ArrayList<>();
        List<Income> transactions = IncomesDataSource.cursorToList(incomesDataSource.getCurrentMonthIncome());
        for (Income transaction : transactions) {
            viewModels.add(new HistoryViewModel(transaction.get_id(), transaction.get_category(), transaction.get_createdOn(), transaction.get_amount()));
        }

        final ListView listView = (ListView) view.findViewById(R.id.listView);
        final HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(), viewModels);
        listView.setAdapter(historyAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Removing transaction")
                        .setMessage("Are you sure you want to remove this transation?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HistoryViewModel viewModel = historyAdapter.getItem(position);
                                Long transactionId = viewModel.get_transactionId();
                                incomesDataSource.remove(transactionId);
                                historyAdapter.remove(viewModel);
                                historyAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

        return view;
    }
}
