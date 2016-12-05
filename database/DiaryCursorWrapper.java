package ie.pdprojects.personaldiary.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import ie.pdprojects.personaldiary.Entry;


// Custom CursorWrapper class
public class DiaryCursorWrapper extends CursorWrapper
{
    public DiaryCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Entry getEntry()
    {

        String uuidString = getString(getColumnIndex(DiaryDBManager.UUID));
        String title = getString(getColumnIndex(DiaryDBManager.TITLE));
        String details = getString(getColumnIndex(DiaryDBManager.DETAILS));
        String date = getString(getColumnIndex(DiaryDBManager.DATE));

        Entry entry = new Entry(UUID.fromString(uuidString));
        entry.setEntryTitle(title);
        entry.setEntryDetails(details);
        entry.setDate(new Date(date));

        return entry;
    }

}