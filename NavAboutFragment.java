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

public class NavAboutFragment extends Fragment
{
    private List<Entry> mEntries;

    private TextView mTextView;

    public NavAboutFragment()
    {
        // Required empty public constructor
    }

    public static NavAboutFragment newInstance()
    {

        NavAboutFragment fr = new NavAboutFragment();
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
        View view = inflater.inflate(R.layout.nav_about_fragment, container, false);

        mTextView = (TextView)view.findViewById(R.id.nav_about_text);
        mTextView.setGravity(Gravity.CENTER);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        mTextView.setText("Description: Personal Diary Application" +
                "\nModule: Mobile Software Development" +
                "\nName: Patrycjusz Duszek" +
                "\nCourse: DT211C/3"+
                "\nStudent no.: C14731771");



        getActivity().getWindow().setLayout((int)(width*.8), (int)(height*.4));

        return view;
    }


}
