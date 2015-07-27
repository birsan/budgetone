package ro.birsan.budgetone;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Irinel on 7/27/2015.
 */
public class DialogAddBudget extends DialogFragment {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_budget, container, false);
        FragmentEditBudgetItem editBudgetItem = new FragmentEditBudgetItem();
        getChildFragmentManager().beginTransaction().add(R.id.container, editBudgetItem).commit();
        return view;
    }
}
