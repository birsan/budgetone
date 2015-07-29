package ro.birsan.budgetone.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by Irinel on 7/28/2015.
 */
public class CategoryBudgetSeekBar extends SeekBar {
    /*private Bitmap _labelBackground;
    private int _viewWidth, _barHeight, _labelOffset;;
    private Rect _barBounds, _labelTextRect;
    private float _progressPosX;
    private Point _labelPos;
    private Drawable _progressDrawable;
    private Paint _labelTextPaint, _labelBackgroundPaint;*/

    public CategoryBudgetSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*public CategoryBudgetSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        _progressDrawable = getProgressDrawable();

        _labelTextPaint = new Paint();
        _labelTextPaint.setColor(Color.WHITE);
        _labelTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        _labelTextPaint.setAntiAlias(true);
        _labelTextPaint.setDither(true);
        _labelTextPaint.setTextSize(13f);

        _labelBackgroundPaint = new Paint();

        _barBounds = new Rect();
        _labelTextRect = new Rect();

        _labelPos = new Point();
    }*/

    /*@Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //if (_labelBackground != null)
        //{
            _viewWidth = getMeasuredWidth();
            _barHeight = getMeasuredHeight();// returns only the bar height (without the label);
            //setMeasuredDimension(_viewWidth, _barHeight + _labelBackground.getHeight());
        setMeasuredDimension(_viewWidth, _barHeight + 15);
        //}
    }*/

    /*@Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        //if (_labelBackground != null) {
            _barBounds.left = getPaddingLeft();
            //_barBounds.top = _labelBackground.getHeight() + getPaddingTop();
            _barBounds.top = 15 + getPaddingTop();
            _barBounds.right = _barBounds.left + _viewWidth - getPaddingRight() - getPaddingLeft();
            _barBounds.bottom = _barBounds.top + _barHeight - getPaddingBottom() - getPaddingTop();

            _progressPosX = _barBounds.left + ((float) this.getProgress() / (float) this.getMax()) * _barBounds.width();

            _labelPos.x = (int) _progressPosX - _labelOffset;
            _labelPos.y = getPaddingTop();

            _progressDrawable = getProgressDrawable();
            _progressDrawable.setBounds(_barBounds.left, _barBounds.top, _barBounds.right, _barBounds.bottom);
            _progressDrawable.draw(canvas);

            String pro = getProgress() + "";
            _labelTextPaint.getTextBounds(pro, 0, pro.length(), _labelTextRect);

            //canvas.drawBitmap(_labelBackground, _labelPos.x, _labelPos.y, _labelBackgroundPaint);
            //canvas.drawText(pro, _labelPos.x + _labelBackground.getWidth() / 2 - _labelTextRect.width() / 2, _labelPos.y + _labelBackground.getHeight() / 2 + _labelTextRect.height() / 2, _labelTextPaint);
            canvas.drawText(pro, _labelPos.x + 25 - _labelTextRect.width() / 2, _labelPos.y + 25 + _labelTextRect.height() / 2, _labelTextPaint);

            int thumbX = (int) _progressPosX - getThumbOffset();
        _progressDrawable.setBounds(thumbX, _barBounds.top, thumbX + _progressDrawable.getIntrinsicWidth(), _barBounds.top + _progressDrawable.getIntrinsicHeight());
            _progressDrawable.draw(canvas);
        //} else {
            //super.onDraw(canvas);
        //}
    }*/

    /*public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }*/
}
