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
 * Created by anarasim on 5/17/2017.
 */

public class TripCreatorAdapterTwo extends android.support.v7.widget.RecyclerView.Adapter<TripCreatorAdapterTwo.ViewHolder> {
    static TripCreatorAdapterTwo.OnItemClickListener mItemClickListener ;
    Context context = null;
    public TripCreatorAdapterTwo(Context context)
    {
        this.context =context;
    }
@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.headerlayout, parent,false);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout1, parent,false);
        }

        return new ViewHolder(view);
        }
    @Override
    public int getItemViewType ( int position ){
        if (position == 0 || position == 3 || position == 6)
            return 0;
        else
            return 1;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Typeface custom_font = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        Typeface dinlight = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        holder.name.setTypeface(custom_font);
        switch (position)
        {
            case 0:
                holder.name.setText("Hidden Gems");
                holder.name.setTypeface(dinlight);
                holder.image.setImageResource(R.drawable.hiddengemicon);
                break;
            case 1:
                holder.name.setText("Gem List");
                holder.image.setImageResource(R.drawable.listviewicon);
                break;
            case 2:
                holder.name.setText("Add gem");
                holder.image.setImageResource(R.drawable.pindropicon);
                break;
            case 3:
                holder.name.setText("Restaurants");
                holder.name.setTypeface(dinlight);
                holder.image.setImageResource(R.drawable.restaurants);
                break;
            case 4:
                holder.name.setText("Restaurants List");
                holder.image.setImageResource(R.drawable.listviewicon);
                break;
            case 5:
                holder.name.setText("Add Restaurant");
                holder.image.setImageResource(R.drawable.pindropicon);
                break;
            case 6:
                holder.name.setText("Paid Activities");
                holder.name.setTypeface(dinlight);
                holder.image.setImageResource(R.drawable.paidactivityicon);
                break;
            case 7:
                holder.name.setText("Activities List");
                holder.image.setImageResource(R.drawable.listviewicon);
                break;
            case 8:
                holder.name.setText("Add Activity");
                holder.image.setImageResource(R.drawable.addhiddengem);
                break;

        }
//            if (position==0) {
//                holder.name.setText("Hidden Gems");
//                holder.image.setImageResource(R.drawable.addhiddengem);
//            }
//            else if (position==1)
//            {
//                holder.name.setText("Paid Activities");
//                holder.image.setImageResource(R.drawable.paidactivities);
//            }
//            else if (position ==2) {
//                holder.name.setText("RestaurantsActivity");
//                holder.image.setImageResource(R.drawable.restaurants);
//            }

            }

    @Override
    public int getItemCount() {
        return 9;
        }
    public void setListener(TripCreatorAdapterTwo.OnItemClickListener listener) {
        mItemClickListener = listener;
    }
    public void bindData(ViewHolder holder)
    {

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