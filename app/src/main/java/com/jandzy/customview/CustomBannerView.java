package com.jandzy.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
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
public class CustomBannerView extends RelativeLayout implements ViewPager.OnPageChangeListener{

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

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mCurPosition==listImageViews.size()-2) {
                mCurPosition = 1;
            }else{
                mCurPosition ++;
            }
            mViewPager.setCurrentItem(mCurPosition,false);
            mHandler.sendEmptyMessageDelayed(0,delayMillis);
        }
    };

    public CustomBannerView(Context context) {
        this(context,null);
    }

    public CustomBannerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
        initAction();

        mHandler.sendEmptyMessageDelayed(0,delayMillis);
    }


    private void initView(Context context) {
        mViewPager = new ViewPager(context);
        ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
        layoutParams.height = ViewPager.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewPager.LayoutParams.MATCH_PARENT;
        mViewPager.setLayoutParams(layoutParams);
        addView(mViewPager);

        mLinearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(params);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        mLinearLayout.setBackgroundColor(Color.GREEN);
//        mLinearLayout.set
        addView(mLinearLayout);
    }

    private void initAction() {
        mCustomBannerAdapter = new CustomBannerAdapter();
        mViewPager.setAdapter(mCustomBannerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 设置listimageview数据
     * 为实现循环，将数组变成这样的结构
     *  0  1   2   3   4   5
     *     a   b   c   d         //原来的数组
     *  d  a   b   c   d   a     //修改过的数组
     *  当滑动到5的位置时候切换到1的位置，来实现循环
     * @param imgsID
     */
    public void setValues(int[] imgsID){
        for(int i=0;i<imgsID.length+2;i++){
            ImageView imageView = new ImageView(getContext());
            if(i==0){
                imageView.setImageDrawable(getResources().getDrawable(imgsID[imgsID.length-1]));
            }else if(i == imgsID.length+1){
                imageView.setImageDrawable(getResources().getDrawable(imgsID[0]));
            }else{
                imageView.setImageDrawable(getResources().getDrawable(imgsID[i-1]));
            }
            listImageViews.add(imageView);
        }

        for(int j=0;j<imgsID.length;j++){
            View view = new View(getContext());
            LinearLayout.LayoutParams viewp = new LinearLayout.LayoutParams(20,20);
            viewp.setMargins(5,5,35,35);
            view.setLayoutParams(viewp);
            setViewBackground(view);
            mLinearLayout.addView(view);
        }
        mCustomBannerAdapter.notifyDataSetChanged();
    }

    //设置原点
    private void  setViewBackground(final View view){
        Drawable drawable = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawCircle(view.getWidth()/2,view.getHeight()/2,100,new Paint());
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
      view.setBackground(drawable);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurPosition = position;
    }

    //手动滑动viewpager改变时才会调用此方法，setCurrentItem不会调用该方法。
    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:

                if(mCurPosition == 0){
                    mViewPager.setCurrentItem(listImageViews.size()-2,false);
                }else{
                    if(mCurPosition == listImageViews.size()-1){
                        mViewPager.setCurrentItem(1,false);
                    }
                }

                break;
        }

    }


    class CustomBannerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return listImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView(listImageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(listImageViews.get(position));
            return listImageViews.get(position);
        }
    }

}
