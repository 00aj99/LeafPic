package com.leafpic.app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.leafpic.app.Adapters.AlbumsAdapter;
import com.leafpic.app.Adapters.SideDrawerAdapter;
import com.leafpic.app.utils.string;

public class AlbumsActivity extends AppCompatActivity {
    DatabaseHandler db = new DatabaseHandler(AlbumsActivity.this);
    HandlingAlbums albums = new HandlingAlbums(AlbumsActivity.this);

    boolean editmode = false, hidden = false;
    RecyclerView.Adapter mAdapter;
    RecyclerView mRecyclerView;
    AlbumsAdapter adapt;
    // MediaStoreObserver observer;
    Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

       /* ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(200, 200)
                .diskCacheExtraOptions(200, 200, null)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .build();
        ImageLoader.getInstance().init(config);*/

        initUiTweaks();
        checkPermissions();
        //db.LogPhotosMediaStoreByFolderPath();

        /* observer = new MediaStoreObserver(null);

        Log.d("INSTANT", "registered content observer");

        this.getApplicationContext()
                .getContentResolver()
                .registerContentObserver(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false,
                        observer);

        Log.d("INSTANT", "registered content observer");
*/
    }

    @Override
    public void onDestroy() {
        //getContentResolver().unregisterContentObserver(observer);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        /*db.updatePhotos();
        albums.loadAlbums();
        adapt.notifyDataSetChanged();*/
        checkPermissions();
        //    adapt.notifyDataSetChanged();
        super.onResume();
    }

    public void initUiTweaks(){

        /**** Navigation Bar*/
        /*
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.toolbar));
        }
        */
        /**** Status Bar */
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.status_bar));

        /**** ToolBar*/
        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        /**** Drawer*/
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //toolbar.setTitle("asd");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        String drawerArrayItems[] = getResources().getStringArray(R.array.drawer_items);
        int ICONS[] = new int[1];

        RecyclerView drawerAdapter = (RecyclerView) findViewById(R.id.RecyclerView);

        mAdapter = new SideDrawerAdapter(drawerArrayItems, ICONS);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        drawerAdapter.setLayoutManager(mLayoutManager);
        drawerAdapter.setAdapter(mAdapter);

        try{
            ImageView current_picture = (ImageView) findViewById(R.id.current_picture);
            Glide.with(this)
                    .load(R.drawable.storage_icon)
                    .centerCrop()
                    .placeholder(R.drawable.ic_empty)
                    .crossFade()
                    .into(current_picture);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position){
                    case 0:
                        hidden = false;
                        checkPermissions();
                        /*albums.loadAlbums();
                        adapt.setDataset(albums.dispAlbums);
                        adapt.notifyDataSetChanged();*/

                        break;
                    case 1:
                        hidden = true;
                        checkPermissions();
                        /*albums.loadHiddenAlbums();
                        adapt.setDataset(albums.dispAlbums);
                        adapt.notifyDataSetChanged();*/

                        break;

                    default:break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public  void checkPermissions(){

        if (ContextCompat.checkSelfPermission(AlbumsActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AlbumsActivity.this,
                    Manifest.permission.INTERNET))
                string.showToast(AlbumsActivity.this, "eddai dammi internet");
            else
                ActivityCompat.requestPermissions(AlbumsActivity.this,
                        new String[]{Manifest.permission.INTERNET}, 1);
        }

        if (ContextCompat.checkSelfPermission(AlbumsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AlbumsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                string.showToast(AlbumsActivity.this, "no storage permission");
            else
                ActivityCompat.requestPermissions(AlbumsActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else
            //got the power
            loadAlbums();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_albums, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        MenuItem opt;

        if (editmode) {
            opt = menu.findItem(R.id.endEditAlbumMode);
            opt.setEnabled(true).setVisible(true);
            setOptionsAlbmuMenusItemsVisible(menu,true);
        } else {
            opt = menu.findItem(R.id.endEditAlbumMode);
            opt.setEnabled(false).setVisible(false);
            setOptionsAlbmuMenusItemsVisible(menu,false);
        }

        if (hidden) {
            opt = menu.findItem(R.id.refreshhiddenAlbumsButton);
            opt.setEnabled(true).setVisible(true);
            opt = menu.findItem(R.id.hideAlbumButton);
            opt.setTitle(getString(R.string.unhide_album_action));
        } else {
            opt = menu.findItem(R.id.refreshhiddenAlbumsButton);
            opt.setEnabled(false).setVisible(false);
            opt = menu.findItem(R.id.hideAlbumButton);
            opt.setTitle(getString(R.string.hide_album_action));
        }


        if (albums.getSelectedCount()==0) {
            editmode = false;
            opt = menu.findItem(R.id.endEditAlbumMode);
            setOptionsAlbmuMenusItemsVisible(menu,false);
            opt.setEnabled(false).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private void setOptionsAlbmuMenusItemsVisible(final Menu m, boolean val) {
        MenuItem option = m.findItem(R.id.hideAlbumButton);
        option.setEnabled(val).setVisible(val);

        option = m.findItem(R.id.deleteAction);

        option.setEnabled(val).setVisible(val);

        option = m.findItem(R.id.excludeAlbumButton);
        option.setEnabled(val).setVisible(val);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_action:
                View sort_btn = findViewById(R.id.sort_action);
                PopupMenu popup = new PopupMenu(AlbumsActivity.this, sort_btn);
                popup.setGravity(Gravity.AXIS_CLIP);

                popup.getMenuInflater()
                        .inflate(R.menu.sort, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(
                                AlbumsActivity.this,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
                break;

            case R.id.refreshhiddenAlbumsButton:
                albums.loadPreviewHiddenAlbums();
                adapt.notifyDataSetChanged();
                break;
            case R.id.endEditAlbumMode:
                editmode = false;
                invalidateOptionsMenu();
                albums.clearSelectedAlbums();
                adapt.notifyDataSetChanged();
                break;

            case R.id.excludeAlbumButton:
                AlertDialog.Builder dasdf = new AlertDialog.Builder(
                        new ContextThemeWrapper(this, R.style.AlertDialogCustom));
                dasdf.setMessage(getString(R.string.exclude_album_message));
                dasdf.setCancelable(true);
                dasdf.setPositiveButton("EXCLUDE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        albums.excludeSelectedAlbums();
                        adapt.notifyDataSetChanged();
                        invalidateOptionsMenu();
                    }
                });
                dasdf.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dasdf.show();
                break;

            case R.id.deleteAction:
                AlertDialog.Builder dlg = new AlertDialog.Builder(
                        new ContextThemeWrapper(this, R.style.AlertDialogCustom));
                dlg.setMessage(getString(R.string.delete_album_message));
                dlg.setCancelable(true);
                dlg.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        albums.deleteSelectedAlbums();
                        adapt.notifyDataSetChanged();
                        invalidateOptionsMenu();

                    }
                });
                dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dlg.show();
                break;

            case R.id.hideAlbumButton:
                if (hidden) {
                    albums.unHideSelectedAlbums();
                    adapt.notifyDataSetChanged();
                    invalidateOptionsMenu();
                } else {
                    AlertDialog.Builder dlg1 = new AlertDialog.Builder(
                            new ContextThemeWrapper(this, R.style.AlertDialogCustom));
                    dlg1.setMessage(getString(R.string.hide_album_message));
                    dlg1.setPositiveButton("HIDE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            albums.hideSelectedAlbums();
                            adapt.notifyDataSetChanged();
                            invalidateOptionsMenu();
                        }
                    });
                    dlg1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dlg1.setNeutralButton("EXCLUDE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            albums.excludeSelectedAlbums();
                            adapt.notifyDataSetChanged();
                            invalidateOptionsMenu();
                        }
                    });
                    dlg1.show();
                }
                break;

            case R.id.action_camera:
                Intent i = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                startActivity(i);
                return true;

            case  R.id.settinglayout:
                Intent intent = new Intent(this, Preferences_Activity.class);
                startActivity(intent);
                return true;
            // PER USARE I SHARED PREFERENCES GUARDA STA ROBA STRONZO BELLO
            case  R.id.trySetting:

                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String strUserName = SP.getString("username", "NA");
                boolean bAppUpdates = SP.getBoolean("applicationUpdates", false);
                String downloadType = SP.getString("downloadType", "1");

                string.showToast(AlbumsActivity.this," UserName: " + strUserName + " DownloadType: " + downloadType +" Updates: " + bAppUpdates);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    loadAlbums();
                break;
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    string.showToast(AlbumsActivity.this, "i got NET");
                break;
        }
    }


    private void loadAlbums() {


        if (hidden)
            albums.loadPreviewHiddenAlbums();
        else {
            db.updatePhotos();
            albums.loadPreviewAlbums();
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.gridAlbums);
        adapt = new AlbumsAdapter(albums.dispAlbums, R.layout.album_card);

        adapt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView a = (TextView) v.findViewById(R.id.picturetext);
                String s = a.getTag().toString();
                adapt.notifyItemChanged(albums.selectAlbum(s, true));
                editmode = true;
                invalidateOptionsMenu();
                return true;
            }
        });

        adapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView a = (TextView) v.findViewById(R.id.picturetext);
                String s = a.getTag().toString();
                Album album = albums.getAlbum(s);

                int pos;
                if (editmode) {
                    if (album.isSelected()) pos = albums.selectAlbum(s, false);
                    else pos = albums.selectAlbum(s, true);
                    adapt.notifyItemChanged(pos);
                    invalidateOptionsMenu();
                } else {
                    Intent intent = new Intent(AlbumsActivity.this, PhotosActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelable("album", album);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapt);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}
