package com.example.movieappclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.movieappclient.Adapter.MovieShowAdapter;
import com.example.movieappclient.Adapter.SliderPagerAdapterNew;
import com.example.movieappclient.Model.MovieItemClickListenerNew;
import com.example.movieappclient.Model.SliderSide;
import com.example.movieappclient.Model.VideoDetails;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MovieItemClickListenerNew {
    MovieShowAdapter movieShowAdapter;
    DatabaseReference databaseReference;
    private List<VideoDetails> uploads, uploadListLatest, uploadListPopular;
    private List<VideoDetails> action_movies, sport_movies, comedy_movies, romantic_movies, adventure_movies;
    private ViewPager slidePager;
    private List<SliderSide> uploadSlider;
    private TabLayout indicator, tab_movies_action;
    private RecyclerView moviesRv, moviesRvWeek, tab;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        progressDialog = new ProgressDialog(this);

        inViews();
        addAllMovies();
        iniPopularMovies();
        iniWeekMovies();
        moviesViewTab();
      //  iniSlider();

    }

    private void addAllMovies(){
        uploads = new ArrayList<>();
        uploadListLatest = new ArrayList<>();
        uploadListPopular = new ArrayList<>();
        action_movies = new ArrayList<>();
        adventure_movies = new ArrayList<>();
        comedy_movies = new ArrayList<>();
        sport_movies = new ArrayList<>();
        romantic_movies = new ArrayList<>();
        uploadSlider = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("videos");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    VideoDetails videoDetails = postSnapshot. getValue(VideoDetails.class);
                    SliderSide slide = postSnapshot.getValue(SliderSide.class);
                    if(videoDetails.getVideo_type().equals("Latest movies")){
                        uploadListLatest.add(videoDetails);
                    }

                    else if(videoDetails.getVideo_type().equals("Best popular movies")){
                        uploadListPopular.add(videoDetails);
                    }

                    if(videoDetails.getVideo_category().equals("Action")){
                        action_movies.add(videoDetails);
                    }

                    else if(videoDetails.getVideo_category().equals("Adventure")){
                        adventure_movies.add(videoDetails);
                    }

                    if(videoDetails.getVideo_category().equals("Comedy")){
                        comedy_movies.add(videoDetails);
                    }

                   else if(videoDetails.getVideo_category().equals("Romantics")){
                        romantic_movies.add(videoDetails);
                    }

                    if(videoDetails.getVideo_category().equals("Sports")){
                        sport_movies.add(videoDetails);
                    }

                    uploads.add(videoDetails);
                }
                iniSlider();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });
    }

    private void iniSlider(){
        SliderPagerAdapterNew adapterNew = new SliderPagerAdapterNew(this, uploads);
        slidePager.setAdapter(adapterNew);
        adapterNew.notifyDataSetChanged();

        //set up timer
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),4000,6000);
        indicator.setupWithViewPager(slidePager, true);
    }

    private void iniWeekMovies(){
        movieShowAdapter = new MovieShowAdapter(this,uploadListLatest,this);
        moviesRvWeek.setAdapter(movieShowAdapter);
        moviesRvWeek.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

        movieShowAdapter.notifyDataSetChanged();
    }

    private void iniPopularMovies(){
        movieShowAdapter = new MovieShowAdapter(this,uploadListPopular,this);
        moviesRv.setAdapter(movieShowAdapter);
        moviesRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

        movieShowAdapter.notifyDataSetChanged();
    }

    private void getActionMovies(){
        movieShowAdapter = new MovieShowAdapter(this,action_movies,this);
        //adding adapter to recyclerview
        tab.setAdapter(movieShowAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        movieShowAdapter.notifyDataSetChanged();
    }

    private void getSportMovies(){
        movieShowAdapter = new MovieShowAdapter(this,sport_movies,this);
        tab.setAdapter(movieShowAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

        movieShowAdapter.notifyDataSetChanged();
    }

    private void getRomanticMovies(){
        movieShowAdapter = new MovieShowAdapter(this,romantic_movies,this);
        tab.setAdapter(movieShowAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

        movieShowAdapter.notifyDataSetChanged();
    }

    private void getComedyMovies(){
        movieShowAdapter = new MovieShowAdapter(this,comedy_movies,this);
        tab.setAdapter(movieShowAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

        movieShowAdapter.notifyDataSetChanged();
    }

    private void getAdventureMovies(){
        movieShowAdapter = new MovieShowAdapter(this,adventure_movies,this);
        tab.setAdapter(movieShowAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

        movieShowAdapter.notifyDataSetChanged();
    }


    private void moviesViewTab(){
        tab_movies_action.addTab(tab_movies_action.newTab().setText("Action"));
        tab_movies_action.addTab(tab_movies_action.newTab().setText("Adventure"));
        tab_movies_action.addTab(tab_movies_action.newTab().setText("Comedy"));
        tab_movies_action.addTab(tab_movies_action.newTab().setText("Romantic"));
        tab_movies_action.addTab(tab_movies_action.newTab().setText("Sport"));

        tab_movies_action.setTabGravity(TabLayout.GRAVITY_FILL);
        tab_movies_action.setTabTextColors(ColorStateList.valueOf(Color.WHITE));

        tab_movies_action.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getActionMovies();
                        break;
                    case 1:
                        getAdventureMovies();
                        break;
                    case 2:
                        getComedyMovies();
                        break;
                    case 3:
                        getRomanticMovies();
                        break;
                    case 4:
                        getSportMovies();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void inViews(){
        tab_movies_action = findViewById(R.id.tabActionMovies);
        slidePager = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);
        moviesRv = findViewById(R.id.Rv_movies);
        moviesRvWeek = findViewById(R.id.rv_movies_week);
        tab = findViewById(R.id.tabrecyler);
    }

    @Override
    public void onMovieClick(VideoDetails videoDetails, ImageView imageView) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        intent.putExtra("title", videoDetails.getVideo_name());
        intent.putExtra("imgURL", videoDetails.getVideo_thum());
        intent.putExtra("imgCover", videoDetails.getVideo_thum());
        intent.putExtra("movieDetails",videoDetails.getVideo_description());
        intent.putExtra("movieURL", videoDetails.getVideo_url());
        intent.putExtra("movieCategory", videoDetails.getVideo_category());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                imageView, "sharedName");
        startActivity(intent,options.toBundle());



    }

    public class SliderTimer  extends TimerTask {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(slidePager.getCurrentItem() < uploadSlider.size()-1){
                        slidePager.setCurrentItem(slidePager.getCurrentItem()+1);

                    }
                    else {
                        slidePager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}