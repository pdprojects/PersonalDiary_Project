package ie.pdprojects.personaldiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.List;

import jp.wasabeef.blurry.Blurry;


public class DiaryListFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener
{
    private RecyclerView mNoteRecyclerView;
    private MyAdapter mAdapter;
    public static Context context;
    private FloatingActionButton mFloatingActionButton;
    private LinearLayoutManager mLayoutManager;
    public ImageView mImageView;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mToogle;
    private Toolbar mToolbar;
    private List<Entry> entries;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.fragment_list, container, false);

        // Custom  toolbar as the theme was set to NoActionBar(see style.xml)
        mToolbar = (Toolbar)view.findViewById(R.id.navigation_actionbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);




        // side menu
        mNavigationView = (NavigationView) view.findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {

                // put the main thread to sleep for 0.4 sec to close the drawer and blur the
                // background otherwise the background will be blurred with the drawer half open
                mDrawerLayout.closeDrawers();
                try
                {
                    Thread.sleep(400);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                // Drawer menu
                switch (menuItem.getItemId()){

                    // if 'Entries' clicked get the background blurred  and display the fragment
                    case R.id.nav_entry:
                        getBlurred();
                        Intent i = new Intent(getContext(), EntryPopUpActivity.class);
                        startActivity(i);

                        return true;
                    case R.id.nav_calendar:

                        // Display calendar alert dialog
                        FragmentManager manager = getFragmentManager();
                        CalendarFragment dialog = new CalendarFragment();
                        dialog.show(manager, "Calendar");
                        return true;

                    //Same as nav entry
                    case R.id.nav_about:
                        getBlurred();
                        Intent j = new Intent(getContext(), AboutPopUpActivity.class);
                        startActivity(j);
                        return true;


                    default:
                        Toast.makeText(getContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        mDrawerLayout = (DrawerLayout)view.findViewById(R.id.fragment_list_frame_layout);
        mToogle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,R.string.open,
                R.string.close);
        mDrawerLayout.addDrawerListener(mToogle);

        mToogle.syncState();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout,mToolbar,R.string.open, R.string.close){

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Refresh entries no when drawer opened
                MenuItem menu = mNavigationView.getMenu().findItem(R.id.nav_entry);
                menu.setTitle("Entries (" + Integer.toString(entries.size())+")");
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);



        // Recycler view
        mNoteRecyclerView = (RecyclerView) view
                .findViewById(R.id.note_recycler_view);
        mNoteRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mNoteRecyclerView.setLayoutManager( mLayoutManager);
        mLayoutManager.setReverseLayout(true);



        // Create new entry if the fab was pressed
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fa_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Entry entry = new Entry();
                EntryCollection.get(getActivity()).addEntry(entry);
                Intent i = new Intent(getContext(), PersonalDiaryActivity.class);
                i.putExtra(MyAdapter.DiaryHolder.ENTRY_ID, entry.getEntryId());
                startActivity(i);


            }
        });

        // update recycler view in onCreate or onResume
        updateUI();

        return view;

    }


    // inflating a menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
    {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_list, menu);
    }

    // respond to menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {


        // Hamburger icon listener
        if(mToogle.onOptionsItemSelected(menuItem))
        {
            return true;

        }
        // + icon, same action as fab
        switch (menuItem.getItemId())
        {
            case R.id.new_note:

                Entry entry = new Entry();
                EntryCollection.get(getActivity()).addEntry(entry);
                Intent i = new Intent(getContext(), PersonalDiaryActivity.class);
                i.putExtra(MyAdapter.DiaryHolder.ENTRY_ID, entry.getEntryId());
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);

        }
    }

    // Override onResume() to delete the blurr,
    @Override
    public void onResume()
    {
        super.onResume();
        updateUI(); // call updateUI to see if any changes
        Blurry.delete((ViewGroup)view.findViewById(R.id.fragment_list_frame_layout));

        // Scroll down to last created item
        mNoteRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                // Call smooth scroll
                mNoteRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }
        });
    }

    private void updateUI()
    {
        //NotePad notePad = NotePad.get(getActivity());
        //List<Note> notes = notePad.getNotes();
        entries = EntryCollection.get(getActivity()).getNotes();


        if(mAdapter == null) // if onCreate()
        {
            mAdapter = new MyAdapter(entries, getActivity() );
            mNoteRecyclerView.setAdapter(mAdapter);
        }
        else // else onResume()
        {
            mAdapter.notifyDataSetChanged();
            mAdapter.updateMyadapter(entries);

        }

    }

    // long hold context menu
    @Override
    public boolean onContextItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.id_delete:
                EntryCollection.get(getActivity()).deleteEntry(MyAdapter.DiaryHolder.mUUID);
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                updateUI();
                return true;


            default:
                return super.onContextItemSelected(menuItem);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        return false;
    }

    // blurred method called twice in nav drawer
    public void getBlurred()
    {


        Blurry.with(getActivity())
                .radius(10)
                .sampling(8)
                .async()
                .animate(500)
                .onto((ViewGroup)view.findViewById(R.id.fragment_list_frame_layout));


    }


}
