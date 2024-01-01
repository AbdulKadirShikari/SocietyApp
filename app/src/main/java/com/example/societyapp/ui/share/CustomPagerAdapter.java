package com.example.societyapp.ui.share;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.example.societyapp.R;
import com.example.societyapp.ui.home.SliderImageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<SliderImageModel> imageList=new ArrayList<SliderImageModel>();
    LayoutInflater layoutInflater;


    public CustomPagerAdapter(Context context, ArrayList<SliderImageModel>imageList) {
        this.context = context;
        this.imageList = imageList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.image_slider_layout_item, container, false);
        SliderImageModel imageSlider = imageList.get(position);
        String imageslider=imageSlider.sliderimage;
        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_auto_image_slider);
        Picasso.get().load("http://placementsadda.com/Society/uploads/"+imageslider).into(imageView);
        Log.e("checkImageSlider","Test::"+imageSlider);

        container.addView(itemView);

        //listening to image click

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}