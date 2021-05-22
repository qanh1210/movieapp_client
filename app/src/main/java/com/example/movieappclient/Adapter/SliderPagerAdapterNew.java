package com.example.movieappclient.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.movieappclient.Model.SliderSide;
import com.example.movieappclient.Model.VideoDetails;
import com.example.movieappclient.MoviePlayerActivity;
import com.example.movieappclient.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SliderPagerAdapterNew extends PagerAdapter {

    private Context context;
    List<VideoDetails> videoDetailsList;

    public SliderPagerAdapterNew(Context context, List<VideoDetails> videoDetailsList) {
        this.context = context;
        this.videoDetailsList = videoDetailsList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
      /*  context = container.getContext();
        final AppCompatImageView imageView = new AppCompatImageView(context);
        container.addView(imageView);*/

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout = inflater.inflate(R.layout.slide_item,null);
        ImageView slideimage = slideLayout.findViewById(R.id.slide_img);
        TextView slidetitle = slideLayout.findViewById(R.id.slide_title);
        FloatingActionButton floatingActionButton = slideLayout.findViewById(R.id.floatingActionButton);
       // slideimage.setImageResource(Integer.parseInt(sliderSideList.get(position).getVideo_thum()));
        Glide.with(context).load(videoDetailsList.get(position).getVideo_thum()).into(slideimage);
   //     Glide.with(context).load(sliderSideList.get(position).getVideo_thum()).into(imageView);

        slidetitle.setText(videoDetailsList.get(position).getVideo_name());

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String video_url = videoDetailsList.get(position).getVideo_url();
                String title = videoDetailsList.get(position).getVideo_name();
                Intent intent = new Intent(context, MoviePlayerActivity.class);

                intent.putExtra("videoUri",video_url);
                intent.putExtra("title",title);
                v.getContext().startActivity(intent);
            }
        });

        container.addView(slideLayout);

        return slideLayout;
    }

    @Override
    public int getCount() {
        return videoDetailsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
