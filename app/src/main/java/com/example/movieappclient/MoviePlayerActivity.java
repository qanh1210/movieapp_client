package com.example.movieappclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.movieappclient.Service.FloatingWidgetService;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
//import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MoviePlayerActivity extends AppCompatActivity  {

    Uri videoUri;
    PlayerView playerView;
    SimpleExoPlayer exoPlayer;
    ExtractorsFactory extractorsFactory;
    ImageView exo_floating_widget;
   // private String uri_str;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_movie_player);
        hideActionBar();
        playerView = findViewById(R.id.playerView);
        exo_floating_widget = findViewById(R.id.exo_floating_widget);

        Intent intent = getIntent();
        if(intent != null){
            String uri_str = intent.getStringExtra("videoUri");
           //String title = intent.getStringExtra("title");
            videoUri = Uri.parse(uri_str);
        }

        exo_floating_widget.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                exoPlayer.setPlayWhenReady(false);
                exoPlayer.release();
                Intent intent = new Intent(MoviePlayerActivity.this, FloatingWidgetService.class);
                intent.putExtra("videoUri",videoUri.toString());
             //   intent.putExtra("title",title);
                startService(intent);
            }
        });

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(getApplicationContext()).build();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);
        extractorsFactory = new DefaultExtractorsFactory();

        try{
            //DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this,playerInfo);
            //  MediaSource mediaSource = new ExtractorMediaSource(videoUri,dataSourceFactory,extractorsFactory,null,null);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,Util.getUserAgent(this,getString(R.string.app_name)));
           // MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);
            MediaSource videoSource = new ExtractorMediaSource(videoUri,dataSourceFactory,extractorsFactory,null,null);
            playerView.setPlayer(exoPlayer);
            exoPlayer.prepare(videoSource);
            exoPlayer.setPlayWhenReady(true);
            playerView.setKeepScreenOn(true);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void hideActionBar(){
        getSupportActionBar().hide();
    }

    private void setFullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(getApplicationContext()).build();
//        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//        exoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);
//        extractorsFactory = new DefaultExtractorsFactory();
//
//        playerView.setPlayer(exoPlayer);
//
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,Util.getUserAgent(this,getString(R.string.app_name)));
//        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);
//
//        exoPlayer.prepare(videoSource);
//        exoPlayer.setPlayWhenReady(true);
//
//        playerView.setKeepScreenOn(true);
//    }

//    private void playVideo(){
//
//    }

    @Override
    protected void onPause() {
        super.onPause();

        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        exoPlayer.release();
    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        exoPlayer.release();
//    }
}