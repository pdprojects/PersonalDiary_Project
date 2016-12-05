package ie.pdprojects.personaldiary;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;



public class DiaryFragment extends Fragment
{

    private Entry mEntry;
    private EditText mTextTitle;
    private EditText mTextDetails;
    private ImageView mImageView;
    private File mPhotoFile;
    private float mTargetW;
    private float mTargetH;
    private FloatingActionButton mFloatingActionButtonCamera;
    private FloatingActionButton mFloatingActionButtonGallery;
    private FloatingActionButton mFloatingActionButtonShare;
    private Toolbar mToolbar;

    boolean isImageFitToScreen;


    private static final String ARGUMENT_NOTE_ID =
            "ie.pdprojects.personaldiary";
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_GALLERY = 1;



    // This method is called in PersonalDiaryActivity, just before
    // fragment is added to the activity, to attach arguments
    public static DiaryFragment newInstance(UUID noteID)
    {
        // Create Bundle object to stash uuid
        Bundle argument = new Bundle();
        argument.putSerializable(ARGUMENT_NOTE_ID, noteID);

        DiaryFragment fr = new DiaryFragment();
        fr.setArguments(argument);
        return fr;

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Retrieve the extra from the fragment arguments
        // which were attached in PersonalDiaryActivity using DiaryFragment.newInstance -- see above
        UUID intentNoteID = (UUID) getArguments().getSerializable(ARGUMENT_NOTE_ID);
        mEntry = EntryCollection.get(getActivity()).getEntry(intentNoteID);
        mPhotoFile = EntryCollection.get(getActivity()).getPhotoFile(mEntry);
    }

    // Update database
    @Override
    public void onPause()
    {
        super.onPause();

        EntryCollection.get(getActivity()).updateEntries(mEntry);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_entry, container, false);

        // Custom toolbar
        mToolbar = (Toolbar)view.findViewById(R.id.navigation_actionbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        //Enabling Hierarchical Navigation
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        mTextTitle = (EditText)view.findViewById(R.id.entry_title);
        mTextTitle.setText(mEntry.getEntryTitle());
        // Entry title listener
        mTextTitle.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                mEntry.setEntryTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });


        mTextDetails = (EditText)view.findViewById(R.id.note_details);
        mTextDetails.setText(mEntry.getEntryDetails());
        // Entry details listener
        mTextDetails.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                mEntry.setEntryDetails(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        mFloatingActionButtonCamera = (FloatingActionButton) view.findViewById(R.id.fab_menu_camera);

        // Create implicit intent to call camera
        final Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        Uri uri = Uri.fromFile(mPhotoFile);
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        // add picture to public gallery(into the Diary folder)
        Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        getActivity().sendBroadcast(galleryIntent);


        mFloatingActionButtonCamera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Check if any app can handle
                if (takePicture.resolveActivity(getActivity().getPackageManager()) != null)
                {
                    startActivityForResult(takePicture, REQUEST_CAMERA);

                }

            }
        });



        mImageView = (ImageView) view.findViewById(R.id.picture_view);

        // Queue the code(thread) till the layout passes
        mImageView.post(new Runnable()
        {
            @Override
            public void run()
            {
                mImageView.setImageBitmap(ImageManager.setPic(mPhotoFile, mImageView));

                mTargetH = mImageView.getHeight();
                mTargetW = mImageView.getWidth();
            }
        });
        /*
        Glide.with(getContext())
                .load(mPhotoFile).placeholder(R.drawable.placeholder)

                .centerCrop()
                .into(mImageView);
                */


        mImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                // Change dynamically layout to see the pic full size on the black background
                // make sure image exists
                if (mPhotoFile.exists() && mPhotoFile != null)
                {
                    if (!isImageFitToScreen)
                    {
                        isImageFitToScreen = true;
                        LinearLayout.LayoutParams lr1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        mImageView.setLayoutParams(lr1);
                        mImageView.setAdjustViewBounds(true);
                        mTextTitle.setVisibility(View.GONE);
                    } else
                    {

                        isImageFitToScreen = false;
                        LinearLayout.LayoutParams lr = new LinearLayout.LayoutParams(mImageView.getLayoutParams());
                        lr.width = (int) mTargetW;
                        lr.height = (int) mTargetH;
                        lr.setMargins(15, 0, 15, 0);
                        mImageView.setLayoutParams(lr);
                        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        mTextTitle.setVisibility(View.VISIBLE);
                    }
                }
            }


        });

        mFloatingActionButtonGallery = (FloatingActionButton) view.findViewById(R.id.fab_menu_gallery);

        // Create the implicit intent to search gallery


        mFloatingActionButtonGallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent searchGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                searchGallery = Intent.createChooser(searchGallery,"Complete action using");
                startActivityForResult(searchGallery, REQUEST_GALLERY);
            }
        });

        mFloatingActionButtonShare = (FloatingActionButton) view.findViewById(R.id.fab_menu_share);

        mFloatingActionButtonShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // make sure the image exists to share it
                if ((mPhotoFile.exists() && mPhotoFile != null))
                {
                    Bitmap icon = ImageManager.setPic(mPhotoFile, mImageView);
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                    try
                    {
                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
                    startActivity(Intent.createChooser(share, "Share Image"));
                }
            }
        });

        return view;
    }


    // Get data from intents
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // camera intent
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA)
        {

            mImageView.setImageBitmap(ImageManager.setPic(mPhotoFile, mImageView));

        }
        // gallery intent
        else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY)
        {


            Uri uri = data.getData();
            String[]projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);

            // Create copy if used picture from different directory as entry and image must share uuid
            File scrFile = new File(filePath);
            try
            {
                ImageManager.copy(scrFile, mPhotoFile);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            cursor.close();
            mImageView.setImageBitmap(ImageManager.setPic(mPhotoFile, mImageView));
        }
        else
        {
            return;
        }

    }

}
