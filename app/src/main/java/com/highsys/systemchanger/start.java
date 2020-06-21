package com.highsys.systemchanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.StringReader;

public class start extends AppCompatActivity {
    Button sys3;
    Button sys4;
    Button help;
    Button aboutm;
    Button set;
    Button supportme;
    Button slota;
    Button update;
    Button slotb;
    setCommand.resultCom r;
    Handler h =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==01){
                showpross("少年不知愁滋味——天天搞机\n请稍后...","Changing System3");
            }
            if (msg.what==02){
                showpross("锄禾日当午，编程皆辛苦！\n请稍后...","Changing System4");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_start);
        getWindow().setEnterTransition(new Slide().setDuration(500));
        getWindow().setExitTransition(new Slide().setDuration(1000));
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }

        FloatingActionButton fab1=findViewById(R.id.fab1);
        set=findViewById(R.id.settings);
        help=findViewById(R.id.help);
        aboutm=findViewById(R.id.aboutme);
        sys3=findViewById(R.id.sys3);
        sys4=findViewById(R.id.sys4);
        supportme=findViewById(R.id.supportme);
        slota=findViewById(R.id.slota);
        slotb=findViewById(R.id.slotb);
        update=findViewById(R.id.update);




        /////////////////////////////////////////////////
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(start.this,"请通过官网或者酷安更新！",Toast.LENGTH_LONG).show();
            }
        });
        slota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r=setCommand.execCommand(new String[]{"bootctl set-active-boot-slot 0","reboot"},true,false);
                // r=setCommand.execCommand(new String[]{"cd /data/highsys/","./bootctl set-active-boot-slot 0","reboot"},true,false);
            }
        });
        slotb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r=setCommand.execCommand(new String[]{"bootctl set-active-boot-slot 1","reboot"},true,false);
               // r=setCommand.execCommand(new String[]{"cd /data/highsys/","./bootctl set-active-boot-slot 1","reboot"},true,false);
            }
        });
        supportme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(start.this, com.highsys.systemchanger.supportme.class);
                startActivity(i);
            }
        });
        aboutm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(start.this,aboutme.class);
                startActivity(i);
            }
        });
        sys3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.sendEmptyMessage(01);
                if(Integer.valueOf(settings.allsettings[0])==0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            r=setCommand.execCommand(new String[]{"su","dd if="+settings.allsettings[3]+"sda3 bs=512 count=1024 of=/dev/block/sda","dd if="+settings.allsettings[3]+"sde3 bs=512 count=1024 of=/dev/block/sde","dd if="+settings.allsettings[3]+"boot3 of=/dev/block/by-name/boot","dd if="+settings.allsettings[3]+"dtbo3 of=/dev/block/by-name/dtbo","reboot"},true,false);

                        }
                    }).start();
                    // showpross("峰峦如聚，钱包如怒\n请稍后...","切换状态");
                }
                if (Integer.valueOf(settings.allsettings[0])==1){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            r=setCommand.execCommand(new String[]{"su","dd if="+settings.allsettings[3]+"sys3 bs=512 count=1024 of=/dev/block/mmcblk0","dd if="+settings.allsettings[3]+"boot3 of=/dev/block/by-name/boot","dd if="+settings.allsettings[3]+"dtbo3 of=/dev/block/by-name/dtbo","reboot"},true,false);

                        }
                    }).start();
                     //  showpross("峰峦如聚，钱包如怒\n请稍后...","切换状态");
                }else if (Integer.valueOf(settings.allsettings[0])!=0&&Integer.valueOf(settings.allsettings[0])!=1){
                    hitpross();
                    Toast.makeText(start.this,"未知的芯片格式！配置错误",Toast.LENGTH_LONG).show();
                }
            }
        });
        sys4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.sendEmptyMessage(02);
                if(Integer.valueOf(settings.allsettings[0])==0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            r=setCommand.execCommand(new String[]{"su","dd if="+settings.allsettings[3]+"sda4 bs=512 count=1024 of=/dev/block/sda","dd if="+settings.allsettings[3]+"sde4 bs=512 count=1024 of=/dev/block/sde","dd if="+settings.allsettings[3]+"boot4 of=/dev/block/by-name/boot","dd if="+settings.allsettings[3]+"dtbo4 of=/dev/block/by-name/dtbo","reboot"},true,false);

                        }
                    }).start();
                     showpross("峰峦如聚，钱包如怒\n请稍后...","切换状态");
                }
                if (Integer.valueOf(settings.allsettings[0])==1){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            r=setCommand.execCommand(new String[]{"su","dd if="+settings.allsettings[3]+"sys4 bs=512 count=1024 of=/dev/block/mmcblk0","dd if="+settings.allsettings[3]+"boot4 of=/dev/block/by-name/boot","dd if="+settings.allsettings[3]+"dtbo4 of=/dev/block/by-name/dtbo","reboot"},true,false);

                        }
                    }).start();
                       showpross("峰峦如聚，钱包如怒\n请稍后...","切换状态");
                }else if (Integer.valueOf(settings.allsettings[0])!=0&&Integer.valueOf(settings.allsettings[0])!=1){
                    hitpross();
                    Toast.makeText(start.this,"未知的芯片格式！配置错误",Toast.LENGTH_LONG).show();
                }
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a =new Intent(start.this,thehelps.class);
                startActivity(a);
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(start.this,setvalues.class);
                startActivity(i,ActivityOptions.makeSceneTransitionAnimation(start.this).toBundle());
                //Toast.makeText(start.this,"请手动更改/sdcard/highsys/setup.txt、文件，测试版无任何提示帮助！",Toast.LENGTH_LONG).show();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
               // finish();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
            }
        });
        final Button power=findViewById(R.id.power);
        power.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i=new Intent(start.this,power.class);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(start.this).toBundle());
                finish();
            }
        });
    }
    public void showpross(String pross,String title){
        settings.processcache=0;
        settings.processmsg=pross;
        settings.processtitle=title;
        // pd.show();
        Intent i =new Intent(start.this,processdia.class);
        startActivity(i);
    }
    public void hitpross(){
        settings.processcache=1;
    }
}
