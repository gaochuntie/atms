package com.highsys.systemchanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class setvalues extends AppCompatActivity {
    AlertDialog.Builder meterr;
    String[] sets=new String[8];
    EditText btn1;
    EditText btn2;
    EditText btn3;
    EditText btn4;
    EditText btn5;
    Button fingers;
    public int isnull=0;
    EditText btn6;
    Handler h;
    String erro=null;
    int ifgo;
    com.kyleduo.switchbutton.SwitchButton switchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_setvalues);

        //handler
        h=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what==-1){
                    meterro(erro);
                }
                if(msg.what==01){
                    Toast.makeText(setvalues.this,"操作已执行",Toast.LENGTH_SHORT).show();
                   // Toast.makeText(setvalues.this,settings.allsettings[0]+settings.allsettings[1]+settings.allsettings[2]+settings.allsettings[3]+settings.allsettings[4],Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(setvalues.this,MainActivity.class);
                    startActivity(i, ActivityOptions.makeSceneTransitionAnimation(setvalues.this).toBundle());

                }
                if (msg.what==-100){
                    erro="对不起，严禁出现null值，如果您不知道如何填写，请填写I don't know，如果软件因为不正确的配置崩溃，请删除/sdcard/highsys文件夹重新打开软件";
                    meterro(erro);
                }
            }
        };
        //
        getWindow().setEnterTransition(new Slide().setDuration(500));
        getWindow().setExitTransition(new Slide().setDuration(1000));
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }
        //
         btn1=findViewById(R.id.btn1);
         btn2=findViewById(R.id.btn2);
         btn3=findViewById(R.id.btn3);
         btn4=findViewById(R.id.btn4);
         btn5=findViewById(R.id.btn5);
         btn6=findViewById(R.id.btn6);
        switchButton= (com.kyleduo.switchbutton.SwitchButton)
                findViewById(R.id.fingers);

        switchButton.setChecked(true);//设置为真，即默认为真
        switchButton.isChecked();//被选中
        switchButton.toggle();     //开关状态
        switchButton.setEnabled(true);//false为禁用按钮
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                com.highsys.systemchanger.fingers.fingermode=0;
                Intent i =new Intent(setvalues.this,com.highsys.systemchanger.fingers.class);
                startActivity(new Intent(setvalues.this,fingers.class));
                overridePendingTransition(R.anim.botttom_in,R.anim.bottom_silent);
               // Toast.makeText(setvalues.this,"jfioadsjf",Toast.LENGTH_SHORT).show();
            }
        });
         Button exitwin=findViewById(R.id.exitwin);
         exitwin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Runtime runtime = Runtime.getRuntime();
                 try {
                     runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         });
        Button ok=findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    settings.allsettings[0]= String.valueOf(btn1.getText());
                    settings.allsettings[1]= String.valueOf(btn2.getText());
                    settings.allsettings[2]= String.valueOf(btn3.getText());
                    settings.allsettings[3]= String.valueOf(btn4.getText());
                    settings.allsettings[4]= String.valueOf(btn5.getText());
                    settings.allsettings[5]= String.valueOf(btn6.getText());
                   // sets[6]=settings.username;
                   // sets[7]=settings.userid;
                    //开始写入配置文件

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                 isnull=0;
                                for (int i=0;i<6;i++){
                                    if (settings.allsettings[i]==null){
                                        isnull=-1;
                                        erro="请全部填写！";
                                        meterr.setCancelable(true);
                                        h.sendEmptyMessage(-1);
                                        break;
                                    }
                                }
                                File f=new File("/sdcard/highsys/setup.txt");
                                if (f.exists() & isnull!=-1){
                                    PrintWriter pw =new PrintWriter("/sdcard/highsys/setup.txt");
                                    int i =0;
                                   while ( i < 20 & settings.allsettings[i]!=null){
                                        pw.write(settings.allsettings[i]+"\n");
                                        i++;
                                    }
                                    pw.flush();
                                    pw.close();
                                    h.sendEmptyMessage(01);
                                }else if (!f.exists()){
                                    erro="文件不存在！";
                                    h.sendEmptyMessage(-1);
                                }else if (isnull==-1){
                                    erro="不要因为看不懂就空着，你是小学生蛮？";
                                    meterr.setCancelable(true);
                                    h.sendEmptyMessage(-1);

                                }
                            }catch (Exception e){
                                erro=e.getMessage();
                                h.sendEmptyMessage(-1);
                            }
                        }
                    }).start();

                }catch (Exception e){

                }


               // Toast.makeText(setvalues.this,btn1.getText(),Toast.LENGTH_LONG).show();
            }
        });
        meterr=new AlertDialog.Builder(setvalues.this);
        meterr.setCancelable(false);
        meterr.setMessage("对不起发生了严重错误！请联系管理员QQ2041469901反馈！！！");
        meterr.setTitle("ERRO！！！");
        meterr.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });

        checkfingers();
    }
    public void meterro(String err){
        meterr.setMessage("\n"+err);
        meterr.show();
    }
    public void checkfingers(){
        setCommand.resultCom r=setCommand.execCommand(new String[]{"[ -e /data/highsyslock ] && echo 0 || echo 1"},true,true);
        if (r.ok.equals("0")){
            switchButton.setCheckedNoEvent(true);
        }
        if (r.ok.equals("1")){
           switchButton.setCheckedNoEvent(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkfingers();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkfingers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkfingers();
    }
}
