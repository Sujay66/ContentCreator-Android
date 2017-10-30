package com.raveltrips.contentcreator.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raveltrips.contentcreator.R;

/**
 * Created by anarasim on 5/16/2017.
 */

public class TripCreatorAdapter extends android.support.v7.widget.RecyclerView.Adapter<TripCreatorAdapter.ViewHolder> {

    static OnItemClickListener mItemClickListener ;
    Context context = null;
    public TripCreatorAdapter(Context context)
    {
        this.context =context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.headerlayout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Typeface custom_font = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        holder.name.setTypeface(dinlight);
        if (position==0) {
            holder.name.setText("Trip Overview");
            holder.image.setImageResource(R.drawable.tripoverview);
        }
        else if (position==1) {
            holder.name.setText("Video Upload");
            holder.image.setImageResource(R.drawable.videouploadicon);
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void setListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface OnItemClickListener
    {
        public void onItemClick(View v, int position);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public ImageView arrowImage;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iconimage);
            name = (TextView) itemView.findViewById(R.id.text1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(v,getPosition());
                }
            });



        }
    }
}
