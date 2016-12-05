package ie.pdprojects.personaldiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;
import java.util.UUID;

// RecyclerView widget class
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.DiaryHolder>
{

    private static Context mContext;
    private List <Entry> mEntries;


    public static class DiaryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener
    {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mImageView;
        private File mPhotoFile;
        private Entry mEntry;
        protected static UUID mUUID;
        protected static final String ENTRY_ID =
                "ie.pdprojects.personaldiary";

        public DiaryHolder(View v)
        {
            super(v);
            v.setOnClickListener(this);
            v.setOnCreateContextMenuListener(this);
            mTitleTextView = (TextView)v.findViewById(R.id.notes_list_title);
            mDateTextView = (TextView)v.findViewById(R.id.notes_list_date);
            mImageView = (ImageView)v.findViewById(R.id.picture_cardview);
        }

        @Override
        public void onClick(View view)
        {
            // Start PhotonActivity from PhotonListFragment using mContext
            Intent i = new Intent(mContext, PersonalDiaryActivity.class);
            i.putExtra(ENTRY_ID, mEntry.getEntryId());
            mContext.startActivity(i);
        }

        public void bindNote(Entry entry)
        {

            mEntry = entry;
            mTitleTextView.setText(mEntry.getEntryTitle());
            mDateTextView.setText(mEntry.getDate());
            mPhotoFile = EntryCollection.get(mContext).getPhotoFile(mEntry);

            // ImageManager is my custom class for dealing with pictures it works perfectly in a single screen
            // but not so good in the recycler view. Picasso is similar to Glide(even the syntax is the same)
            //mImageView.setImageBitmap(ImageManager.setPic(mPhotoFile, mImageView));
            // Picasso.with(mContext).load(mPhotoFile).into(mImageView);

            /* This setting disables cache(so the new pictures can be uploaded or if
                Personal Diary folder is deleted they wont show up) but it affects the performance
            Glide.with(mContext)
                    .load(mPhotoFile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // no disk strategy
                    .skipMemoryCache(true) // old images wont be kept in cache
                    .centerCrop()
                    .into(mImageView);
            */
            // This setting keeps the old pictures in cache(even if deleted)
            // but increases the performance of the cardview
            Glide.with(mContext)
                    .load(mPhotoFile)
                    .centerCrop()
                    .into(mImageView);

        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
        {

            contextMenu.setHeaderTitle(mEntry.getEntryTitle());
            mUUID = mEntry.getEntryId();
            Activity activity = (Activity) mContext;
            MenuInflater menuInflater = activity.getMenuInflater();
            menuInflater.inflate(R.menu.context_menu, contextMenu);
        }
    } // end static NotesHolder class


    // get Data when fragment created
    public MyAdapter(List<Entry> notes, Context context)
    {
        mEntries= notes;
        mContext = context;

    }
    // update date when onResume()
    public void updateMyadapter(List<Entry> entries)
    {
        mEntries = entries;

    }
    @Override
    public MyAdapter.DiaryHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_card,parent,false);
        DiaryHolder dh = new DiaryHolder(view);


        return dh;

    }

    @Override
    public void onBindViewHolder(DiaryHolder holder, int position)
    {
        Entry entry = mEntries.get(position);
        holder.bindNote(entry);
    }

    @Override
    public int getItemCount()
    {
        return mEntries.size();

    }
}
