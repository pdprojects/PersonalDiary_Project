package ie.pdprojects.personaldiary;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

import jp.wasabeef.blurry.Blurry;

public class NavEntryFragment extends Fragment
{
    private List<Entry> mEntries;

    private TextView mTextView;


    public NavEntryFragment()
    {
        // Required empty public constructor
    }

    public static NavEntryFragment newInstance()
    {
        NavEntryFragment fr = new NavEntryFragment();

        return fr;

    }




    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.nav_entry_fragment, container, false);

        mTextView = (TextView)view.findViewById(R.id.nav_entry_text);
        mTextView.setGravity(Gravity.CENTER);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        mEntries = EntryCollection.get(getActivity()).getNotes();
        if(mEntries.size()!=0)
        {
            Entry firstEntry = mEntries.get(0);
            Entry lastEntry = mEntries.get(mEntries.size() - 1);


            mTextView.setText("Total entries: " + mEntries.size() + "\n\n" +
                    "First entry date: " + firstEntry.getDate() + "\n\n" +
                    "Last entry date: " + lastEntry.getDate());
        }
        else
        {
            mTextView.setText("No entries");

        }



        getActivity().getWindow().setLayout((int)(width*.8), (int)(height*.4));

        return view;
    }


}
