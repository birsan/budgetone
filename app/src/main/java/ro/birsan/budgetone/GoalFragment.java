package ro.birsan.budgetone;


import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoalFragment extends Fragment  implements View.OnClickListener {

    public static final String BUNDLE_ID_KEY = "id";
    public static final String BUNDLE_NAME_KEY = "name";
    public static final String BUNDLE_DESCRIPTION_KEY = "description";
    public static final String BUNDLE_DUE_DATE_KEY = "duedate";
    public static final String BUNDLE_TARGET_KEY = "target";
    public static final String BUNDLE_PROGRESS_KEY = "progress";
    public static final String BUNDLE_ADVICE_KEY = "advice";
    public static final String BUNDLE_AVAILABLE_KEY = "available";
    public static final String BUNDLE_IMAGE_KEY = "image";

    private OnFragmentInteractionListener _listener;

    ProgressBar _progressBar;
    TextView tvProgress;
    Double _target;
    Double _progress;
    String _goalId;

    public GoalFragment() {
        // Required empty public constructor
    }

    public static Bundle buildArguments(
            String goalId,
            String name,
            String description,
            String dueDate,
            Double target,
            Double progress,
            String advice,
            String availableDescription,
            byte[] image)
    {
        Bundle args = new Bundle();
        args.putString(GoalFragment.BUNDLE_ID_KEY, goalId);
        args.putString(GoalFragment.BUNDLE_NAME_KEY, name);
        args.putString(GoalFragment.BUNDLE_IMAGE_KEY, description);
        args.putString(GoalFragment.BUNDLE_DUE_DATE_KEY, dueDate);
        args.putDouble(GoalFragment.BUNDLE_TARGET_KEY, target);
        args.putDouble(GoalFragment.BUNDLE_PROGRESS_KEY, progress);
        args.putString(GoalFragment.BUNDLE_ADVICE_KEY, advice);
        args.putString(GoalFragment.BUNDLE_AVAILABLE_KEY, availableDescription);
        args.putByteArray(BUNDLE_IMAGE_KEY, image);
        return args;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            _listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement GoalFragment.OnFragmentInteractionListener");
        }
    }

    public void setProgress(Double progress) {
        tvProgress.setText(getProgressDescription(progress, _target));
        _progressBar.setProgress(((Double) ((progress * 100) / _target)).intValue());
        getArguments().putDouble(BUNDLE_PROGRESS_KEY, progress);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _goalId = getArguments().getString(BUNDLE_ID_KEY);
        String name = getArguments().getString(BUNDLE_NAME_KEY);
        String description = getArguments().getString(BUNDLE_DESCRIPTION_KEY);
        String dueDate = getArguments().getString(BUNDLE_DUE_DATE_KEY);
        _target = getArguments().getDouble(BUNDLE_TARGET_KEY);
        _progress = getArguments().getDouble(BUNDLE_PROGRESS_KEY);
        String advice = getArguments().getString(BUNDLE_ADVICE_KEY);
        String availableDesc = getArguments().getString(BUNDLE_AVAILABLE_KEY);
        byte[] imageTile = getArguments().getByteArray(BUNDLE_IMAGE_KEY);
        Bitmap bMap = BitmapFactory.decodeByteArray(imageTile, 0, imageTile.length);
        View view = inflater.inflate(R.layout.fragment_goal, container, false);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        TextView tvDueDate = (TextView) view.findViewById(R.id.tvDueDate);
        tvProgress = (TextView) view.findViewById(R.id.tvProgress);
        _progressBar = (ProgressBar) view.findViewById(R.id.progress);
        TextView tvAdvice = (TextView) view.findViewById(R.id.tvAdvice);
        TextView tvAvailableForGoals = (TextView) view.findViewById(R.id.tvAvailableForGoals);
        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        Button btnTakeBack = (Button) view.findViewById(R.id.btnTakeBack);
        ImageButton btnRemove = (ImageButton) view.findViewById(R.id.btnRemove);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        tvName.setText(name);
        tvDescription.setText(description);
        tvDueDate.setText(dueDate);
        tvAdvice.setText(advice);
        tvAvailableForGoals.setText(availableDesc);
        if (advice.isEmpty()) {
            tvAvailableForGoals.setVisibility(View.GONE);
        }

        tvProgress.setText(getProgressDescription(_progress, _target));
        _progressBar.setMax(100);
        _progressBar.setProgress(((Double) ((_progress * 100) / _target)).intValue());
        image.setImageBitmap(bMap);

        btnAdd.setOnClickListener(this);
        btnTakeBack.setOnClickListener(this);
        btnRemove.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRemove) {
            new AlertDialog.Builder(getActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Removing goal")
                    .setMessage("Are you sure you want to remove this goal?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            _listener.onGoalRemoved(UUID.fromString(_goalId));
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        if (v.getId() == R.id.btnAdd) {
            final EditText txtInput = new EditText(getActivity());
            txtInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            new AlertDialog.Builder(getActivity())
                    .setTitle("Make Progress Towards Achieving Your Goal!")
                    .setMessage("Add money")
                    .setView(txtInput)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            onGoalAmount(txtInput.getText().toString());
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

        if (v.getId() == R.id.btnTakeBack) {
            final EditText txtInput = new EditText(getActivity());
            txtInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            new AlertDialog.Builder(getActivity())
                    .setTitle("Gear down?")
                    .setMessage("How much do you want to take back?")
                    .setView(txtInput)
                    .setPositiveButton("Take Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            onGoalAmount(txtInput.getText().toString());
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void onGoalAmount(String txtInput)
    {
        Double amountToAdd = 0.0;
        try {
            amountToAdd = Double.valueOf(txtInput);
        } catch (NumberFormatException e) {
        }

        if (amountToAdd == 0) {
            Toast.makeText(getActivity(), "Enter an amount grater than 0", Toast.LENGTH_LONG).show();
            return;
        }

        Double newAmount = _progress + amountToAdd;
        if (newAmount < 0 || newAmount > _target) {
            Toast.makeText(getActivity(), "That's too much", Toast.LENGTH_LONG).show();
            return;
        }

        _listener.onGoalAmountSubmitted(this, _goalId, amountToAdd);
    }

    private String getProgressDescription(Double progress, Double target)
    {
        return progress + " out of " + target;
    }

    public interface OnFragmentInteractionListener
    {
        void onGoalAmountSubmitted(GoalFragment goalFragment, String goalId, Double amount);
        void onGoalRemoved(UUID goalId);
    }
}
