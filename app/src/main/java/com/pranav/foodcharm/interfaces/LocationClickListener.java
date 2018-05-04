package com.pranav.foodcharm.interfaces;

import com.pranav.foodcharm.adapter.LocationListAdapter;

/**
 * Created by parshant on 2/6/17.
 */

public interface LocationClickListener {
    void onLocationItemClicked(LocationListAdapter.MyViewHolder holder, int position, String countryCode, String nameWithCode);
}
