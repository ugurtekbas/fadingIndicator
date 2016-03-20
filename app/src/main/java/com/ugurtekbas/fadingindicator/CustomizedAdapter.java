package com.ugurtekbas.fadingindicator;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CustomizedAdapter extends PagerAdapter{

    private Context mContext;
    private int[] mPics;
    private LayoutInflater inflater;

    public CustomizedAdapter(Context context, int[] pics) {
        super();
        this.mContext = context;
        this.mPics = pics;
    }

    @Override
    public int getCount() {
        return mPics.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView ivPics;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item,container,false);
        ivPics  =   (ImageView)itemView.findViewById(R.id.ivPager);
        ivPics.setImageResource(mPics[position]);

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
