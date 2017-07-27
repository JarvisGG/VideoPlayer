package com.jarvis.videoplayer.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.jarvis.videoplayer.R;
import com.jarvis.videoplayer.adapter.HomeAdapter;
import com.jarvis.videoplayer.fragment.BaseFragment;
import com.jarvis.videoplayer.fragment.HomeFragment;
import com.jarvis.videoplayer.fragment.VideoFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    private MenuItem menuItem;

    private HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_view);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container_view);

        adapter = new HomeAdapter(getSupportFragmentManager());
        adapter.addFragment(HomeFragment.newInstance("home"));
        adapter.addFragment(VideoFragment.newInstance("video"));
        adapter.addFragment(BaseFragment.newInstance("user"));

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = mBottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                @StringRes int text;
                switch (item.getItemId()) {
                    case R.id.recents:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.favourites:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.nearby:
                        mViewPager.setCurrentItem(2);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    public void enterTinyWindow(View view) {
    }

    public void showVideoList(View view) {
        startActivity(new Intent(this, RecyclerViewActivity.class));
    }
}
