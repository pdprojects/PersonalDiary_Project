package ie.pdprojects.personaldiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;


// same as AboutPopUpActivity

public class EntryPopUpActivity extends AppCompatActivity
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
            fragment = NavEntryFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment) // store fragment in the container
                    .commit();
        }

    } // end onCreate()


}
