package com.ugurtekbas.fadingindicator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ugurtekbas.fadingindicatorlibrary.FadingIndicator;
import com.ugurtekbas.fadingindicatorlibrary.PageChangedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment to use default state of indicators.
 * Created by Ugur Tekbas
 */
public class DefaultFragment extends Fragment implements PageChangedListener{

    private DefaultAdapter adapter;
    @BindView(R.id.viewpagerDefault) ViewPager viewpagerDefault;
    @BindView(R.id.circleIndicator) FadingIndicator indicator;

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
        View fragmentView =  inflater.inflate(R.layout.fragment_default, null);
        ButterKnife.bind(this, fragmentView);

        int[] pics = {
                R.mipmap.pic1,
                R.mipmap.pic2,
                R.mipmap.pic3,
                R.mipmap.pic4
        };

        adapter = new DefaultAdapter(getContext(),pics);
        viewpagerDefault.setAdapter(adapter);
        indicator.setViewPager(viewpagerDefault);
        indicator.setPageListener(this);

        return fragmentView;
    }

    @Override
    public void onPageFlipped(int pageIndex) {
        Log.i("Page is flipped ", "page index:"+ pageIndex);
    }
}
