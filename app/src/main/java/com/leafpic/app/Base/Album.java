package com.leafpic.app.Base;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.leafpic.app.R;
import com.leafpic.app.utils.StringUtils;

import java.util.ArrayList;

/**
 * Created by dnld on 12/11/15.
 */
public class Album implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
    public String ID;
    public String DisplayName;
    public String Path = "";

    ArrayList<Media> medias;
    private int imagesCount = 0;
    private boolean hidden = false;
    private boolean selected = false;
    private String coverPath = null;

    public Album(String id, String name, int count) {
        ID = id;
        DisplayName = name;
        imagesCount = count;
    }

    public Album(String ID) {
        this.ID = ID;
    }

    public Album(String path, String displayName, boolean hidden, int count) {
        medias = new ArrayList<Media>();
        DisplayName = displayName;
        Path = path;
        setHidden(hidden);
        imagesCount = count;
    }

    protected Album(Parcel in) {
        ID = in.readString();
        DisplayName = in.readString();
        imagesCount = in.readInt();
        Path = in.readString();
        if (in.readByte() == 0x01) {
            medias = new ArrayList<Media>();
            in.readList(medias, Media.class.getClassLoader());
        } else {
            medias = null;
        }
        hidden = in.readByte() != 0x00;
        selected = in.readByte() != 0x00;
    }

    public void setPath() {
        try {
            Path = StringUtils.getBucketPathbyImagePath(medias.get(0).Path);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean value) {
        hidden = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelcted(boolean value) {
        selected = value;
    }

    public String getPathCoverAlbum() {
        if (hasCustomCover()) return coverPath;
        if (medias.size() > 0) return "file://" + medias.get(0).Path;
        else return "drawable://" + R.drawable.ic_empty;
    }

    public boolean hasCustomCover() {
        return coverPath != null;
    }

    public Media getCoverAlbum() {
        if (coverPath != null) {
            Log.wtf("asdasd", Path + "--" + coverPath);

            return new Media(coverPath);

        }
        if (medias.size() > 0) return medias.get(0);
        return new Media("drawable://" + R.drawable.ic_empty);
    }

    public void setCoverPath(String path) {
        coverPath = path;
    }

    public int getImagesCount() {
        return imagesCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(DisplayName);
        dest.writeInt(imagesCount);
        dest.writeString(Path);
        if (medias == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(medias);
        }
        dest.writeByte((byte) (hidden ? 0x01 : 0x00));
        dest.writeByte((byte) (selected ? 0x01 : 0x00));
    }
}