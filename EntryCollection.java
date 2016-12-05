package ie.pdprojects.personaldiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ie.pdprojects.personaldiary.database.DiaryCursorWrapper;
import ie.pdprojects.personaldiary.database.DiaryDBManager;



// This singleton class will store the list of  the Entry classes
// Its private constructor guarantees thread safety
// and that the db will be opened only once
public class EntryCollection
{
    private static EntryCollection sEntryCollection;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static EntryCollection get(Context context)
    {
        if (sEntryCollection == null)
        {
            sEntryCollection = new EntryCollection(context);
        }
        return sEntryCollection;
    }

    private EntryCollection(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new DiaryDBManager(mContext)
                .getWritableDatabase();


    }

    public void addEntry(Entry entry)
    {
        ContentValues contentValues = getContentValues(entry);

        mDatabase.insert(DiaryDBManager.NAME, null, contentValues);
    }


    public void deleteEntry(UUID uuid)
    {

        mDatabase.delete(DiaryDBManager.NAME, DiaryDBManager.UUID + " = ?",
                new String[] {uuid.toString()});
    }

    public List<Entry> getNotes()
    {
        List<Entry> notes = new ArrayList<>();

        DiaryCursorWrapper cursorWrapper = queryEntries(null,null);
        try
        {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast())
            {
                notes.add(cursorWrapper.getEntry());
                cursorWrapper.moveToNext();
            }
        }
        finally
        {
            cursorWrapper.close();
        }


        return notes;
    }


    public Entry getEntry(UUID uuid)
    {

        DiaryCursorWrapper cursorWrapper = queryEntries(
                DiaryDBManager.UUID + " = ?",
                new String[] {uuid.toString()}
        );

        try
        {
            if (cursorWrapper.getCount() == 0)
            {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getEntry();
        }
        finally
        {
            cursorWrapper.close();
        }
    }

    public File getPhotoFile(Entry entry)
    {


        File externalDirectory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"Diary"); // Gallery folder name

        // Check if there is external storage to sava file
        if(!externalDirectory.exists())//check if already exists
        {
            externalDirectory.mkdirs();//if not, create it
        }
        return new File(externalDirectory, entry.getPhotoName());


    }

    public void updateEntries(Entry entry)
    {
        String uuidString = entry.getEntryId().toString();
        ContentValues contentValues = getContentValues(entry);

        mDatabase.update(DiaryDBManager.NAME, // Table name
                contentValues, // Data
                DiaryDBManager.UUID  + " = ?", // Where row is equal to String[] uuid
                new String[]{uuidString});
    }

    // Write in - this method deals with database writes/updates
    private static ContentValues getContentValues(Entry entry)
    {
        // Create key-value instance of ContentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put(DiaryDBManager.UUID, entry.getEntryId().toString());
        contentValues.put(DiaryDBManager.TITLE, entry.getEntryTitle());
        contentValues.put(DiaryDBManager.DETAILS, entry.getEntryDetails());
        contentValues.put(DiaryDBManager.DATE, entry.getDate().toString());

        return contentValues;

    }

    // Read in data
    //private Cursor queryNotes(String whereClause, String[] whereArgs)
    private DiaryCursorWrapper queryEntries(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                DiaryDBManager.NAME,
                null, // Columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        // Return custom CursorWrapper class(stored in db folder)
        return new DiaryCursorWrapper(cursor);
    }
}
