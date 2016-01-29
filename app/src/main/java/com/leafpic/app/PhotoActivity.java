package com.leafpic.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.leafpic.app.Adapters.PhotosPagerAdapter;
import com.leafpic.app.Animations.DepthPageTransformer;
import com.leafpic.app.utils.string;

/**
 * Created by dnld on 12/12/15.
 */
public class PhotoActivity extends AppCompatActivity {

    PhotosPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    HandlingPhotos photos;

    Toolbar toolbar;
    boolean fullscreenmode;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        initUiTweaks();

        try {

            Bundle data = getIntent().getExtras();
            photos = data.getParcelable("album");
            photos.setContext(PhotoActivity.this);
            final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    toggleSystemUI();
                    return true;
                }
            });

            mCustomPagerAdapter = new PhotosPagerAdapter(this, photos.photos);

            mCustomPagerAdapter.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mCustomPagerAdapter);
            mViewPager.setCurrentItem(photos.getCurrentPhotoIndex());
            mViewPager.setPageTransformer(true, new DepthPageTransformer());
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    photos.setCurrentPhotoIndex(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        //DA FIXXARE
        hideSystemUI();
        showSystemUI();
        hideSystemUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.shareButton:
                String file_path = photos.photos.get(mViewPager.getCurrentItem()).Path;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType(string.getMimeType(file_path));
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file_path));
                startActivity(Intent.createChooser(share, "Share Image"));
                return true;

            case R.id.deletePhoto:
                new MaterialDialog.Builder(this)
                        .content(R.string.delete_photo_message)
                        .positiveText("DELETE")
                        .negativeText("CANCEL")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                int index = mViewPager.getCurrentItem();

                                mViewPager.removeViewAt(index);
                                photos.deleteCurrentPhoto();
                                mCustomPagerAdapter.notifyDataSetChanged();
                                mViewPager.setCurrentItem(index + 1);
                            }
                        })
                        .show();
                return true;

            case R.id.rotatePhoto:
                return true;

            case R.id.useAsIntent:
                String file_path_use_as = photos.photos.get(mViewPager.getCurrentItem()).Path;
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.setDataAndType(Uri.parse("file://" + file_path_use_as), "image/*");
                intent.putExtra("jpg", string.getMimeType(file_path_use_as));
                startActivity(Intent.createChooser(intent, "Use As"));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void initUiTweaks() {

        /**** ToolBar ********/
        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setBackgroundColor(getColor(R.color.transparent_gray));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /**** Status Bar *****/
        getWindow().setStatusBarColor(getColor(R.color.transparent_gray));
        /**** Navigation Bar */
        getWindow().setNavigationBarColor(getColor(R.color.transparent_gray));

        // TODO start immersiveMode [PORCODIO]

    }

    private void toggleSystemUI() {
        if (fullscreenmode)
            showSystemUI();
        else hideSystemUI();

    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.

        //getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();

        fullscreenmode = true;
    }

    private void showSystemUI() {
        toolbar.animate().translationY(getStatusBarHeight()).setInterpolator(new DecelerateInterpolator()).start();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        fullscreenmode = false;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
