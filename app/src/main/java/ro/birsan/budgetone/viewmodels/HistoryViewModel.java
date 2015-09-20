package ro.birsan.budgetone.viewmodels;

import java.util.Date;

/**
 * Created by Irinel on 9/18/2015.
 */
public class HistoryViewModel {
    private String _title;
    private Date _createdOn;
    private Double _amount;
    private Long _transactionId;

    public HistoryViewModel(Long _transactionId, String _title, Date _createdOn, Double _amount) {
        this._title = _title;
        this._createdOn = _createdOn;
        this._amount = _amount;
        this._transactionId = _transactionId;
    }

    public String get_title() {
        return _title;
    }

    public Date get_createdOn() {
        return _createdOn;
    }

    public Double get_amount() {
        return _amount;
    }

    public Long get_transactionId() {
        return _transactionId;
    }
}
