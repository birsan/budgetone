package ro.birsan.budgetone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Irinel on 7/30/2015.
 */
public abstract class DataSourceBase {
    protected MySQLiteHelper _dbHelper;
    protected SQLiteDatabase _writableDatabase;
    protected SQLiteDatabase _readableDatabase;

    public DataSourceBase(Context context) {
        _dbHelper = new MySQLiteHelper(context);
        _writableDatabase = _dbHelper.getWritableDatabase();
        _readableDatabase = _dbHelper.getReadableDatabase();
    }
}
