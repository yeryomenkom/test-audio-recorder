package com.yeryomenkom.testaudiorecorder;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_MA);
        setupViewPager();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_MA);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //fix library bug
        mViewPager.clearOnPageChangeListeners();
        mViewPager.addOnPageChangeListener(new FixedOnPageChangedListener(tabLayout));

        startService(new Intent(this, RecorderService.class));
        startService(new Intent(this, PlayerService.class));
    }

    private void setupViewPager() {
        mPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        mPagerAdapter.addFragment(new RecorderFragment(), getString(R.string.tittle_recorder));
        mPagerAdapter.addFragment(new RecordsFragment(), getString(R.string.tittle_records));
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPageTransformer(false, new CustomPageTransformerFromGoogleDevelopers());
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
