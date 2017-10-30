package com.raveltrips.contentcreator.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.async.DownloadImageAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by LENOVO on 29-05-2017.
 */

public class ViewPhotosAdapter extends BaseAdapter {

    private Context mContext;
    ImageView image;
    List<String> imageIds;
    ImageView imageViewAndroid;
    public ViewPhotosAdapter(Context context, List<String> images){
        imageIds =  images;
        mContext = context;
    }

    @Override
    public int getCount() {
        return imageIds.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null)
        {
            imageViewAndroid = new ImageView(mContext);
            imageViewAndroid.setLayoutParams(new GridView.LayoutParams(500, 500));
            imageViewAndroid.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViewAndroid.setPadding(6, 6, 6, 6);
            if (imageIds.get(i) != null ) {
                    String url = imageIds.get(i);
                    try {
                        if(AppContext.USE_PICASSO)
                            Picasso.with(mContext).load(url).into(imageViewAndroid);
                        else {
                            Bitmap bitmap = AppContext.getBitmapFromMemCache(url);
                            if(bitmap != null){
                                imageViewAndroid.setImageBitmap(bitmap);
                            }else {
                                DownloadImageAsyncTask task = new DownloadImageAsyncTask(imageViewAndroid);
                                task.execute(url);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        else
        {
            imageViewAndroid = (ImageView) view;
        }
        return imageViewAndroid;
    }
}
