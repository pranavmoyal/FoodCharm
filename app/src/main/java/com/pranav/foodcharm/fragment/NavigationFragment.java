package com.pranav.foodcharm.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pranav.foodcharm.R;
import com.pranav.foodcharm.adapter.NavigationFragmentAdapter;
import com.pranav.foodcharm.view.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by parshant on 15/5/17.
 */

public class NavigationFragment extends Fragment implements View.OnClickListener {
    private FragmentActivity mActivity;
    private RecyclerView rv_nav;
    private TextView tv_address;
    public TextView tv_name;
    public CircleImageView iv_profile;
    public LinearLayout ll_nav_sports;
    private NavigationFragmentAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout rl_profile_header;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivity = (FragmentActivity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            mActivity = (FragmentActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation_fragment_view, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView(view);
        setListeners();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setListeners() {
        rl_profile_header.setOnClickListener(this);
    }

    private void initView(View view) {
        rv_nav = (RecyclerView) view.findViewById(R.id.rv_nav);
        iv_profile = (CircleImageView) view.findViewById(R.id.iv_profile);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        ll_nav_sports = (LinearLayout) view.findViewById(R.id.ll_nav_sports);
        rl_profile_header = (RelativeLayout) view.findViewById(R.id.rl_profile_header);

        mAdapter = new NavigationFragmentAdapter(mActivity);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        rv_nav.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(mActivity);                 // Creating a layout Manager
        rv_nav.setLayoutManager(mLayoutManager);
        rv_nav.setNestedScrollingEnabled(false);
        rv_nav.hasFixedSize();
    }

    @Override
    public void onResume() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_profile_header:
               /* if (mActivity instanceof UserDashBoard)
                    ((UserDashBoard) mActivity).drawer.closeDrawer(Gravity.LEFT);
                else {
                   *//* Intent intent = new Intent(mActivity, UserDashBoard.class);
                    startActivity(intent);
                    mActivity.finish();*/

                break;
        }
    }
}
