package ro.birsan.budgetone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ro.birsan.budgetone.data.GoalsDataSource;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.services.GoalsService;


public class AddGoalActivity extends ActionBarActivity {
    private static final int REQUEST_CODE = 1;

    private ImageView _image;
    private Bitmap _bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_target);

        final EditText etDueDate = (EditText)findViewById(R.id.etDueDate);
        etDueDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != MotionEvent.ACTION_UP) return true;

                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                }

                etDueDate.requestFocus();
                Calendar cal = Calendar.getInstance();
                new DatePickerDialog(
                        AddGoalActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                                Calendar myCalendar = Calendar.getInstance();
                                myCalendar.set(Calendar.YEAR, selectedYear);
                                myCalendar.set(Calendar.MONTH, selectedMonth);
                                myCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                                SimpleDateFormat sdf = new SimpleDateFormat();
                                etDueDate.setText(sdf.format(myCalendar.getTime()));
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
                return true;
            }
        });

        _image = ((ImageView)findViewById(R.id.image));
        Button btnSetImage = (Button)findViewById(R.id.btnSetImage);
        btnSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_activity_add_target);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.check);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {
                // We need to recycle unused bitmaps
                if (_bitmap != null) {
                    _bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                _bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
                _image.setImageBitmap(_bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            String name = ((EditText)findViewById(R.id.etName)).getText().toString();
            String description = ((EditText)findViewById(R.id.etDescription)).getText().toString();
            Double targetAmount = 0.0;
            try
            {
                targetAmount = Double.valueOf(((EditText) findViewById(R.id.etTargetAmount)).getText().toString());
            }
            catch (NumberFormatException e){}
            if (!validateInput(name, targetAmount)) return true;

            GoalsService goalsService = new GoalsService(new GoalsDataSource(this), new TransactionsDataSource(this));

            _image.setDrawingCacheEnabled(true);
            _image.buildDrawingCache();
            Bitmap bm = _image.getDrawingCache();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();

            Date dueDate = null;
            try {
                dueDate = new SimpleDateFormat().parse(((EditText) findViewById(R.id.etDueDate)).getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            goalsService.createGoal(name, description, image, targetAmount, dueDate);
            goalsService.close();
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean validateInput(String name, Double target)
    {
        if (name == null || name.isEmpty()) {
            Toast.makeText(this, "A goal needs a name!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (target <= 0)
        {
            Toast.makeText(this, "A goal with target zero is already accomplished!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
