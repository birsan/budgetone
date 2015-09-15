package ro.birsan.budgetone;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalculatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CalculatorFragment extends DialogFragment {

    private StringBuilder  _operand1 = new StringBuilder();
    private StringBuilder  _operand2 = new StringBuilder();
    private StringBuilder  _currentOperand = _operand1;
    private String _operator = "";
    private TextView _tvOutput;
    private OnFragmentInteractionListener mListener;

    public CalculatorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        _tvOutput = (TextView)view.findViewById(R.id.tvOutput);
        Button btn0 = (Button)view.findViewById(R.id.btn0);
        Button btn1 = (Button)view.findViewById(R.id.btn1);
        Button btn2 = (Button)view.findViewById(R.id.btn2);
        Button btn3 = (Button)view.findViewById(R.id.btn3);
        Button btn4 = (Button)view.findViewById(R.id.btn4);
        Button btn5 = (Button)view.findViewById(R.id.btn5);
        Button btn6 = (Button)view.findViewById(R.id.btn6);
        Button btn7 = (Button)view.findViewById(R.id.btn7);
        Button btn8 = (Button)view.findViewById(R.id.btn8);
        Button btn9 = (Button)view.findViewById(R.id.btn9);
        Button btnClear = (Button)view.findViewById(R.id.btnClear);
        Button btnAdd = (Button)view.findViewById(R.id.btnAdd);
        Button btnDivide = (Button)view.findViewById(R.id.btnDivide);
        Button btnMultiply = (Button)view.findViewById(R.id.btnMultiplication);
        Button btnMinus = (Button)view.findViewById(R.id.btnMinus);
        Button btnPlus = (Button)view.findViewById(R.id.btnPlus);
        Button btnEqual = (Button)view.findViewById(R.id.btnEquals);
        Button btnPeriod = (Button)view.findViewById(R.id.btnPeriod);

        View.OnClickListener operatorButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_operator.length() != 0 && _operand2.length() == 0) {
                    _operator = ((TextView)v).getText().toString();
                    _currentOperand = _operand2;
                    return;
                }

                computeCurrentOperation();
                updateResultTextView();
                _operator = ((TextView)v).getText().toString();
                _currentOperand = _operand2;
                updateResultTextView();
            }
        };

        View.OnClickListener numericButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _currentOperand.append(((TextView)v).getText().toString());
                updateResultTextView();
            }
        };

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    computeCurrentOperation();
                    mListener.onTransactionAdded(getArguments().getLong("category_id"), Double.valueOf(_operand1.toString()));
                    dismiss();
                }
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _operand1 = new StringBuilder();
                _operand2 = new StringBuilder();
                _currentOperand = _operand1;
                _operator = "";
                updateResultTextView();
            }
        });
        btn0.setOnClickListener(numericButtonListener);
        btn1.setOnClickListener(numericButtonListener);
        btn2.setOnClickListener(numericButtonListener);
        btn3.setOnClickListener(numericButtonListener);
        btn4.setOnClickListener(numericButtonListener);
        btn5.setOnClickListener(numericButtonListener);
        btn6.setOnClickListener(numericButtonListener);
        btn7.setOnClickListener(numericButtonListener);
        btn8.setOnClickListener(numericButtonListener);
        btn9.setOnClickListener(numericButtonListener);
        btnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!_currentOperand.toString().contains(".")) {
                    _currentOperand.append(".");
                    updateResultTextView();
                }
            }
        });
        btnDivide.setOnClickListener(operatorButtonListener);
        btnMultiply.setOnClickListener(operatorButtonListener);
        btnMinus.setOnClickListener(operatorButtonListener);
        btnPlus.setOnClickListener(operatorButtonListener);
        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                computeCurrentOperation();
                updateResultTextView();
            }
        });

        return  view;
    }

    private void updateResultTextView()
    {
        String display = "";
        if (_operand1.length() != 0) display += _operand1;
        if (_operator.length() != 0) display += " " +  _operator + " ";
        if (_operand2.length() != 0) display += _operand2;
        _tvOutput.setText(display);
    }

    private void computeCurrentOperation() {
        if (_operand1.length() == 0 || _operator.length() == 0 || _operand2.length() == 0) return;

        Double result = 0.0;
        Double d1 = Double.valueOf(_operand1.toString());
        Double d2 = Double.valueOf(_operand2.toString());

        if (_operator.equals("/")) result = d1 / d2;
        if (_operator.equals("*")) result = d1 * d2;
        if (_operator.equals("+")) result = d1 + d2;
        if (_operator.equals("-")) result = d1 - d2;

        _operand1 = new StringBuilder(String.valueOf(result));
        _operand2 = new StringBuilder();
        _currentOperand = _operand1;
        _operator = "";
        updateResultTextView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onTransactionAdded(long categoryId, Double amount);
    }

}
