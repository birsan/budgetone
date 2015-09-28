package ro.birsan.budgetone;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoalFragment extends Fragment  implements View.OnClickListener {

    public static final String BUNDLE_ID_KEY = "id";
    public static final String BUNDLE_NAME_KEY = "name";
    public static final String BUNDLE_DESCRIPTION_KEY = "description";
    public static final String BUNDLE_DUE_DATE_KEY = "duedate";
    public static final String BUNDLE_PROGRESS_DESC_KEY = "progress_desc";
    public static final String BUNDLE_TARGET_KEY = "target";
    public static final String BUNDLE_PROGRESS_KEY = "progress";
    public static final String BUNDLE_IMAGE_KEY = "image";

    private OnFragmentInteractionListener _listener;

    ProgressBar _progressBar;
    EditText _etAmount;
    Double _target;
    String _goalId;

    public GoalFragment() {
        // Required empty public constructor
    }

    public static Bundle buildArguments(
            String goalId,
            String name,
            String description,
            String dueDate,
            String progressDescription,
            Double target,
            Double progress,
            byte[] image)
    {
        Bundle args = new Bundle();
        args.putString(GoalFragment.BUNDLE_ID_KEY, goalId);
        args.putString(GoalFragment.BUNDLE_NAME_KEY, name);
        args.putString(GoalFragment.BUNDLE_IMAGE_KEY, description);
        args.putString(GoalFragment.BUNDLE_DUE_DATE_KEY, dueDate);
        args.putString(GoalFragment.BUNDLE_PROGRESS_DESC_KEY, progressDescription);
        args.putDouble(GoalFragment.BUNDLE_TARGET_KEY, target);
        args.putDouble(GoalFragment.BUNDLE_PROGRESS_KEY, progress);
        args.putByteArray(BUNDLE_IMAGE_KEY, image);
        return args;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            _listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void setProgress(Double progress) {
        _progressBar.setProgress(((Double) ((progress * 100) / _target)).intValue());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _goalId = getArguments().getString(BUNDLE_ID_KEY);
        String name = getArguments().getString(BUNDLE_NAME_KEY);
        String description = getArguments().getString(BUNDLE_DESCRIPTION_KEY);
        String dueDate = getArguments().getString(BUNDLE_DUE_DATE_KEY);
        String progressDescription = getArguments().getString(BUNDLE_PROGRESS_DESC_KEY);
        _target = getArguments().getDouble(BUNDLE_TARGET_KEY);
        Double progress = getArguments().getDouble(BUNDLE_PROGRESS_KEY);
        byte[] imageTile = getArguments().getByteArray(BUNDLE_IMAGE_KEY);
        Bitmap bMap = BitmapFactory.decodeByteArray(imageTile, 0, imageTile.length);
        View view = inflater.inflate(R.layout.fragment_goal, container, false);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        TextView tvDueDate = (TextView) view.findViewById(R.id.tvDueDate);
        TextView tvProgress = (TextView) view.findViewById(R.id.tvProgress);
        _progressBar = (ProgressBar) view.findViewById(R.id.progress);
        Button btnSubmitMoney = (Button) view.findViewById(R.id.btnSubmitMoney);
        _etAmount = (EditText) view.findViewById(R.id.etAmount);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        tvName.setText(name);
        tvDescription.setText(description);
        tvDueDate.setText(dueDate);
        tvProgress.setText(progressDescription);
        _progressBar.setMax(100);
        _progressBar.setProgress(((Double) ((progress * 100) / _target)).intValue());
        image.setImageBitmap(bMap);

        btnSubmitMoney.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmitMoney)
        {
            Double amountToSubmit = 0.0;
            try {
                amountToSubmit = Double.valueOf(_etAmount.getText().toString());
            } catch (NumberFormatException e) {
            }

            if (amountToSubmit == 0) {
                Toast.makeText(getActivity(), "Enter an amount grater than 0", Toast.LENGTH_LONG);
                return;
            }

            _listener.onGoalAmountSubmitted(this, _goalId, amountToSubmit);
        }
    }

    public interface OnFragmentInteractionListener
    {
        void onGoalAmountSubmitted(GoalFragment goalFragment, String goalId, Double amount);
    }
}
