package com.raveltrips.contentcreator.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raveltrips.contentcreator.R;

/**
 * Created by LENOVO on 17-05-2017.
 */

public class OptionsNavigatorAdapter extends android.support.v7.widget.RecyclerView.Adapter<OptionsNavigatorAdapter.ViewHolder> {

    static OptionsNavigatorAdapter.OnItemClickListener mItemClickListener ;
    private String[] listitems;
    private Context mContext;

    public OptionsNavigatorAdapter(Context context, String[] tripsData ){
        mContext = context;
        this.listitems = tripsData;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == 0)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.optionscardunderline, parent,false);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.optionscard, parent,false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("OptionsAdapter",listitems[position]);
        Log.d("OptionsAdapter",String.valueOf(listitems.length));

        holder.option.setText(listitems[position]);
        Typeface dinlight = Typeface.createFromAsset(mContext.getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");

        holder.option.setTypeface(dinlight);
    }

    @Override
    public int getItemViewType ( int position ){
        if (position == 0 || position == 3 )
            return 0;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return listitems.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView option;

        public ViewHolder(View view) {
            super(view);
            option = (TextView) view.findViewById(R.id.homeoption);
            view.setOnClickListener(new View.OnClickListener() {
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
    public void setListener(OptionsNavigatorAdapter.OnItemClickListener listener) {
        mItemClickListener = listener;
    }
}
