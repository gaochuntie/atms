package com.highsys.systemchanger;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;

public class power extends AppCompatActivity {
    setCommand.resultCom r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_power);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }

        Button cancle=findViewById(R.id.cancle);
        Button reboot=findViewById(R.id.reboot);
        final Button twrp=findViewById(R.id.twrp);
        Button shutdown=findViewById(R.id.shutdown);
        Button helps=findViewById(R.id.helps);
        cancle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
               // finish();
            }
        });
        reboot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //3333333333333播放音乐
               r=setCommand.execCommand(new String[]{"reboot"},true,false);
            }
        });
        twrp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                r=setCommand.execCommand(new String[]{"reboot recovery"},true,false);
            }
        });
        shutdown.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                r=setCommand.execCommand(new String[]{"reboot fastboot"},true,false);
            }
        });
        helps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Intent i =new Intent(power.this,helper.class);
               // startActivity(i);
                finish();
            }
        });
    }
}
