package org.horaapps.leafpic.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.bumptech.glide.signature.StringSignature;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.data.base.MediaDetailsMap;
import org.horaapps.leafpic.util.StringUtils;
import org.jetbrains.annotations.TestOnly;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dnld on 26/04/16.
 */
public class Media implements Parcelable, Serializable {

    private String path = null;
    private long dateModified = -1;
    private String mimeType = null;

    private String uri = null;

    private long size = 0;
    private boolean selected = false;
    private MetadataItem metadata;

    public Media() { }

    public Media(String path, long dateModified) {
        this.path = path;
        this.dateModified = dateModified;
        mimeType = StringUtils.getMimeType(path);
    }

    public Media(File file) {
        this(file.getPath(), file.lastModified());
        this.size = file.length();
        mimeType = StringUtils.getMimeType(path);
    }

    public Media(String path) {
        this(path, -1);
    }

    public Media(Context context, Uri mediaUri) {
        this.uri = mediaUri.toString();
        this.path = null;
        mimeType = context.getContentResolver().getType(getUri());
    }

    public Media(@NotNull Cursor cur) {
        this.path = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
        this.size = cur.getLong(cur.getColumnIndex(MediaStore.Images.Media.SIZE));
        this.dateModified = cur.getLong(cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
        mimeType = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
    }

    public void setUri(String uriString) {
        this.uri = uriString;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public boolean isSelected() {
        return selected;
    }

    void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isGif() { return getMimeType().endsWith("gif"); }

    public boolean isImage() { return getMimeType().startsWith("image"); }

    public boolean isVideo() { return getMimeType().startsWith("video"); }

    public Uri getUri() {
        return hasUri() ? Uri.parse(uri) : Uri.fromFile(new File(path));
    }

    public String getName() {
        return StringUtils.getPhotoNameByPath(path);
    }

    public long getSize() {
        return size;
    }

    private boolean hasUri() {
        return uri != null;
    }

    public String getPath() {
        return path;
    }

    public Long getDateModified() {
        return dateModified;
    }


    public Bitmap getBitmap(){
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path,bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
        return bitmap;
    }

    public StringSignature getSignature() {
        return new StringSignature(getPath() + "-" + getDateModified());
    }

    //<editor-fold desc="Exif & More">
    public GeoLocation getGeoLocation()  {
        return metadata.getLocation();
    }

    public MediaDetailsMap<String, String> getAllDetails() {
        MediaDetailsMap<String, String> data = new MediaDetailsMap<String, String>();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new File(getPath()));
            for(Directory directory : metadata.getDirectories()) {

                for(Tag tag : directory.getTags()) {
                    data.put(tag.getTagName(), directory.getObject(tag.getTagType())+"");
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;

    }

    public MediaDetailsMap<String, String> getMainDetails(Context context){
        metadata = new MetadataItem(new File(getPath()));
        MediaDetailsMap<String, String> details = new MediaDetailsMap<String, String>();
        details.put(context.getString(R.string.path), getDisplayPath());
        details.put(context.getString(R.string.type), getMimeType());
        String tmp;
        if ((tmp = metadata.getResolution()) != null)
            details.put(context.getString(R.string.resolution), tmp);

        details.put(context.getString(R.string.size), getHumanReadableSize());
        details.put(context.getString(R.string.date), SimpleDateFormat.getDateTimeInstance().format(new Date(getDateModified())));
        if (metadata.getOrientation() != -1)
            details.put(context.getString(R.string.orientation), metadata.getOrientation()+"");
        if (metadata.getDateOriginal() != null)
            details.put(context.getString(R.string.date_taken), SimpleDateFormat.getDateTimeInstance().format(metadata.getDateOriginal()));

        if ((tmp = metadata.getCameraInfo()) != null)
            details.put(context.getString(R.string.camera), tmp);
        if ((tmp = metadata.getExifInfo()) != null)
            details.put(context.getString(R.string.exif), tmp);
        GeoLocation location;
        if ((location = metadata.getLocation()) != null)
            details.put(context.getString(R.string.location), location.toDMSString());

        return details;
    }

    public boolean setOrientation(int orientation) {
        // TODO: 06/08/16 implement this method
        /*int asd;
        ExifInterface exif;
        try { exif = new ExifInterface(getPath()); }
        catch (IOException ex) { return false; }
        switch (orientation) {
            case 90: asd = ExifInterface.ORIENTATION_ROTATE_90; break;
            case 180: asd = ExifInterface.ORIENTATION_ROTATE_180; break;
            case 270: asd = ExifInterface.ORIENTATION_ROTATE_270; break;
            case 0: asd = 1; break;
            default: return false;
        }
        exif.setAttribute(ExifInterface.TAG_ORIENTATION, asd+"");
        try {  exif.saveAttributes(); }
        catch (IOException e) {  return false;}
        return true;*/
        return false;
    }

    private long getDateTaken() {
        // TODO: 16/08/16 improved
        Date dateOriginal = metadata.getDateOriginal();
        if (dateOriginal != null) return metadata.getDateOriginal().getTime();
        return -1;
    }

    public boolean fixDate(){
        long newDate = getDateTaken();
        if (newDate != -1){
            File f = new File(getPath());
            if (f.setLastModified(newDate)) {
                dateModified = newDate;
                return true;
            }
        }
        return false;
    }
    private String getHumanReadableSize() {
        return StringUtils.humanReadableByteCount(size, true);
    }
    private String getDisplayPath() {
        return  hasPath() ? getPath() : getUri().getEncodedPath();
    }
    private boolean hasPath() {
        return path != null;
    }
    //</editor-fold>

    //<editor-fold desc="Thumbnail Tests">
    @TestOnly
    public byte[] getThumbnail() {

        ExifInterface exif;
        try {
            exif = new ExifInterface(getPath());
        } catch (IOException e) {
            return null;
        }
        if (exif.hasThumbnail())
            return exif.getThumbnail();
        return null;

        // NOTE: ExifInterface is faster than metadata-extractor to getValue the thumbnail data
        /*try {
            Metadata metadata = ImageMetadataReader.readMetadata(new File(getMediaPath()));
            ExifThumbnailDirectory thumbnailDirectory = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
            if (thumbnailDirectory.hasThumbnailData())
                return thumbnailDirectory.getThumbnailData();
        } catch (Exception e) { return null; }*/
    }

    @TestOnly
    public String getThumbnail(Context context) {
        /*Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
                context.getContentResolver(), id,
                MediaStore.Images.Thumbnails.MINI_KIND,
                new String[]{ MediaStore.Images.Thumbnails.DATA } );
        if(cursor.moveToFirst())
            return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
        return null;*/
        return null;
    }
    //</editor-fold>


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeLong(this.dateModified);
        dest.writeString(this.mimeType);
        dest.writeLong(this.size);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected Media(Parcel in) {
        this.path = in.readString();
        this.dateModified = in.readLong();
        this.mimeType = in.readString();
        this.size = in.readLong();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}