package com.ugurtekbas.fadingindicator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ugurtekbas.fadingindicatorlibrary.FadingIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment to use customized indicators.
 * Created by Ugur Tekbas
 */
public class CustomizedFragment extends Fragment {

    private CustomizedAdapter adapter;
    @Bind(R.id.viewpagerCustomized) ViewPager viewpagerCustomized;
    @Bind(R.id.circleIndicator) FadingIndicator indicator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView =  inflater.inflate(R.layout.fragment_customized, null);
        ButterKnife.bind(this, fragmentView);

        int[] pics = {
                R.mipmap.pic5,
                R.mipmap.pic6,
                R.mipmap.pic7,
                R.mipmap.pic8
        };

        adapter = new CustomizedAdapter(getContext(),pics);
        viewpagerCustomized.setAdapter(adapter);
        viewpagerCustomized.setCurrentItem(2);
        indicator.setViewPager(viewpagerCustomized);

        return fragmentView;
    }
}