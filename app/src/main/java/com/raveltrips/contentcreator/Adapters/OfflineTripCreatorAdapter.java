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
 * Created by LENOVO on 20-06-2017.
 */

public class OfflineTripCreatorAdapter extends RecyclerView.Adapter<OfflineTripCreatorAdapter.ViewHolder> {

    static OnItemClickListener mItemClickListener ;
    Context context = null;
    String[] list = null;
    public OfflineTripCreatorAdapter(Context context, String[] list)
    {
        this.context =context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offlinetripcreatorcard, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(list[position]);
        holder.arrowImage.setImageResource(R.drawable.greyarrowicon);
        Typeface museo = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        holder.name.setTypeface(dinlight);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView arrowImage;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.mytriptext);
            arrowImage = (ImageView) itemView.findViewById(R.id.arrowimage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(v,getPosition());
                }
            });
        }
    }
    public interface OnItemClickListener
    {
        public void onItemClick(View v, int position);
    }

    public void setListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
}


