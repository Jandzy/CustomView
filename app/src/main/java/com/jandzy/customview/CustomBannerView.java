package com.jandzy.customview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrazy on 2016/11/29.
 */
public class CustomBannerView extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private static final String TAG = "CustomBannerView";

    private List<ImageView> listImageViews = new ArrayList<>();

    //自动滑动间隔时间
    private long delayMillis = 2000;

    //用于存放原点
    private List<View> listpoints = new ArrayList<>();

    //水平的linearlayout来存放原点显示
    private LinearLayout mLinearLayout;

    private ViewPager mViewPager;
    private CustomBannerAdapter mCustomBannerAdapter;
    private int mCurPosition;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCurPosition++;
            mViewPager.setCurrentItem(mCurPosition);
            mHandler.sendEmptyMessageDelayed(0, delayMillis);
        }
    };


    public CustomBannerView(Context context) {
        this(context, null);
    }

    public CustomBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
        initAction();

        mHandler.sendEmptyMessageDelayed(0, delayMillis);
    }


    private void initView(Context context) {
        mViewPager = new ViewPager(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(layoutParams);
        addView(mViewPager, layoutParams);

        mLinearLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mLinearLayout.setLayoutParams(params);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER);

        addView(mLinearLayout, params);
    }

    private void initAction() {
        mCustomBannerAdapter = new CustomBannerAdapter();
        mViewPager.setAdapter(mCustomBannerAdapter);
        mViewPager.addOnPageChangeListener(this);
//        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(Integer.MAX_VALUE/2);

    }


    public void setValues(int[] imgsID, boolean isShowPoints) {

       /* *//**
         * 设置listimageview数据
         * 为实现循环，将数组变成这样的结构
         * 0  1   2   3   4   5
         * a   b   c   d         //原来的数组
         * d  a   b   c   d   a     //修改过的数组
         * 当滑动到5的位置时候切换到1的位置，来实现循环
         *
         * @param imgsID
         *//*
        for (int i = 0; i < imgsID.length + 2; i++) {
            ImageView imageView = new ImageView(getContext());

            if (i == 0) {
                imageView.setImageDrawable(getResources().getDrawable(imgsID[imgsID.length - 1]));
            } else if (i == imgsID.length + 1) {
                imageView.setImageDrawable(getResources().getDrawable(imgsID[0]));
            } else {
                imageView.setImageDrawable(getResources().getDrawable(imgsID[i - 1]));
            }}*/
        for (int i = 0; i < imgsID.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(getResources().getDrawable(imgsID[i]));
            listImageViews.add(imageView);
        }


        //如果显示points
        if (isShowPoints) {
            for (int j = 0; j < imgsID.length; j++) {
                View view = new View(getContext());
                LinearLayout.LayoutParams viewp = new LinearLayout.LayoutParams(40, 40);
                viewp.setMargins(0, 10, 10, 10);
                view.setLayoutParams(viewp);
                view.setBackgroundColor(Color.WHITE);
                mLinearLayout.addView(view);
            }

            mLinearLayout.getChildAt(Integer.MAX_VALUE/2%listImageViews.size()).setBackgroundColor(Color.RED);

        }

        mCustomBannerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurPosition = position;
        Log.e("mCurPosition", "onPageSelected: "+mCurPosition);
        changePointColor();

    }

    private void changePointColor() {
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            if (i == mCurPosition %listImageViews.size()) {
                mLinearLayout.getChildAt(i).setBackgroundColor(Color.RED);
            } else {
                mLinearLayout.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
        }
    }


    //手动滑动viewpager改变时才会调用此方法，setCurrentItem不会调用该方法。
    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class CustomBannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.e(TAG, "destroyItem: "+position );
            container.removeView(listImageViews.get(position%listImageViews.size()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.e(TAG, "instantiateItem: "+position );
            container.addView(listImageViews.get(position%listImageViews.size()));
            return listImageViews.get(position%listImageViews.size());
        }
    }

}
