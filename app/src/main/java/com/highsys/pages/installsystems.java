package com.highsys.pages;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.highsys.systemchanger.MyApplication;
import com.highsys.systemchanger.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class installsystems extends AppCompatActivity {
    TextView results;
    TextView anous;
    TextView amtspro_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_installsystems);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();

        }
        amtspro_msg=findViewById(R.id.pay_msg);
        anous=findViewById(R.id.anous);

        ObjectAnimator.ofFloat(anous,"translationX",-200,0).setDuration(500).start();
        ObjectAnimator.ofFloat(amtspro_msg,"translationX",-200,0).setDuration(500).start();
        getpay();
    }
    public void getpay(){
        final String msg="购买须知:\n网络链接失败!";
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder()
                        .url("http://121.5.103.76/atms_msg/atmspro_pay.txt")
                        .build();
                try {
                    Response response=client.newCall(request).execute();
                    final String msg=response.body().string();
                    Log.d("INSTALLER",msg);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            amtspro_msg.setText("购买须知:\n"+msg);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            amtspro_msg.setText(msg);
                        }
                    });
                }


            }
        }).start();
    }
}
