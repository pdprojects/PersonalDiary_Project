package ie.pdprojects.personaldiary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// Datebase class
public class DiaryDBManager extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "notes.db";
    public static final String NAME = "note";
    public static final String UUID = "uuid";
    public static final String TITLE = "title";
    public static final String DETAILS = "details";
    public static final String DATE = "date";

    public DiaryDBManager(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);

    }

    // Create new database
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL("create table " + NAME
                + "("  + "_id integer primary key autoincrement, " +
                UUID + ", " +
                TITLE + ", " +
                DETAILS + ", " +
                DATE + ")"
        );
    }
    // Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}

