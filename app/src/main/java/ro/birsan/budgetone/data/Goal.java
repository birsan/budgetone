package ro.birsan.budgetone.data;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Irinel on 9/26/2015.
 */
public class Goal {
    private UUID _id;
    private String _name;
    private String _description;
    private byte[] _image;
    private Double _targetAmount;
    private Date _dueDate;
    private Date _createdOn;

    public Goal(UUID _id, String _name, String _description, byte[] _image, Double _targetAmount, Date _dueDate, Date _createdOn) {
        this._id = _id;
        this._name = _name;
        this._description = _description;
        this._image = _image;
        this._targetAmount = _targetAmount;
        this._dueDate = _dueDate;
        this._createdOn = _createdOn;
    }

    public UUID get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_description() {
        return _description;
    }

    public byte[] get_image() {
        return _image;
    }

    public Double get_targetAmount() {
        return _targetAmount;
    }

    public Date get_dueDate() {
        return _dueDate;
    }

    public Date get_createdOn() {
        return _createdOn;
    }
}
