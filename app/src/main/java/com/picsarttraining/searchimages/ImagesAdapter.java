package com.picsarttraining.searchimages;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Arsen on 04.04.2016.
 */
public class ImagesAdapter extends BaseAdapter {
    ArrayList<Bitmap> images;
    Context context;
    private LayoutInflater inflater;
    LastElementReachedListener lastElementEvent;
    private int whichTimeReached;

    public ImagesAdapter(Context context) {
        this.context = context;
        this.images = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        whichTimeReached = 1;
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images.clear();
        this.images = new ArrayList<>(images);
        notifyDataSetChanged();
    }

    public void addImages(ArrayList<Bitmap> images) {
        this.images.addAll(images);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == images.size() ? 1 : 0;
    }

    @Override
    public int getCount() {
        return images.size() + 1;
    }

    @Override
    public Bitmap getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == images.size()) {
            if (lastElementEvent != null) {
                lastElementEvent.reach(whichTimeReached);
                whichTimeReached++;
            }

            if (convertView != null)
                return convertView;
            else {
                return inflater.inflate(R.layout.image_loading, parent, false);
            }
        }

        Bitmap imageBitmap = images.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_image, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.image.setImageBitmap(imageBitmap);

        return convertView;
    }

    private class ViewHolder {
        private ImageView image;

        ViewHolder(View convertView) {
            image = (ImageView) convertView.findViewById(R.id.item_image_view);
        }
    }

    public void resetWatchCount() {
        whichTimeReached = 1;
    }

    public void setOnlastElementReachedListener(LastElementReachedListener listener) {
        lastElementEvent = listener;
    }

    public interface LastElementReachedListener {
        void reach(int whichTime);
    }
}