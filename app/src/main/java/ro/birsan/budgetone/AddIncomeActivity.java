package ro.birsan.budgetone;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import ro.birsan.budgetone.data.IncomesDataSource;


public class AddIncomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        IncomesDataSource incomesDataSource = new IncomesDataSource(this);
        AutoCompleteTextView categoryTextView = (AutoCompleteTextView)findViewById(R.id.category);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,incomesDataSource.getCategories());
        categoryTextView.setAdapter(adapter);
        incomesDataSource.close();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_activity_add_income);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.check);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.check);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            IncomesDataSource incomesDataSource = new IncomesDataSource(this);
            String category = ((AutoCompleteTextView)findViewById(R.id.category)).getText().toString();
            if (category.matches("")){
                Toast.makeText(this, "You didn't specify the category", Toast.LENGTH_LONG).show();
                return true;
            }
            String amountAsText = ((EditText)findViewById(R.id.amount)).getText().toString();
            if (category.matches("")){
                Toast.makeText(this, "You didn't specify the amount", Toast.LENGTH_LONG).show();
                return true;
            }
            Double amount = Double.parseDouble(amountAsText);
            if (amount <= 0){
                Toast.makeText(this, "The amount needs to be positive", Toast.LENGTH_LONG).show();
                return true;
            }
            
            incomesDataSource.addIncome(amount, category);
            incomesDataSource.close();
        }

        return super.onOptionsItemSelected(item);
    }
}
