package com.leafpic.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.leafpic.app.Adapters.SelectAlbumAdapter;
import com.leafpic.app.Base.HandlingAlbums;
import com.leafpic.app.Base.HandlingPhotos;

/**
 * Created by dnld on 2/8/16.
 */
public class SelectAlbumActivity extends AppCompatActivity {

    public static final int COPY_TO_ACTION = 23;
    public static final int MOVE_TO_ACTION = 69;


    HandlingAlbums albums = new HandlingAlbums(SelectAlbumActivity.this);
    RecyclerView mRecyclerView;
    SelectAlbumAdapter adapt;
    String photoPaths;
    int code;
    HandlingPhotos p;//= new HandlingPhotos(SelectAlbumActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_album_activity);

        photoPaths = getIntent().getStringExtra("selected_photos");
        code = getIntent().getIntExtra("request_code", -1);
        p = new HandlingPhotos(SelectAlbumActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(getColor(R.color.toolbar));



        albums.loadPreviewAlbums();

        mRecyclerView = (RecyclerView) findViewById(R.id.gridAlbums);

        adapt = new SelectAlbumAdapter(albums.dispAlbums, R.layout.select_album_card);

        adapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView a = (TextView) v.findViewById(R.id.album_name);
                String newAlbumPath = a.getTag().toString();
                //  Album album = albums.getAlbum(s);
                // Intent result = new Intent();
                //result.putExtra("album_path", album.Path);
                //result.putExtra("selected_photos", photoPaths);

                if (code == 69) {

                    String paths[] = photoPaths.split("ç");
                    Log.wtf("asdasd", photoPaths);
                    for (String path : paths) {
                        p.movePhoto(path, newAlbumPath);
                    }
                    setResult(Activity.RESULT_OK);
                }

                finish();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapt);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapt.notifyDataSetChanged();
    }
}

