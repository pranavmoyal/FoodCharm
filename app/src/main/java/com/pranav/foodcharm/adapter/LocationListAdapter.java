package com.pranav.foodcharm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranav.foodcharm.R;
import com.pranav.foodcharm.interfaces.LocationClickListener;
import com.pranav.foodcharm.model.LocationListModel;
import com.pranav.foodcharm.view.TextView;

import java.util.List;

/**
 * Created by pranav on 2/6/17.
 */

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.MyViewHolder> {
    public List<LocationListModel> locationListModels;
    private Context context;
    private final LocationClickListener mListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_country;

        public MyViewHolder(View view) {
            super(view);
            tv_country = (TextView) view.findViewById(R.id.tv_country);
        }
    }

    public LocationListAdapter(Context context, List<LocationListModel> locationListModels, LocationClickListener mListener) {
        this.locationListModels = locationListModels;
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list_item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            holder.tv_country.setText(locationListModels.get(position).getNameWithCode());

            holder.tv_country.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onLocationItemClicked(holder, position, locationListModels.get(position).getCode(), locationListModels.get(position).getNameWithCode());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return locationListModels.size();
    }
}

