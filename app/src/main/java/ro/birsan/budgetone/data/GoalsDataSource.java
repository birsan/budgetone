package ro.birsan.budgetone.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ro.birsan.budgetone.util.DateTimeHelper;

/**
 * Created by Irinel on 9/26/2015.
 */
public class GoalsDataSource extends DataSourceBase {
    public static final String TABLE_NAME = "goals";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_TARGET_AMOUNT = "target_amount";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_CREATED_ON = "created_on";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME
            + " (" + COLUMN_ID + " text primary key, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_DESCRIPTION + " text not null, "
            + COLUMN_IMAGE + " blob null, "
            + COLUMN_TARGET_AMOUNT + " real not null, "
            + COLUMN_DUE_DATE + " text null, " //ISO8601 string format
            + COLUMN_CREATED_ON + " text null " //ISO8601 string format
            + ");";

    public GoalsDataSource(Context context) {
        super(context);
    }

    public void createGoal(Goal goal)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, goal.get_id().toString());
        values.put(COLUMN_NAME, goal.get_name());
        values.put(COLUMN_DESCRIPTION, goal.get_description());
        values.put(COLUMN_IMAGE, goal.get_image());
        values.put(COLUMN_TARGET_AMOUNT, goal.get_targetAmount());
        if (goal.get_dueDate() != null)
        {
            values.put(COLUMN_DUE_DATE, DateTimeHelper.ISO8601DateFormat.format(goal.get_dueDate()));
        }

        values.put(COLUMN_CREATED_ON, DateTimeHelper.ISO8601DateFormat.format(goal.get_createdOn()));
        _writableDatabase.insert(TABLE_NAME, null, values);
    }

    public Goal getGoal(UUID goalId) {
        Goal goal = null;
        Cursor cursor = _readableDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + goalId.toString() + ";", null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast())
            goal = cursorToGoal(cursor);

        cursor.close();
        return goal;
    }

    public List<Goal> getAllGoals()
    {
        List<Goal> goals = new ArrayList<>();
        Cursor cursor = _readableDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " ;", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            goals.add(cursorToGoal(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return goals;
    }

    public void deleteGoal(UUID goalId) {
        _writableDatabase.delete(TABLE_NAME, COLUMN_ID + " = ? ", new String[]{goalId.toString()});
    }

    private static final Goal cursorToGoal(Cursor cursor) {
        UUID id = UUID.fromString(cursor.getString(0));
        String name = cursor.getString(1);
        String description = cursor.getString(2);
        byte[] image = cursor.getBlob(3);
        Double targetAmount = cursor.getDouble(4);
        Date dueDate = null;
        Date createdOn = null;
        try {
            String dueDateString = cursor.getString(6);
            if (dueDateString != null)
            {
                dueDate = DateTimeHelper.ISO8601DateFormat.parse(dueDateString);
            }

            createdOn = DateTimeHelper.ISO8601DateFormat.parse(cursor.getString(7));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Goal(id, name, description, image, targetAmount, dueDate, createdOn);
    }
}
