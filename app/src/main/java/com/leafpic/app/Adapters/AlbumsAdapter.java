package com.leafpic.app.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.leafpic.app.Album;
import com.leafpic.app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

/**
 * Created by dnld on 1/7/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    ArrayList<Album> albums;

    private int layout_ID;
    private View.OnClickListener mOnClickListener;
    private View.OnLongClickListener mOnLongClickListener;

    public AlbumsAdapter(ArrayList<Album> ph, int id) {
        albums = ph;
        layout_ID = id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout_ID, parent, false);
        v.setOnClickListener(mOnClickListener);
        v.setOnLongClickListener(mOnLongClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlbumsAdapter.ViewHolder holder, int position) {
        Album a = albums.get(position);
        holder.picture.setTag(a.getPathCoverAlbum());
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_empty)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .cacheInMemory(true)
                .build();

        ImageLoader.getInstance().displayImage(a.getPathCoverAlbum(), holder.picture, defaultOptions);
        holder.name.setText(a.DisplayName);
        holder.nPhotos.setText(Html.fromHtml("<b><font color='#FBC02D'>" + a.getImagesCount() + "</font></b>" + "<font " +
                "color='#FFFFFF'> Photos</font>"));
        holder.name.setTag(a.Path);


        if (a.isSelected()) {
            //name.setBackgroundColor(localContext.getColor(R.color.selected_album));
            holder.card_layout.setBackgroundColor(holder.card_layout.getContext().getColor(R.color.selected_album));
        } else {
            //name.setBackgroundColor(localContext.getColor(R.color.unselected_album));
            holder.card_layout.setBackgroundColor(holder.card_layout.getContext().getColor(R.color.unselected_album));
        }

    }


    public void setDataset(ArrayList<Album> dataset) {
        albums = dataset;
        // This isn't working
        notifyItemRangeInserted(0, dataset.size());

    }

    public void setOnClickListener(View.OnClickListener lis) {
        mOnClickListener = lis;
    }

    public void setOnLongClickListener(View.OnLongClickListener lis) {
        mOnLongClickListener = lis;
    }


    @Override
    public int getItemCount() {
        return albums.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout card_layout;
        ImageView picture;
        TextView name;
        TextView nPhotos;

        public ViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.picture);
            card_layout = (RelativeLayout) itemView.findViewById(R.id.layout_card_id);
            name = (TextView) itemView.findViewById(R.id.picturetext);
            nPhotos = (TextView) itemView.findViewById(R.id.image_number_text);
        }
    }
}



