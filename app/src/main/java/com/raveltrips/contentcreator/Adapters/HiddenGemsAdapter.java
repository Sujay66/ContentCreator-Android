package com.raveltrips.contentcreator.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.async.DownloadImageAsyncTask;
import com.raveltrips.contentcreator.models.CompleteTrip;
import com.raveltrips.contentcreator.models.PaidActivities;
import com.raveltrips.contentcreator.models.Pindrop;
import com.raveltrips.contentcreator.models.Restaurants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by LENOVO on 26-05-2017.
 */

public class HiddenGemsAdapter extends android.support.v7.widget.RecyclerView.Adapter<HiddenGemsAdapter.ViewHolder> {

    private static OnItemClickListener mItemClickListener ;
    private Context context ;
    private List<CompleteTrip> gems;
    int type;
    public HiddenGemsAdapter(Context context, List<CompleteTrip> t , int type){
        gems = t;
        this.context = context;
        this.type = type;
    }
    @Override
    public HiddenGemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gemcard, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HiddenGemsAdapter.ViewHolder holder, int position) {

        Typeface custom_font = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        holder.name.setTypeface(dindemi);
        holder.tags.setTypeface(dindemi);
        if (type == 1)
        {
            Pindrop pindrop = AppContext.selectedCompleteTrip.getPindrops().get(position);
            holder.name.setText(pindrop.getName());
            holder.desc.setText(pindrop.getDescription());
            if(!pindrop.getreadyForReview()){
                holder.finished.setText("Unfinished");
            }
            else
            {
                holder.finished.setText("");
            }
            if(pindrop.getTags().size()>0){
                holder.tags.setText("");
                for(String s: pindrop.getTags()){
                    if(pindrop.getTags().size() ==1 ) {
                        if(!s.equals("")){
                            holder.tags.append("#" + s);
                        }
                    }else {
                        if(!s.equals("")){
                            if(s.equals(pindrop.getTags().get(pindrop.getTags().size()-1))){
                                holder.tags.append("#" + s);
                            }else {
                                holder.tags.append("#" + s + ",");
                            }
                        }
                    }
                }
            }

            if (pindrop.getImageUrls() != null && pindrop.getImageUrls().size() > 0) {
                try {
                    String url = pindrop.getImageUrls().get(0);
                    if (AppContext.USE_PICASSO)
                        Picasso.with(context).load(url).into(holder.gemimage);
                    else {
                        Bitmap bitmap = AppContext.getBitmapFromMemCache(url);
                        if (bitmap != null) {
                            holder.gemimage.setImageBitmap(bitmap);
                        } else {
                            DownloadImageAsyncTask task = new DownloadImageAsyncTask(holder.gemimage);
                            task.execute(url);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (type == 4)
        {
            Restaurants pindrop =(Restaurants) AppContext.selectedCompleteTrip.getRestaurants().get(position);
            holder.name.setText(pindrop.getName());
            holder.desc.setText(pindrop.getDescription());
            if(!pindrop.getreadyForReview()){
                holder.finished.setText("Unfinished");
            }
            if(pindrop.getTags().size()>0){
                holder.tags.setText("");
                for(String s: pindrop.getTags()){
                    if(pindrop.getTags().size() ==1 ) {
                        if(!s.equals("")){
                            holder.tags.append("#" + s);
                        }
                    }else {
                        if(!s.equals("")){
                            if(s.equals(pindrop.getTags().get(pindrop.getTags().size()-1))){
                                holder.tags.append("#" + s);
                            }else {
                                holder.tags.append("#" + s + ",");
                            }
                        }
                    }
                }
            }

            if (pindrop.getImageUrls() != null && pindrop.getImageUrls().size() > 0) {
                try {
                    String url = pindrop.getImageUrls().get(0);
                    if (AppContext.USE_PICASSO)
                        Picasso.with(context).load(url).into(holder.gemimage);
                    else {
                        Bitmap bitmap = AppContext.getBitmapFromMemCache(url);
                        if (bitmap != null) {
                            holder.gemimage.setImageBitmap(bitmap);
                        } else {
                            DownloadImageAsyncTask task = new DownloadImageAsyncTask(holder.gemimage);
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
            PaidActivities pindrop = (PaidActivities) AppContext.selectedCompleteTrip.getPaidActivities().get(position);
            holder.name.setText(pindrop.getName());
            holder.desc.setText(pindrop.getDescription());
            if(!pindrop.getreadyForReview()){
                holder.finished.setText("Unfinished");
            }
            if(pindrop.getTags().size()>0){
                holder.tags.setText("");
                for(String s: pindrop.getTags()){
                    if(pindrop.getTags().size() ==1 ) {
                        if(!s.equals("")){
                            holder.tags.append("#" + s);
                        }
                    }else {
                        if(!s.equals("")){
                            if(s.equals(pindrop.getTags().get(pindrop.getTags().size()-1))){
                                holder.tags.append("#" + s);
                            }else {
                                holder.tags.append("#" + s + ",");
                            }
                        }
                    }
                }
            }

            if (pindrop.getImageUrls() != null && pindrop.getImageUrls().size() > 0) {
                try {
                    String url = pindrop.getImageUrls().get(0);
                    if (AppContext.USE_PICASSO)
                        Picasso.with(context).load(url).into(holder.gemimage);
                    else {
                        Bitmap bitmap = AppContext.getBitmapFromMemCache(url);
                        if (bitmap != null) {
                            holder.gemimage.setImageBitmap(bitmap);
                        } else {
                            DownloadImageAsyncTask task = new DownloadImageAsyncTask(holder.gemimage);
                            task.execute(url);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }



    }

    @Override
    public int getItemCount() {
        try {
            if (type == 1) {
                if (AppContext.selectedCompleteTrip.getPindrops() != null)
                    return AppContext.selectedCompleteTrip.getPindrops().size();
                else
                    return 0;
            } else if (type == 4) {
                if (AppContext.selectedCompleteTrip.getRestaurants() != null)
                    return AppContext.selectedCompleteTrip.getRestaurants().size();
                else
                    return 0;
            } else {
                if (AppContext.selectedCompleteTrip.getPaidActivities() != null)
                    return AppContext.selectedCompleteTrip.getPaidActivities().size();
                else
                    return 0;
            }
        }
        catch (Exception  e)
        {
            Log.d("HiddenGemsAdapter","Error "+e.getStackTrace());
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView name,desc,tags,finished;
         ImageView gemimage,editgem,delgem;
            Button nav;
         ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.gemtitle);
            gemimage = (ImageView) itemView.findViewById(R.id.gemimage);
            tags = (TextView) itemView.findViewById(R.id.gemtags);
            desc = (TextView) itemView.findViewById(R.id.gemdescription);
             editgem = (ImageView) itemView.findViewById(R.id.editgem);
             delgem = (ImageView) itemView.findViewById(R.id.deletegem);
             finished = (TextView)itemView.findViewById(R.id.finished) ;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(v,getAdapterPosition(),type);
                }
            });
             editgem.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                        if(mItemClickListener!=null){
                            mItemClickListener.onEditClick(view,getAdapterPosition(),type);
                        }
                 }
             });

             delgem.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     if(mItemClickListener!=null){
                         mItemClickListener.onDeleteClick(view,getAdapterPosition(),type);
                     }
                 }
             });
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(View v, int position , int selectedType);
        void onEditClick(View v, int position,int selectedType);
        void onDeleteClick(View v, int position, int selectedType);
    }

    public void setListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
}
