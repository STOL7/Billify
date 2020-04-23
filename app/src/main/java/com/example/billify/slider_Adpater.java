package com.example.billify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class slider_Adpater extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

   public slider_Adpater(Context context){
       this.context =context;
   }

   int slider_Image[]={
           R.mipmap.all,
           R.mipmap.add_expance,
           R.mipmap.history,
           R.mipmap.history_detail,
           R.mipmap.detail_black
   };

   String Title[] ={
           "Welcome To BilliFy",
           "Add Your Daily Expences",
           "Check Your Expence History",
           "Trace Your History Details Any Time",
           "Enjoy Dark Mode"
   };
    @Override
    public int getCount() {
        return slider_Image.length;
    }

    @Override
    public boolean isViewFromObject( View view,Object object) {
        return view == (ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem( ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.slidertext);

        imageView.setImageResource(slider_Image[position]);
        textView.setText(Title[position]);

        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem( ViewGroup container, int position, Object object) {
      container.removeView((ConstraintLayout)object);
    }
}
