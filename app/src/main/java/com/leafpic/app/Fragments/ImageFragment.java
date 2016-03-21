package com.leafpic.app.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.ion.Ion;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by dnld on 18/02/16.
 */

public class ImageFragment extends Fragment {

    private String path;
    private long DataModified;
    private int orientation;
    private String MIME;
    private PhotoViewAttacher.OnPhotoTapListener onPhotoTapListener;

    public static ImageFragment newInstance(String path, long dateModified, int orientation, String mime) {
        ImageFragment fragmentFirst = new ImageFragment();

        Bundle args = new Bundle();
        args.putInt("orientation", orientation);
        args.putString("path", path);
        args.putLong("dateModified", dateModified);
        args.putString("mime", mime);
        fragmentFirst.setArguments(args);

        return fragmentFirst;
    }

    public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener onPhotoTapListener) {
        this.onPhotoTapListener = onPhotoTapListener;
    }

    //public void setOnTouchListener(View.OnTouchListener l){onTouchListener = l;}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orientation = getArguments().getInt("orientation", 0);
        DataModified = getArguments().getLong("dateModified", 0);
        path = getArguments().getString("path");
        MIME = getArguments().getString("mime");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PhotoView photoView = new PhotoView(container.getContext());

        Ion.with(getContext())
                .load(path)
                .withBitmap()
                .deepZoom()
                .intoImageView(photoView);

        photoView.setOnPhotoTapListener(onPhotoTapListener);

        return photoView;
    }

    public void rotatePicture(int rotation) {

    }
}