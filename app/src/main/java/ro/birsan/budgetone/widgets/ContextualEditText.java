package ro.birsan.budgetone.widgets;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ibirsan on 8/3/2015.
 */
public class ContextualEditText extends EditText {

    private List<ContextualEditTextActionListener> _listeners = new ArrayList<ContextualEditTextActionListener>();

    public ContextualEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImeOptions(EditorInfo.IME_ACTION_SEND);
        setInputType(InputType.TYPE_CLASS_TEXT);
    }

    public void show()
    {
        if (getVisibility() == View.VISIBLE) return;
        setVisibility(View.VISIBLE);
        requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
    }

    public void setOnActionListener(ContextualEditTextActionListener toAdd) {
        _listeners.add(toAdd);
    }


    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        if (actionCode == EditorInfo.IME_ACTION_SEND) {
            for (ContextualEditTextActionListener listener : _listeners) {
                listener.onSend(getText().toString());
            }
            this.setVisibility(View.INVISIBLE);
            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.setVisibility(View.INVISIBLE);
            for (ContextualEditTextActionListener listener : _listeners) {
                listener.onHide();
            }
            return super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    public interface ContextualEditTextActionListener
    {
        void onSend(String value);
        void onHide();
    }
}
