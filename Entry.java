package ie.pdprojects.personaldiary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


// Most basic model class in the project, used in Entry collection as list of objects

public class Entry
{
    private UUID mEntryId;   // Unique id
    private String mEntryTitle; // Title
    private String mEntryDetails; // Note
    private Date mDate;
    private SimpleDateFormat mDateFormat;


    // Assign unique id
    public Entry()
    {
        this(UUID.randomUUID()); // random id

    }

    public Entry(UUID uuid)
    {
        mEntryId = uuid;
        mDate = new Date(); // current date

    }

    public UUID getEntryId()
    {
        return mEntryId;
    }

    public String getEntryTitle()
    {
        return mEntryTitle;
    }

    public void setEntryTitle(String noteTitle)
    {
        mEntryTitle = noteTitle;
    }

    public String getEntryDetails()
    {
        return mEntryDetails;
    }

    public void setEntryDetails(String noteDetails)
    {
        mEntryDetails = noteDetails;
    }

    public String getDate()
    {

        mDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return  mDateFormat.format(mDate);




    }

    public void setDate(Date date)
    {
        mDate = date;
    }

    public String getPhotoName()
    {
        return "IMG__" + getEntryId().toString() + ".jpg";
    }
}
