package com.highsys.systemchanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.highsys.atms_obj.settings;

public class processdia extends AppCompatActivity {
    public  Animation animation= AnimationUtils.loadAnimation(MyApplication.getContext(),R.anim.push_bottom_in);
    public  Animation animationout= AnimationUtils.loadAnimation(MyApplication.getContext(),R.anim.push_bottom_out);
    public static int isBACKPRESS=1;
    public LinearLayout process_parent;
    public static int BACKPRESS_ENABLE=0;
    public static int BACKPRESS_DISABLE=1;
    public static int PROCESS_FINISH=1;
    public static int PROCESS_RUNNING=0;
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
        setContentView(R.layout.activity_processdia);
        //权限

        //
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }
        //
        animationout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        process_parent=findViewById(R.id.process_parent);
        process_parent.startAnimation(animation);
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
        if (isBACKPRESS==BACKPRESS_ENABLE){
            process_parent.startAnimation(animationout);
        }else {
            Log.d("TAG",String.valueOf(isBACKPRESS));
            Toast.makeText(processdia.this,"无为在歧路，不妨等一等",Toast.LENGTH_SHORT).show();
        }

   }
   public static void setIsBACKPRESS(int i){
        isBACKPRESS=i;
   }
   public static void setProcess(int i ){
        settings.processcache=i;
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

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
public static void showPrecessWindows(Context context,String title,String msg,int isBACKPRESS_Enable){
        settings.processtitle=title;
        settings.processmsg=msg;
        settings.processcache=0;
        isBACKPRESS=isBACKPRESS_Enable;
    Intent i =new Intent(context,processdia.class);
    context.startActivity(i);
}


}
