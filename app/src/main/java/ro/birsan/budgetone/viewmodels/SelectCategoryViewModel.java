package ro.birsan.budgetone.viewmodels;

/**
 * Created by Irinel on 8/30/2015.
 */
public class SelectCategoryViewModel {
    private String _name;
    private long _id;
    private Boolean _selected;
    private Boolean _originalSelected;

    public SelectCategoryViewModel(long id, String name, Boolean selected) {
        _id = id;
        _name = name;
        _selected = selected;
        _originalSelected = _selected;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public Boolean getSelected() {
        return _selected;
    }

    public void setSelected(Boolean _selected) {
        this._selected = _selected;
    }

    public Boolean getOriginalSelected() {
        return _originalSelected;
    }
}
