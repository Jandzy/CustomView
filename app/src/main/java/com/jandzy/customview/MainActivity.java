package com.jandzy.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private CustomBannerView banner;
    private int[] imgsID = {
            R.mipmap.one,R.mipmap.two,R.mipmap.three,R.mipmap.four
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banner = (CustomBannerView) findViewById(R.id.banner);
        banner.setValues(imgsID,true);
    }
}
