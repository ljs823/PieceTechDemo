package com.ljs.piecetechdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by ljs on 2018/4/2.
 * Desc:
 */

public class MyVideoActivity extends AppCompatActivity {

    private VideoView video;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_video);
        video=findViewById(R.id.video);

        Vitamio.isInitialized(this);
        video.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(MyVideoActivity.this,"准备完毕",Toast.LENGTH_SHORT).show();
                video.start();
            }
        });
        video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(MyVideoActivity.this,"视频获取失败",Toast.LENGTH_SHORT).show();
                video.stopPlayback();
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        video.stopPlayback();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        video.setVideoPath("rtmp://live.hkstv.hk.lxdns.com/live/hks");
    }
}
