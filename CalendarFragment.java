package ie.pdprojects.personaldiary;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;


// Calendar fragment


public class CalendarFragment extends DialogFragment
{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.calendar_dialog, null);


        return new AlertDialog.Builder(getActivity())
                .setTitle("Calendar")
                .setView(v)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
