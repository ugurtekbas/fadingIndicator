package com.ugurtekbas.fadingindicator;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Main activity to host fragments.
 * Created by Ugur Tekbas
 */
public class Main extends AppCompatActivity {

    private ViewPagerAdapter    mAdapter;
    @Bind(R.id.tabLayout)TabLayout tabLayout;
    @Bind(R.id.viewPagerMain)ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
