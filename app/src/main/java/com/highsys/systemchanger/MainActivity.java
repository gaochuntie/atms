package com.highsys.systemchanger;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Handler;
import android.os.Message;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {
    public static Animation translateAnimation;
    public static Animation translateAnimation2;
    private static final String TAG = "MyCallBack";
    //filer
    String[] setmsg;
    //
    MediaPlayer m;
    FloatingActionButton fab;
    AlertDialog.Builder meterr;
    AlertDialog.Builder noroot;
    Button exitwin;
    Button sys1;
    Button sys2;
    Button adventage;
    Button backups;
    setCommand.resultCom r;
    String erromse;
    TextView sdtype;
    ProgressDialog pd;
    TextView restoredir;
    String workmsg;
    String worktitle;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //handler0
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==88){
                File f = new File("/sdcard/highsys/setup.txt");
                if (f.exists()) {
                    try {
                        FileReader fr = new FileReader(f);
                        BufferedReader br = new BufferedReader(fr);
                        //测试配置文件
                        setmsg[0] = br.readLine();
                        if (setmsg[0] == null) {
                            h.sendEmptyMessage(01);
                            fr.close();
                            br.close();
                        }
                        if (setmsg[0] != null) {
                            h.sendEmptyMessage(00);
                            int i=1;
                            while ((setmsg[i] = br.readLine())!=null){
                                i++;

                            }
                            settings.allsettings[0]=setmsg[0];
                            settings.allsettings[1]=setmsg[1];
                            settings.allsettings[2]=setmsg[2];
                            settings.allsettings[3]=setmsg[3];
                            settings.allsettings[4]=setmsg[4];
                            settings.allsettings[5]=setmsg[5];
                            settings.allsettings[6]=setmsg[6];
                            settings.allsettings[7]=setmsg[7];
                            settings.allsettings[8]=setmsg[8];
                            //音乐
                            if (settings.allsettings[1].equals("0")){
                                hmedia.sendEmptyMessage(1);
                            }
                            //shell
                            if(settings.allsettings[5]!=null && settings.allsettings[5].equals("0")!=true){
                                tools.onshell(settings.allsettings[5]);
                            }
                            h.sendEmptyMessage(04);
                            fr.close();
                            br.close();
                        }

                    } catch (Exception e) {
                        erromse=e.getMessage();
                        h.sendEmptyMessage(-2);
                    }

                } else if (!f.exists()){
                    h.sendEmptyMessage(01);
                }
                com.highsys.systemchanger.fingers.fingermode=1;
                Intent i =new Intent(MainActivity.this,com.highsys.systemchanger.fingers.class);
                startActivity(i,  ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
            if (msg.what==03){
                hitpross();
                meterro("No rooted?",0);
            }
            if (msg.what == 00) {
                //pd.dismiss();
               hitpross();
                Toast.makeText(MainActivity.this, "配置文件正常使用中！", Toast.LENGTH_SHORT).show();
                //导入配置
            }
            if (msg.what == 01) {
                hitpross();
                Toast.makeText(MainActivity.this, "配置文件不存在或者为空，即将创建默认！", Toast.LENGTH_LONG).show();
                int a =tools.seten();
                //初始化文件
                initfile();
                //重写配置文件
                if (a == -1) {
                    Toast.makeText(MainActivity.this, "创建失败，错误-1！", Toast.LENGTH_LONG).show();
                   meterro("配置文件写入失败！",0);
                } else if (a != -1) {
                    Toast.makeText(MainActivity.this, "创建成功！重启软件生效！", Toast.LENGTH_LONG).show();
                   // pd.dismiss();
                    Intent i=new Intent(MainActivity.this,hello.class);
                    startActivity(i);
                 finish();
                }
                //创建并重启
            }
            if (msg.what == -2) {
                Toast.makeText(MainActivity.this, "严重错误！！！", Toast.LENGTH_LONG).show();
                meterro(erromse,0);
            }
            if (msg.what==-3){
                meterro("未知错误（手动滑稽|添加管理员QQ2041469901报错",0);
            }
            if (msg.what==04){
                initview();
            }
            if (msg.what==-100){
                meterro(erromse,0);
            }
            if (msg.what==05){
                showpross("海内存知己，天涯若比邻\n请稍等...","正在以用户-"+settings.allsettings[6]+"ATMS正在启动");
            }
            if (msg.what==06){
               // pd.dismiss();
                hitpross();
            }
            if (msg.what==07){
                Toast.makeText(MainActivity.this,"备份完成洛！",Toast.LENGTH_SHORT).show();
            }
            if (msg.what==8) {
                showpross(workmsg, worktitle);
            }
        }
    };
    Handler hmedia=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
            //  medplay();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_main);
        //aik
        //settings.packpath=getApplicationContext().getPackageResourcePath();
        //tools.preaik();
        //Toast.makeText(MainActivity.this,settings.packpath,Toast.LENGTH_LONG).show();
        //
        settings.packpath=getApplicationContext().getPackageResourcePath();
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }
        getWindow().setEnterTransition(new Slide().setDuration(500));
        getWindow().setExitTransition(new Slide().setDuration(1000));
           //权限

        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET
        };
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 0);
            }else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                initapp();
            }
        }


    }

    public void initapp() {
        //packpath
        settings.packpath=getApplicationContext().getPackageResourcePath();
        //
        restoredir=findViewById(R.id.restoredir);
        sdtype=findViewById(R.id.sdtype);
        sys1=findViewById(R.id.btn1);
        sys1.setText("切换到系统1");
        sys2=findViewById(R.id.btn2);
        sys2.setText("切换到系统2");
        backups=findViewById(R.id.btn3);
        backups.setText("备份系统文件");
        adventage=findViewById(R.id.btn4);
        adventage.setText("高级工具箱");
        exitwin=findViewById(R.id.exitwin);
        fab = findViewById(R.id.fab);
         translateAnimation = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.fragment_enter);
        translateAnimation2 = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.fragment_enter);
        //执行动画
        //setup
        sys1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpross("惶恐滩头说惶恐，系统2来系统1\n请稍后...","Changing System!");
                //ufs
               if(Integer.valueOf(settings.allsettings[0])==0){
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           r=setCommand.execCommand(new String[]{"su","dd if="+settings.allsettings[3]+"sda1 bs=512 count=1024 of=/dev/block/sda","dd if="+settings.allsettings[3]+"sde1 bs=512 count=1024 of=/dev/block/sde","dd if="+settings.allsettings[3]+"boot1 of=/dev/block/by-name/boot","dd if="+settings.allsettings[3]+"dtbo1 of=/dev/block/by-name/dtbo","reboot"},true,false);

                       }
                   }).start();
                  // showpross("正在切换到系统1，稍后重启！","切换状态");
                   }
               if (Integer.valueOf(settings.allsettings[0])==1){
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           r=setCommand.execCommand(new String[]{"su","dd if="+settings.allsettings[3]+"sys1 bs=512 count=1024 of=/dev/block/mmcblk0","dd if="+settings.allsettings[3]+"boot1 of=/dev/block/by-name/boot","dd if="+settings.allsettings[3]+"dtbo1 of=/dev/block/by-name/dtbo","reboot"},true,false);

                       }
                   }).start();
                  // showpross("正在切换到系统1，稍后重启！","切换状态");
                   }else if (Integer.valueOf(settings.allsettings[0])!=0&&Integer.valueOf(settings.allsettings[0])!=1){
                  meterro("切换到系统1失败！未知的存储类型！",1);
               }
               //切换系统1
          }
        });
        sys2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpross("惶恐滩头说惶恐，系统1来系统2\n请稍后...","Changing System!");
               if(Integer.valueOf(settings.allsettings[0])==0){
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           r=setCommand.execCommand(new String[]{"su","dd if="+settings.allsettings[3]+"sda2 bs=512 count=1024 of=/dev/block/sda","dd if="+settings.allsettings[3]+"sde2 bs=512 count=1024 of=/dev/block/sde","dd if="+settings.allsettings[3]+"boot2 of=/dev/block/by-name/boot","dd if="+settings.allsettings[3]+"dtbo2 of=/dev/block/by-name/dtbo","reboot"},true,false);

                       }
                   }).start();
                  // showpross("正在切换到系统2，稍后重启！","切换状态");
                   }
               if (Integer.valueOf(settings.allsettings[0])==1){
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           r=setCommand.execCommand(new String[]{"su","dd if="+settings.allsettings[3]+"sys2 bs=512 count=1024 of=/dev/block/mmcblk0","dd if="+settings.allsettings[3]+"boot2 of=/dev/block/by-name/boot","dd if="+settings.allsettings[3]+"dtbo2 of=/dev/block/by-name/dtbo","reboot"},true,false);

                       }
                   }).start();
                //   showpross("正在切换到系统1，稍后重启！","切换状态");
                     }else if (Integer.valueOf(settings.allsettings[0])!=0&&Integer.valueOf(settings.allsettings[0])!=1){
                   meterro("切换到系统2失败！未知的存储类型！",1);
               }
                //切换系统2
            }
        });
        adventage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //高级工具
              Intent i =new Intent(MainActivity.this,advence.class);
              startActivity(i,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                    }
        });
        backups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpross("少壮不搞事，老大徒伤悲！\n请稍后...","Backuping Files");
                //备份
                if (Integer.valueOf(settings.allsettings[0])==0){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            r=setCommand.execCommand(new String[]{"dd if=/dev/block/sda bs=512 count=1024 of="+settings.allsettings[2]+"sda"+settings.allsettings[7],"dd if=/dev/block/sde bs=512 count=1024 of="+settings.allsettings[2]+"sde"+settings.allsettings[7],"dd if=/dev/block/by-name/boot of="+settings.allsettings[2]+"boot"+settings.allsettings[7],"dd if=/dev/block/by-name/dtbo of="+settings.allsettings[2]+"dtbo"+settings.allsettings[7]},true,false);
                            h.sendEmptyMessage(06);
                            h.sendEmptyMessage(07);
                        }
                    }).start();
                      }
                //emmc
                if (Integer.valueOf(settings.allsettings[0])==1){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            r=setCommand.execCommand(new String[]{"dd if=/dev/block/mmcblk0 bs=512 count=1024 of="+settings.allsettings[2]+"sys"+settings.allsettings[7],"dd if=/dev/block/by-name/boot of="+settings.allsettings[2]+"boot"+settings.allsettings[7],"dd if=/dev/block/by-name/dtbo of="+settings.allsettings[2]+"dtbo"+settings.allsettings[7]},true,false);
                            h.sendEmptyMessage(06);
                            h.sendEmptyMessage(07);
                        }
                    }).start();
                    }

            }
        });
        exitwin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this,start.class);
                startActivity(i,  ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });
        pd=new ProgressDialog(MainActivity.this);
        pd.setTitle("处理进度");
        pd.setCancelable(false);
        pd.setMessage("正在处理中");
        pd.setIcon(R.drawable.win10);
        noroot=new AlertDialog.Builder(MainActivity.this);
        noroot.setTitle("ERRO！！！");
        noroot.setIcon(R.drawable.win10);
        noroot.setMessage("对不起您的设备没有ROOT或此软件未获取到root!!!一切功能都将报废！感谢您的支持：获取root方式请百度 机型+如何root：或者到到酷安@高纯铁寻求帮助。");
        noroot.setCancelable(false);
        noroot.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });

        meterr=new AlertDialog.Builder(MainActivity.this);
        meterr.setCancelable(false);
        meterr.setMessage("对不起发生了严重错误！请联系管理员QQ2041469901反馈！！！");
        meterr.setTitle("ERRO！！！");
        meterr.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });

        //setupfile
        setmsg = new String[20];
        showpross("海内存知己，天涯若比邻\n请稍等...","ATMS正在启动");
        //test the setup file

        //检测

        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置存储类型
           try {
               File fff = new File("/sdcard/highsys/setup.txt");
               FileReader fffr = null;
               fffr = new FileReader(fff);
               BufferedReader bbr = new BufferedReader(fffr);
               settings.allsettings[0]=bbr.readLine();
               if (settings.allsettings[0]==null){
                   fffr.close();
                   bbr.close();
                   throw new Exception("None disk type!");
               }else if (settings.allsettings[0]!=null){
                   h.sendEmptyMessage(04);
               }

           } catch (Exception e) {
               e.printStackTrace();
               erromse=e.getMessage();
           }

           /////////////////////////////////////////////////////
                boolean rootpe=isrootper.isroot();
                if (rootpe){
                    //root
                    setCommand.resultCom rrrrr=setCommand.execCommand(new String[]{"[ -e /data/highsyslock ] && echo 0 || echo 1"},true,true);
                    if (rrrrr.ok.equals("0")){
                        Log.d(TAG,"启动验证");
                        h.sendEmptyMessage(88);

                    }
                    if (rrrrr.ok.equals("1")){
                        thebeginning();
                    }

                }else {
                    h.sendEmptyMessage(03);
                }
                //check root
            }
        }).start();


    }
    public void thebeginning(){
        //正常入口处1
        new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File("/sdcard/highsys/setup.txt");
                if (f.exists()) {
                    try {
                        FileReader fr = new FileReader(f);
                        BufferedReader br = new BufferedReader(fr);
                        //测试配置文件
                        setmsg[0] = br.readLine();
                        if (setmsg[0] == null) {
                            h.sendEmptyMessage(01);
                            fr.close();
                            br.close();
                        }
                        if (setmsg[0] != null) {
                            h.sendEmptyMessage(00);
                            int i=1;
                            while ((setmsg[i] = br.readLine())!=null){
                                i++;

                            }
                            settings.allsettings[0]=setmsg[0];
                            settings.allsettings[1]=setmsg[1];
                            settings.allsettings[2]=setmsg[2];
                            settings.allsettings[3]=setmsg[3];
                            settings.allsettings[4]=setmsg[4];
                            settings.allsettings[5]=setmsg[5];
                            settings.allsettings[6]=setmsg[6];
                            settings.allsettings[7]=setmsg[7];
                            settings.allsettings[8]=setmsg[8];
                            //音乐
                            if (settings.allsettings[1].equals("0")){
                                hmedia.sendEmptyMessage(1);
                            }
                            //shell
                            if(settings.allsettings[5]!=null && settings.allsettings[5].equals("0")!=true){
                                tools.onshell(settings.allsettings[5]);
                            }
                            h.sendEmptyMessage(04);
                            fr.close();
                            br.close();
                        }

                    } catch (Exception e) {
                        erromse=e.getMessage();
                        h.sendEmptyMessage(-2);
                    }

                } else if (!f.exists()){
                    h.sendEmptyMessage(01);
                }
            }
        }).start();
    }
    public void initfile() {
        //安装包地址

        //tools.preaik();
        //配置aik

        //
        File f = new File("/sdcard/highsys/setup.txt");
        if (f.exists()) {
            try {
                PrintWriter pw=new PrintWriter(f);
                pw.write("0\n0\n/sdcard/highsys/backups/\n/sdcard/highsys/systems/\n0\nadmin\n1\nnull");
                //测试配置文件
                pw.close();

            } catch (Exception e) {
                meterro("写入默认配置失败啦！",0);

            }
        }

    }
    public void meterro(String err,int backpres){
        settings.processcache=1;
        allerros.backpressed=backpres;
        allerros.erromsg=err;
        Intent ii =new Intent(MainActivity.this,allerros.class);
        startActivity(ii);

    }
    public void initview(){
        restoredir.setText("从"+settings.allsettings[3]+"恢复文件");
        if(Integer.valueOf(settings.allsettings[0])==0){
            sdtype.setText("UFS");
        }
        if(Integer.valueOf(settings.allsettings[0])==1){
            sdtype.setText("EMMC");
        }else if (Integer.valueOf(settings.allsettings[0])!=0 &&Integer.valueOf(settings.allsettings[0])!=1){
           meterro("错误！从配置文件读取存储类型错误！",0);
        }
    }
    public void showpross(String pross,String title){
        settings.processcache=0;
        pd.setTitle(title);
        pd.setMessage(pross);
        settings.processmsg=pross;
        settings.processtitle=title;
       // pd.show();
        Intent i =new Intent(MainActivity.this,processdia.class);
        startActivity(i);
    }
    public void hitpross(){
        settings.processcache=1;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){//
            case 0://如果申请权限回调的参数
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"申请成功",Toast.LENGTH_SHORT).show();
                    initapp();
                } else {
                    AlertDialog.Builder nosdcard = new AlertDialog.Builder(MainActivity.this);
                    nosdcard.setCancelable(false);
                    nosdcard.setTitle("错误了小朋友");
                    nosdcard.setIcon(R.drawable.win10);
                    nosdcard.setMessage("不给存储怎么玩啦，重新给一下存储再打开啦！管理员QQ2041469901");
                    nosdcard.setPositiveButton("我玩不起", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    Toast.makeText(this,"拒绝权限",Toast.LENGTH_SHORT).show();
                    nosdcard.show();
                }
                break;

        }

    }
    public void medplay(){
           try {
               if (settings.allsettings[8]!=null){
                   m=new MediaPlayer();
                   m.setDataSource(settings.allsettings[8]);
                   m.prepare();
                   m.start();
               }else if (settings.allsettings[8]==null){
                   m=MediaPlayer.create(MainActivity.this,R.raw.back);
                   m.start();
               }



                }catch (Exception e){
                    erromse=e.getMessage();
                    h.sendEmptyMessage(-2);
                }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sys1.startAnimation(translateAnimation);
        sys2.startAnimation(translateAnimation);
        adventage.startAnimation(translateAnimation);
        backups.startAnimation(translateAnimation);
        if (fingers.fingerresults==0){
            thebeginning();
        }
        if (fingers.fingerresults==-1){

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        sys1.startAnimation(translateAnimation);
        sys2.startAnimation(translateAnimation);
        adventage.startAnimation(translateAnimation);
        backups.startAnimation(translateAnimation);
        if (fingers.fingerresults==0){
            thebeginning();
        }
        if (fingers.fingerresults==-1){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sys1.startAnimation(translateAnimation);
        sys2.startAnimation(translateAnimation);
        adventage.startAnimation(translateAnimation);
        backups.startAnimation(translateAnimation);
        if (fingers.fingerresults==0){
            thebeginning();
        }
        if (fingers.fingerresults==-1){

        }
    }
}
