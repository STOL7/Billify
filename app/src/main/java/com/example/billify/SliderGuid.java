package com.example.billify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SliderGuid extends AppCompatActivity {
    private ViewPager pager;
    private LinearLayout linearLayout;
    private  slider_Adpater slider_adpater;
    private  TextView[] dots;
    private Button skip;
    private Button letsgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_guid);

        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putBoolean("fresh",true);
        editor.commit();

        skip = (Button) findViewById(R.id.skip);
        letsgo = (Button) findViewById(R.id.countinue);



        pager = (ViewPager)findViewById(R.id.pager);
        linearLayout = (LinearLayout)findViewById(R.id.linear);

        slider_adpater= new slider_Adpater(this);

        pager.setAdapter(slider_adpater);

        dotcount(0);

        pager.addOnPageChangeListener(viewListner );

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderGuid.this, MainActivity.class);
                startActivity(intent);
            }
        });

        letsgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderGuid.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void dotcount(int position)
    {
        dots = new TextView[5];
        linearLayout.removeAllViews();

        for (int i=0;i<dots.length;i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.black));

            linearLayout.addView(dots[i]);
        }
        if(position==dots.length-1)
        {
            skip.setVisibility(View.INVISIBLE);
            letsgo.setVisibility(View.VISIBLE);
        }
        else {
            skip.setVisibility(View.VISIBLE);
            letsgo.setVisibility(View.INVISIBLE);
        }
        if(dots.length>0)
        {
            dots[position].setTextColor(getResources().getColor(R.color.white));
        }

    }


    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            dotcount(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
