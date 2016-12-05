package ie.pdprojects.personaldiary;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

public class PersonalDiaryActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_diary);

        // Call fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Create new fragment
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        // Create new fragment if does not already exists
        if (fragment == null)
        {
            // Get note id from starting intent
            UUID noteID = (UUID) getIntent()
                    .getSerializableExtra(MyAdapter.DiaryHolder.ENTRY_ID);
            // call static newInstance and attach noteID in argument
            fragment = DiaryFragment.newInstance(noteID);

            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment) // store fragment in the container
                    .commit();
        }

    } // end onCreate()


}
