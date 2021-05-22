package com.example.movieappclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieappclient.Model.MovieItemClickListenerNew;
import com.example.movieappclient.Model.VideoDetails;
import com.example.movieappclient.R;

import java.util.List;

public class MovieShowAdapter extends RecyclerView.Adapter<MovieShowAdapter.MyViewHolder> {
    private Context context;
    private List<VideoDetails> uploadList;

    MovieItemClickListenerNew movieItemClickListenerNew;

    public MovieShowAdapter(Context context, List<VideoDetails> uploadList, MovieItemClickListenerNew movieItemClickListenerNew) {
        this.context = context;
        this.uploadList = uploadList;
        this.movieItemClickListenerNew = movieItemClickListenerNew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item_new,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieShowAdapter.MyViewHolder holder, int position) {
        VideoDetails videoDetails = uploadList.get(position);
        holder.tvTitle.setText(videoDetails.getVideo_name());
        Glide.with(context).load(videoDetails.getVideo_thum()).into(holder.imgMovie);
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       TextView tvTitle;
       ImageView imgMovie;
        ConstraintLayout container;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_movie_title);
            imgMovie = itemView.findViewById(R.id.item_movie_img);
            container = itemView.findViewById(R.id.container);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieItemClickListenerNew.onMovieClick(uploadList.get(getAdapterPosition()),imgMovie);

                }
            });
        }
    }
}
