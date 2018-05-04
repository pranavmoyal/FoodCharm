package com.pranav.foodcharm.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pranav.foodcharm.R;
import com.pranav.foodcharm.adapter.SlidingImageAdapter;
import com.pranav.foodcharm.fragment.NavigationFragment;

import java.util.Timer;
import java.util.TimerTask;

public class FoodDashBoardActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager intro_images;
    SlidingImageAdapter slidingImageAdapter;
    //private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;

    public static DrawerLayout drawer;
    private NavigationFragment navigationFragment;
    private FrameLayout fl_navigation;

    private int[] mImageResources = {
            R.drawable.bangali,
            R.drawable.gujrati,
            R.drawable.punjabi,
            R.drawable.si,
            R.drawable.punjabi,
            R.drawable.gujrati
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_dash_board_activity);

        initView();
        inIt();
        setMenu();
    }

    private void initView(){
        intro_images = (ViewPager) findViewById(R.id.pager_introduction);
        //pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        drawer = (DrawerLayout) findViewById(R.id.drawer_home);
        fl_navigation = (FrameLayout) findViewById(R.id.fl_navigation);
    }

    private void inIt(){
        slidingImageAdapter = new SlidingImageAdapter(FoodDashBoardActivity.this, mImageResources);
        intro_images.setAdapter(slidingImageAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOffscreenPageLimit(1);
        intro_images.setOnPageChangeListener(this);

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public int NUM_PAGES;

            public void run() {
                if (currentPage == NUM_PAGES-1) {
                    currentPage = 0;
                }
                intro_images.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


       // setUiPageViewController();
    }

    private void setUiPageViewController() {

        dotsCount = slidingImageAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            //pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    private void setMenu() {
        int width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
//        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) fl_navigation.getLayoutParams();
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) fl_navigation.getLayoutParams();
        params.width = width;
        fl_navigation.setLayoutParams(params);

        navigationFragment = new NavigationFragment();
        getFragmentManager().beginTransaction().replace(R.id.fl_navigation, navigationFragment).commit();



        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                try {
                    navigationFragment = new NavigationFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fl_navigation, navigationFragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_DRAGGING) {
                    try {
                        navigationFragment = new NavigationFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fl_navigation, navigationFragment).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
