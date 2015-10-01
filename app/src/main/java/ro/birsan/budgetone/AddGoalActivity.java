package ro.birsan.budgetone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;

import ro.birsan.budgetone.data.GoalsDataSource;
import ro.birsan.budgetone.data.TransactionsDataSource;
import ro.birsan.budgetone.services.GoalsService;
import ro.birsan.budgetone.util.DateTimeHelper;


public class AddGoalActivity extends ActionBarActivity {
    private static final int REQUEST_CODE = 1;

    private ImageView _image;
    private Bitmap _bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_target);

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
                dueDate = DateTimeHelper.ISO8601DateFormat.parse(((EditText) findViewById(R.id.etDueDate)).getText().toString());
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
