package com.highsys.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.highsys.atms_obj.settings;
import com.highsys.systemchanger.MyApplication;
import com.highsys.systemchanger.R;
import com.highsys.tool.Actions;
import com.highsys.tool.CustomVideoView;

import java.security.PublicKey;

import pl.droidsonroids.gif.GifImageView;

public class processdia extends AppCompatActivity {
    public  Animation animation= AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.push_bottom_in);
    public  Animation animationout= AnimationUtils.loadAnimation(MyApplication.getContext(),R.anim.push_bottom_out);
    public static int isBACKPRESS=1;
    public LinearLayout process_parent;
    public static int BACKPRESS_ENABLE=0;
    public static GifImageView gifImageView;
    private Button leftbutton;
    public static TextView protitle;
    public static TextView promsg;
    public static Actions leftaction;
    public static Actions rightaction;
    public static Activity processdia_instance;
    public static boolean GIFENABLE=true;
    private Button rightbutton;
    public static int BACKPRESS_DISABLE=1;
    public static int PROCESS_FINISH=1;
    public static int PROCESS_RUNNING=0;
    private CustomVideoView videoview;
    private static boolean iS_LEFT_BUTTON_PRESSED_ENABLE=true;
    private static boolean iS_RIGHT_BUTTON_PRESSED_ENABLE=true;

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
        leftbutton=findViewById(R.id.leftbutton);
        rightbutton=findViewById(R.id.rightbutton);
        gifImageView=findViewById(R.id.processgif);
        process_parent=findViewById(R.id.process_parent);
        process_parent.startAnimation(animation);
        promsg=findViewById(R.id.promsg);
        protitle=findViewById(R.id.processtitle);
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
        leftbutton.setVisibility(View.GONE);
        rightbutton.setVisibility(View.GONE);
        gifImageView.setVisibility(View.VISIBLE);
        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iS_LEFT_BUTTON_PRESSED_ENABLE){
                    leftaction.action();
                }

            }
        });
        rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iS_RIGHT_BUTTON_PRESSED_ENABLE){
                    rightaction.action();
                }

            }
        });
        //
        Intent i =getIntent();
        if (i.getBooleanExtra("isLeftButtonEnable",false)==true){
            leftbutton.setVisibility(View.VISIBLE);
            leftbutton.setText(i.getStringExtra("LeftButtonTip"));
        }
        if (i.getBooleanExtra("isRightButtonEnable",false)==true){
            rightbutton.setVisibility(View.VISIBLE);
            rightbutton.setText(i.getStringExtra("RightButtonTip"));
        }
        if (i.getBooleanExtra("isGifEnable",true)==false){
            gifImageView.setVisibility(View.GONE);
        }
        processdia_instance=this;
        BUTTON_PRESS_ENABLE_LOOPER();
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
   public static void setProtitle(final String title){
       ((Activity) processdia.processdia_instance).runOnUiThread(new Thread(new Runnable() {
           @Override
           public void run() {
               Log.d("XX","setProtitle");
               processdia.protitle.setText(title);
           }
       }));
   }
    public static void setPromsg(final String msg){
        ((Activity) processdia.processdia_instance).runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("XX","setPromsg : "+msg);
                processdia.promsg.setText(msg);
            }
        }));
    }
    public static void closeGIF(final Boolean is){
        ((Activity) processdia.processdia_instance).runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                if (is){
                    Log.d("XX","closeGIF");
                    processdia.gifImageView.setVisibility(View.GONE);
                }else{
                    Log.d("XX","openGIF");
                    processdia.gifImageView.setVisibility(View.VISIBLE);
                }
            }
        }));
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
    public static void showPrecessWindows(Context context,String title,String msg,int isBACKPRESS_Enable,boolean isprocessgif){
        GIFENABLE=isprocessgif;
        settings.processtitle=title;
        settings.processmsg=msg;
        settings.processcache=0;
        isBACKPRESS=isBACKPRESS_Enable;
        Intent i =new Intent(context,processdia.class);
        i.putExtra("isLeftButtonEnable",false);
        i.putExtra("isRightButtonEnable",false);
        i.putExtra("isGifEnable",isprocessgif);
        context.startActivity(i);
    }
    public static void showPrecessWindows(Context context,String title,String msg,int isBACKPRESS_Enable,boolean isprocessgif,Actions doaction){
        GIFENABLE=isprocessgif;
        settings.processtitle=title;
        settings.processmsg=msg;
        settings.processcache=0;
        isBACKPRESS=isBACKPRESS_Enable;
        Intent i =new Intent(context,processdia.class);
        i.putExtra("isLeftButtonEnable",false);
        i.putExtra("isRightButtonEnable",false);
        i.putExtra("isGifEnable",isprocessgif);
        context.startActivity(i);
        doaction.action();
    }
    public static void showPrecessWindows(Context context,String title,String msg,int isBACKPRESS_Enable,boolean isprocessgif,Actions doaction,Actions rightaction_,String righttip){
        rightaction=rightaction_;

        GIFENABLE=isprocessgif;
        settings.processtitle=title;
        settings.processmsg=msg;
        settings.processcache=0;
        isBACKPRESS=isBACKPRESS_Enable;
        Intent i =new Intent(context,processdia.class);
        i.putExtra("isLeftButtonEnable",false);
        i.putExtra("isRightButtonEnable",true);
        i.putExtra("RightButtonTip",righttip);
        i.putExtra("isGifEnable",isprocessgif);
        context.startActivity(i);
        doaction.action();
    }
    public static void showPrecessWindows(Context context, String title, String msg, int isBACKPRESS_Enable, Actions ac,String tip){

        GIFENABLE=false;
        leftaction=ac;
        settings.processtitle=title;
        settings.processmsg=msg;
        settings.processcache=0;
        isBACKPRESS=isBACKPRESS_Enable;
        Intent i =new Intent(context,processdia.class);
        i.putExtra("isLeftButtonEnable",true);
        i.putExtra("isRightButtonEnable",false);
        i.putExtra("LeftButtonTip",tip);
        i.putExtra("isGifEnable",false);
        context.startActivity(i);
    }

    public static void showPrecessWindows(Context context,String title,String msg,int isBACKPRESS_Enable,Actions lac,Actions rac,String lefttip,String righttip){
        GIFENABLE=false;
        leftaction=lac;
        rightaction=rac;
        settings.processtitle=title;
        settings.processmsg=msg;
        settings.processcache=0;
        isBACKPRESS=isBACKPRESS_Enable;
        Intent i =new Intent(context,processdia.class);
        i.putExtra("LeftButtonTip",lefttip);
        i.putExtra("RightButtonTip",righttip);
        i.putExtra("isLeftButtonEnable",true);
        i.putExtra("isRightButtonEnable",true);
        i.putExtra("isGifEnable",false);
        context.startActivity(i);
    }
    public static void setiS_BUTTON_PRESSED_ENABLE(boolean iS_BUTTON_PRESSED_ENABLE_L,boolean iS_BUTTON_PRESSED_ENABLE_R){
        iS_LEFT_BUTTON_PRESSED_ENABLE=iS_BUTTON_PRESSED_ENABLE_L;
        iS_RIGHT_BUTTON_PRESSED_ENABLE=iS_BUTTON_PRESSED_ENABLE_R;
    }
    private void BUTTON_PRESS_ENABLE_LOOPER(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    //LEFT
                    if (iS_LEFT_BUTTON_PRESSED_ENABLE){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                leftbutton.setTextColor(Color.GRAY);
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                leftbutton.setTextColor(Color.BLACK);
                            }
                        });
                    }
                    //RIGHT
                    if (iS_RIGHT_BUTTON_PRESSED_ENABLE){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rightbutton.setTextColor(Color.GRAY);
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rightbutton.setTextColor(Color.BLACK);
                            }
                        });
                    }

                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
