package ro.birsan.budgetone.util;

/**
 * Created by ibirsan on 8/5/2015.
 */
public class IncomeParser {

    private double _amount;
    private String _source;
    private boolean _isValid = true;

    public IncomeParser(String input) {
        String[] parts = input.split(" ");
        if (parts.length != 2 || !parts[1].startsWith("@")) {
            _isValid = false;
            return;
        }

        try {
            _amount = Double.parseDouble(parts[0]);
        }
        catch (NumberFormatException e) {
            _isValid = false;
            return;
        }

        _source = parts[1].substring(1);
    }

    public double getAmount() {
        return _amount;
    }

    public String getSource() {
        return _source;
    }

    public boolean isValid() {
        return _isValid;
    }
}
