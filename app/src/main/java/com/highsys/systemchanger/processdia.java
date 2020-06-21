package com.highsys.systemchanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class processdia extends AppCompatActivity {
    private CustomVideoView videoview;
    Handler h =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
            //  Runtime runtime = Runtime.getRuntime();
            //  try {
            //      runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
            //  } catch (IOException e) {
            //      e.printStackTrace();
            //  }
            finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_processdia);
        //权限


        //
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }
        //
        TextView promsg=findViewById(R.id.promsg);
        TextView protitle=findViewById(R.id.processtitle);
        protitle.setText(settings.processtitle);
        promsg.setText(settings.processmsg);
        new Thread(new Runnable() {
            @Override
            public void run() {
               while (settings.processcache!=1){
                   try {
                       Thread.sleep(200);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
                h.sendEmptyMessage(1);
            }
        }).start();

    }

   @Override
   public void onBackPressed() {
       Toast.makeText(processdia.this,"无为在歧路，不妨等一等",Toast.LENGTH_SHORT).show();
   }

    //防止锁屏或者切出的时候，音乐在播放
  // public void initView(){
  //     //加载视频资源控件
  //     videoview = (CustomVideoView) findViewById(R.id.videoview);
  //     //设置播放加载路径
  //     videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
  //     //播放
  //     videoview.start();
  //     //循环播放
  //     videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
  //         @Override
  //         public void onCompletion(MediaPlayer mediaPlayer) {
  //             videoview.start();
  //         }
  //     });
  // }
}
