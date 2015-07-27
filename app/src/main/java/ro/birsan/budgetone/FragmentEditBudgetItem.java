package ro.birsan.budgetone;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irinel on 7/27/2015.
 */
public class FragmentEditBudgetItem extends Fragment {
    private SeekBar Seekbar = null;
    private RelativeLayout RelativeLayout = null;

    public FragmentEditBudgetItem() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_budget_item, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIntervals(new ArrayList<String>() {{
            add("1");
            add("aaa");
            add("3");
            add("bbb");
            add("5");
            add("ccc");
            add("7");
            add("ddd");
            add("9");
        }});
    }

    public void setIntervals(List<String> intervals) {
        displayIntervals(intervals);
        getSeekbar().setMax(intervals.size() - 1);
    }

    private void displayIntervals(List<String> intervals) {
        int idOfPreviousInterval = 0;

        if (getRelativeLayout().getChildCount() == 0) {
            for (String interval : intervals) {
                TextView textViewInterval = createInterval(interval);
                alignTextViewToRightOfPreviousInterval(textViewInterval, idOfPreviousInterval);

                idOfPreviousInterval = textViewInterval.getId();

                getRelativeLayout().addView(textViewInterval);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private TextView createInterval(String interval) {
        EditText textBoxView = new EditText(getActivity());
        textBoxView.setId(View.generateViewId());
        textBoxView.setText(interval);

        return textBoxView;
    }

    private void alignTextViewToRightOfPreviousInterval(TextView textView, int idOfPreviousInterval) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (idOfPreviousInterval > 0) {
            params.addRule(RelativeLayout.RIGHT_OF, idOfPreviousInterval);
        }

        textView.setLayoutParams(params);
    }

    private RelativeLayout getRelativeLayout() {
        if (RelativeLayout == null) {
            RelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.intervals);
        }

        return RelativeLayout;
    }

    private SeekBar getSeekbar() {
        if (Seekbar == null) {
            Seekbar = (SeekBar) getActivity().findViewById(R.id.seekbar);
        }

        return Seekbar;
    }
}
