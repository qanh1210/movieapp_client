package com.example.movieappclient.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.movieappclient.MoviePlayerActivity;
import com.example.movieappclient.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class FloatingWidgetService extends Service {

    public FloatingWidgetService() {}

    WindowManager windowManager;
    private View floatingwidget;
    Uri videoUri;
    SimpleExoPlayer exoPlayer;
    PlayerView playerView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String uriStr = intent.getStringExtra("videoUri");
            videoUri = Uri.parse(uriStr);
            if (windowManager != null && floatingwidget.isShown() && exoPlayer != null) {
                windowManager.removeView(floatingwidget);
                windowManager = null;
                floatingwidget = null;

                exoPlayer.setPlayWhenReady(false);
                exoPlayer.release();
                exoPlayer = null;
            }
            final WindowManager.LayoutParams params;
            floatingwidget = LayoutInflater.from(this).inflate(R.layout.custom_pop_up_window, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            } else {
                params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 200;
            params.y = 200;
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.addView(floatingwidget, params);

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            playerView = floatingwidget.findViewById(R.id.playerView);
            ImageView imgViewClose = floatingwidget.findViewById(R.id.imageViewDissmiss);
            ImageView imgViewMaximize = floatingwidget.findViewById(R.id.imageViewMaximize);
            imgViewMaximize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (windowManager != null && floatingwidget.isShown() && exoPlayer != null) {
                        windowManager.removeView(floatingwidget);
                        floatingwidget = null;
                        windowManager = null;
                        exoPlayer.setPlayWhenReady(false);
                        exoPlayer.release();
                        exoPlayer = null;
                        stopSelf();

                        Intent openActivtyIntent = new Intent(FloatingWidgetService.this, MoviePlayerActivity.class);
                        openActivtyIntent.putExtra("videoUri", videoUri.toString());
                        openActivtyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(openActivtyIntent);
                    }
                }
            });

            imgViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (windowManager != null && floatingwidget.isShown() && exoPlayer != null) {
                        windowManager.removeView(floatingwidget);
                        floatingwidget = null;
                        windowManager = null;
                        exoPlayer.setPlayWhenReady(false);
                        exoPlayer.release();
                        exoPlayer = null;
                        stopSelf();
                    }
                }
            });

            playVideos();

            floatingwidget.findViewById(R.id.relativeLayoutpopup).setOnTouchListener(new View.OnTouchListener() {
                private int initialX, initialY;
                private float initialTouchX, initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;

                        case MotionEvent.ACTION_UP:
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(floatingwidget, params);
                            return true;

                    }

                    return false;


                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }
    public void playVideos(){
        try{
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(FloatingWidgetService.this,trackSelector);
            String playerInfo = Util.getUserAgent(this,"VideoPlayer");
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this,playerInfo);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoUri,dataSourceFactory,extractorsFactory,null,null);
            playerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onDestroy(){
        super.onDestroy();
        if(floatingwidget != null){
            windowManager.removeView(floatingwidget);
        }
    }
}
