package com.leafpic.app.Base;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.leafpic.app.utils.StringUtils;

public class Media implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
    public String Path;
    public String MIME;
    public long DateTaken = -1;
    public String FolderPath;
    public long DateModified = -1;
    public int orientation = 0;
    public int width;
    public int height;
    public long size;
    boolean selected = false;

    public Media(String path) {
        Path = path;
    }

    public Media(String path, long dateTaken) {
        Path = path;
        DateTaken = dateTaken;
    }

    public Media(String path, long dateTaken, String mime) {
        Path = path;
        DateTaken = dateTaken;
        MIME = mime;
    }

    public Media(String path, long dateTaken, long dateModified, String mime, int width, int height, int size) {
        Path = path;
        DateTaken = dateTaken;
        MIME = mime;
        DateModified = dateModified;
        this.width = width;
        this.height=height;
        this.size = size;
    }

    public Media(String path, long dateTaken, String mime, String folderPath) {
        Path = path;
        DateTaken = dateTaken;
        MIME = mime;
        FolderPath = folderPath;
    }
    protected Media(Parcel in) {
        Path = in.readString();
        MIME = in.readString();
        DateTaken = in.readLong();
        FolderPath = in.readString();
        DateModified = in.readLong();
        width = in.readInt();
        height = in.readInt();
        size = in.readLong();
        selected = in.readByte() != 0x00;
    }

    public Uri getUri() {
        return Uri.parse("file://" + Path);
    }

    public boolean isGif() {
        return (MIME != null && MIME.equals("image/gif"));
    }

    public boolean isVideo() {
        return (MIME != null && MIME.startsWith("video/"));
    }

    public String getResolution() {
        return width + "x" + height;
    }

    public String getHumanReadableSize() {
        return StringUtils.humanReadableByteCount(size, true);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean val) {
        selected = val;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Path);
        dest.writeString(MIME);
        dest.writeLong(DateTaken);
        dest.writeString(FolderPath);
        dest.writeLong(DateModified);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeLong(size);
        dest.writeByte((byte) (selected ? 0x01 : 0x00));
    }
}