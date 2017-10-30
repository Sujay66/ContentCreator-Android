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
import com.raveltrips.contentcreator.models.CompleteTrip;

import java.util.List;

/**
 * Created by LENOVO on 26-05-2017.
 */

public class MyTripsAdapter extends android.support.v7.widget.RecyclerView.Adapter<MyTripsAdapter.ViewHolder>  {

    static OnItemClickListener mItemClickListener ;
    private Context context = null;
    private List<CompleteTrip> trips;
    private String[] dummy;

    public MyTripsAdapter(Context context, List<CompleteTrip> t){
        trips = t;
        this.context = context;
    }
    @Override
    public MyTripsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mytripscard, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Typeface custom_font = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        holder.name.setTypeface(custom_font);
        holder.name.setText(trips.get(position).getName());
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
        return trips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView arrowImage;
        public ViewHolder(final View itemView) {
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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemClickListener != null) {
                        itemView.setSelected(true);
                        itemView.setBackgroundColor(1);
                        mItemClickListener.onItemLongClick(v, getPosition());

                    }
                    return false;
                }
            });



        }
    }

    public interface OnItemClickListener
    {
         void onItemClick(View v, int position);
         void onItemLongClick(View v, int position);
    }

    public void setListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
}
