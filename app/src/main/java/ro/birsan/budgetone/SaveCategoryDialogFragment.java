package ro.birsan.budgetone;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Irinel on 7/22/2015.
 */
public class SaveCategoryDialogFragment extends DialogFragment {

    private SaveCategoryDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.dialog_save_category)
                .setMessage(R.string.save_category_dialog_title)
                .setPositiveButton(R.string.btn_save_category, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String categoryName = ((EditText)((Dialog)dialog).findViewById(R.id.category_name)).getText().toString();
                        mListener.onSaveCategory(0, categoryName);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.save_category_dialog_cancel_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SaveCategoryDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SaveCategoryDialogListener");
        }
    }

    public interface SaveCategoryDialogListener {
        void onSaveCategory(int id, String name);
    }
}
