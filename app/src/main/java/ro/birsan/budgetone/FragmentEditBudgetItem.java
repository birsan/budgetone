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

import ro.birsan.budgetone.widgets.CategoryBudgetSeekBar;

/**
 * Created by Irinel on 7/27/2015.
 */
public class FragmentEditBudgetItem extends Fragment {
    private CategoryBudgetSeekBar Seekbar = null;
    private RelativeLayout RelativeLayout = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_budget_item, container, false);
        RelativeLayout = (RelativeLayout) view.findViewById(R.id.intervals);
        Seekbar = (CategoryBudgetSeekBar) view.findViewById(R.id.seekbar);
        setIntervals(new ArrayList<String>() {{
            add("1");
            add("3");
            add("5");
            add("7");
            add("9");
        }});
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setIntervals(List<String> intervals) {
        displayIntervals(intervals);
        Seekbar.setMax(intervals.size() - 1);
    }

    private void displayIntervals(List<String> intervals) {
        int idOfPreviousInterval = 0;

        if (RelativeLayout.getChildCount() == 0) {
            for (String interval : intervals) {
                TextView textViewInterval = createInterval(interval);
                alignTextViewToRightOfPreviousInterval(textViewInterval, idOfPreviousInterval);

                idOfPreviousInterval = textViewInterval.getId();

                RelativeLayout.addView(textViewInterval);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private TextView createInterval(String interval) {
        TextView textBoxView = new TextView(getActivity());
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
}
