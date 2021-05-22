package com.example.movieappclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movieappclient.Adapter.MovieShowAdapter;
import com.example.movieappclient.Model.MovieItemClickListenerNew;
import com.example.movieappclient.Model.VideoDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements MovieItemClickListenerNew {
        private ImageView MoviesThumbnail, MoviesCoverImg;
        TextView tv_title, tv_description;
        FloatingActionButton play_btn;
        RecyclerView Rv_Cast, Rv_SimilarMovies;
        MovieShowAdapter movieShowAdapter;
        DatabaseReference databaseReference;
        private List<VideoDetails> videoDetailsList, actionMovies, sportMovies, comedyMovies, romanticMovies, adventureMovies;
        String current_video_url;
        String current_video_category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        inView();
        similarmoviesRecycler();
        similarMovies();

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inView = new Intent(MovieDetailActivity.this,MoviePlayerActivity.class);
              //  String movieURL = getIntent().getExtras().getString("movieURL");
               // current_video_url = movieURL;
                inView.putExtra("videoUri",current_video_url);
                v.getContext().startActivity(inView);

            }
        });
    }

    private void similarMovies() {
        if(current_video_category.equals("Action")){
            movieShowAdapter = new MovieShowAdapter(this,actionMovies,this );
            Rv_SimilarMovies.setAdapter(movieShowAdapter);
            Rv_SimilarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            movieShowAdapter.notifyDataSetChanged();
        }
        else if(current_video_category.equals("Adventure")){
            movieShowAdapter = new MovieShowAdapter(this,adventureMovies,this );
            Rv_SimilarMovies.setAdapter(movieShowAdapter);
            Rv_SimilarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            movieShowAdapter.notifyDataSetChanged();
        }

        if(current_video_category.equals("Comedy")){
            movieShowAdapter = new MovieShowAdapter(this,comedyMovies,this );
            Rv_SimilarMovies.setAdapter(movieShowAdapter);
            Rv_SimilarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            movieShowAdapter.notifyDataSetChanged();
        }

        else if(current_video_category.equals("Romantics")){
            movieShowAdapter = new MovieShowAdapter(this,romanticMovies,this);
            Rv_SimilarMovies.setAdapter(movieShowAdapter);
            Rv_SimilarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            movieShowAdapter.notifyDataSetChanged();
        }

        if(current_video_category.equals("Sport")){
            movieShowAdapter = new MovieShowAdapter(this,sportMovies,this);
            Rv_SimilarMovies.setAdapter(movieShowAdapter);
            Rv_SimilarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            movieShowAdapter.notifyDataSetChanged();
        }

    }

    private void similarmoviesRecycler() {
        videoDetailsList = new ArrayList<>();
        actionMovies = new ArrayList<>();
        sportMovies = new ArrayList<>();
        adventureMovies = new ArrayList<>();
        comedyMovies = new ArrayList<>();
        romanticMovies = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("videos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    VideoDetails list = postSnapshot.getValue(VideoDetails.class);
                    if(list.getVideo_category().equals("Action")){
                        actionMovies.add(list);
                    } else if(list.getVideo_category().equals("Sports")){
                        sportMovies.add(list);
                    }if(list.getVideo_category().equals("Adventure")){
                        adventureMovies.add(list);
                    } else if(list.getVideo_category().equals("Comedy")){
                        comedyMovies.add(list);
                    }
                    if(list.getVideo_category().equals("Romantics")){
                        romanticMovies.add(list);
                    }
                    videoDetailsList.add(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inView() {
        play_btn = findViewById(R.id.play_fab);
        tv_title = findViewById(R.id.detail_movie_title);
        tv_description = findViewById(R.id.detail_movie_desc);
        MoviesThumbnail = findViewById(R.id.detail_movie_img);
        MoviesCoverImg = findViewById(R.id.detail_movie_cover);
        Rv_SimilarMovies = findViewById(R.id.recycler_similar_movies);

        String movieTitle = getIntent().getExtras().getString("title");
        String imgURL = getIntent().getExtras().getString("imgURL");
        String imgCover = getIntent().getExtras().getString("imgCover");
        String movieDescription = getIntent().getExtras().getString("movieDetails");
        String movieURL = getIntent().getExtras().getString("movieURL");
        String movieCategory = getIntent().getExtras().getString("movieCategory");

        current_video_url = movieURL;
        current_video_category = movieCategory;

        Glide.with(this).load(imgURL).into(MoviesThumbnail);
        Glide.with(this).load(imgCover).into(MoviesCoverImg);

        tv_title.setText(movieTitle);
        tv_description.setText(movieDescription);

        getSupportActionBar().setTitle(movieTitle);

        //set up animation
        MoviesCoverImg.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));
        play_btn.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));

    }

    @Override
    public void onMovieClick(VideoDetails videoDetails, ImageView imageView) {
        tv_title.setText(videoDetails.getVideo_name());
        getSupportActionBar().setTitle(videoDetails.getVideo_name());
        Glide.with(this).load(videoDetails.getVideo_thum()).into(MoviesThumbnail);
        Glide.with(this).load(videoDetails.getVideo_thum()).into(MoviesCoverImg);

        MoviesCoverImg.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));
        play_btn.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));

        tv_description.setText(videoDetails.getVideo_description());

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MovieDetailActivity.this,imageView,"sharedName");
        options.toBundle();

        current_video_url = videoDetails.getVideo_url();
        current_video_category = videoDetails.getVideo_category();



    }
}