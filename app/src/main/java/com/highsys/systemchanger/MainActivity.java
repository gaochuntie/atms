package com.highsys.systemchanger;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.highsys.atms_obj.settings;
import com.highsys.fragments.FirmwareKit;
import com.highsys.fragments.SysSelecter;
import com.highsys.fragments.advence_activity;
import com.highsys.fragments.bacpup_frag;
import com.highsys.fragments.rec_frag;
import com.highsys.fragments.shellBox_frag;
import com.highsys.pages.allerros;
import com.highsys.pages.fingers;
import com.highsys.pages.hello;
import com.highsys.pages.install_mobile;
import com.highsys.pages.installsystems;
import com.highsys.pages.processdia;
import com.highsys.pages.setvalues;
import com.highsys.pages.supportme;
import com.highsys.pages.thehelps;
import com.highsys.tool.Actions;
import com.highsys.tool.isrootper;
import com.highsys.tool.setCommand;
import com.highsys.tool.tools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Message;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import atms_pro.activities.ui.AtmsProHome;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import atms_pro.activities.*;

public class MainActivity extends AppCompatActivity {
    private TextView dailysaying;
    public static Context context;
    int root_per=0;
    public static String SETUPFILE="0";
    public static Animation translateAnimation;
    public static Animation translateAnimation2;
    private static final String TAG = "MainActivity";
    //filer
    String[] setmsg=new String[100];
    //
    MediaPlayer m;
    AlertDialog.Builder meterr;
    AlertDialog.Builder noroot;
    setCommand.resultCom r;
    String erromse;
    ProgressDialog pd;
    String workmsg;
    AlertDialog.Builder remote;
    String worktitle;
    Button show_nav;
    public  static NavigationView nav_view;
    DrawerLayout draw_view;
    int backup_flag=0;
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
               //
                if (setmsg[0] != null) {
                    h.sendEmptyMessage(00);
                    settings.allsettings[0]=setmsg[0];
                    Log.d(TAG,"setmsg");
                    settings.allsettings[1]=setmsg[1];
                    Log.d(TAG,"setmsg1");
                    settings.allsettings[2]=setmsg[2];
                    settings.allsettings[3]=setmsg[3];
                    settings.allsettings[4]=setmsg[4];
                    settings.allsettings[5]=setmsg[5];
                    settings.allsettings[6]=setmsg[6];
                    settings.allsettings[7]=setmsg[7];
                    Log.d(TAG,setmsg[7]);
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
                }
                fingers.fingermode=1;
                Intent i =new Intent(MainActivity.this, fingers.class);
                startActivity(i,  ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
            if (msg.what==03){
                hitpross();
                meterro("Permission denied. You don't have access to any system file or device file.I will do nothing.获取root权限失败,无法获取配置文件,且此软件所有功能无效.",allerros.BACKPRESSENABLE);

            }
            if (msg.what == 00) {
                //pd.dismiss();
                loadsystemSelecter();
               hitpross();
               Snackbar.make(draw_view,"配置文件正常!",Snackbar.LENGTH_SHORT).show();
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
                    Intent i=new Intent(MainActivity.this, hello.class);
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

      //  this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
      //  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      //          WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
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
        context=this;
    }

    public void initapp() {
        //packpath
        settings.packpath=getApplicationContext().getPackageResourcePath();
        //

        remote=new AlertDialog.Builder(this);
        dailysaying=findViewById(R.id.dailysaying);
        draw_view=findViewById(R.id.draw_view);
        nav_view=findViewById(R.id.nav_view);
        show_nav=findViewById(R.id.show_nav);
   // restoredir=findViewById(R.id.restoredir);
   // sdtype=findViewById(R.id.sdtype);
   // backups=findViewById(R.id.btn3);
   // backups.setText("备份系统文件");
   // adventage=findViewById(R.id.btn4);
   // adventage.setText("高级工具箱");
   // fab = findViewById(R.id.fab);

         translateAnimation = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.fragment_enter);
        translateAnimation2 = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.fragment_enter);
        //执行动画
        //setup
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.firmware_backuper :
                        replaceFragment(new FirmwareKit());
                        break;
                    case R.id.settings :
                       // Snackbar.make(draw_view,"Still not updated",Snackbar.LENGTH_LONG).show();
                       replaceFragment(new setvalues());
                        break;
                    case R.id.choose :
                        //
                        replaceFragment(new SysSelecter());
                        break;
                    case R.id.atms_pro :
                        startActivity(new Intent(MainActivity.this, AtmsProHome.class));
                        break;
                    case R.id.help :
                        //
                        Intent help=new Intent(MainActivity.this, thehelps.class);
                        startActivity(help);
                        break;
                    case R.id.supportme :
                        Intent ii =new Intent(MainActivity.this, supportme.class);
                        startActivity(ii);
                        break;
                    case R.id.backups :
                        //bacpup_page
                        replaceFragment(new bacpup_frag());
                        break;
                    case R.id.advence :
                        replaceFragment(new advence_activity());
                        break;
                    case R.id.about :
                        replaceFragment(new rec_frag());
                        //
                        break;
                    case R.id.shell:
                        //Snackbar.make(draw_view,"敬请等待",Snackbar.LENGTH_LONG).show();
                        replaceFragment(new shellBox_frag());
                        break;
                    case R.id.remote_server:


                        remote.setIcon(R.drawable.iconnull);
                        remote.setTitle("远程服务");
                        remote.setMessage("您即将联系ATMS认证人工服务人员，请注意数据备份，并且同意设备风险自行承担这一项！　可能会收取一定的费用．");
                        remote.setCancelable(true);
                        remote.setPositiveButton("访问", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqapi://card/show_pslcard?src_type=internal&source=sharecard&version=1&uin=987589769")));//跳转到QQ资
                                }catch (Exception e){
                                    Snackbar.make(nav_view,"无法打开QQ,请安装最新版QQ.\n"+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            }
                        });
                        remote.show();
                         break;
                    default:
                        break;
                }
                //nav_view.setItemIconTintList(null);
                draw_view.closeDrawers();
                return true;
            }
        });
        show_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw_view.openDrawer(GravityCompat.START);
            }
        });
        pd=new ProgressDialog(MainActivity.this);
        pd.setTitle("处理进度");
        pd.setCancelable(false);
        pd.setMessage("正在处理中");
        pd.setIcon(R.drawable.iconnull);
        noroot=new AlertDialog.Builder(MainActivity.this);
        noroot.setTitle("ERRO！！！");
        noroot.setIcon(R.drawable.iconnull);
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
        showpross("海内存知己，天涯若比邻\n请稍等...","ATMS正在启动");

        testsetupfile();

    }

    public void getdailysaying(){

        if (root_per == 1) {
            String msg="Welcome to ATMS";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("http://59.110.167.17/atms_msg/daily_saying.txt")
                            .build();
                    try {
                        Response response=client.newCall(request).execute();
                        final String msg=response.body().string();
                        Log.d(TAG,msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dailysaying.setText(msg);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }).start();
        }else if (root_per==0){
            dailysaying.setText("获取root权限失败! Permission denied !");
            dailysaying.setTextColor(Color.RED);
        }

    }
    public void testsetupfile(){
        //test the setup file

        //检测

        new Thread(new Runnable() {
            @Override
            public void run() {
                ////////
                boolean rootpe= isrootper.isroot();
                if (rootpe){

                    root_per=1;
                    getdailysaying();
                    //root
                    setCommand.resultCom rrrrr=setCommand.execCommand(new String[]{"[ -e /data/highsyslock ] && echo 0 || echo 1"},true,true);
                    if (rrrrr.ok.equals("0")){
                        Log.d(TAG,"启动验证zhiwen");

                        h.sendEmptyMessage(88);

                    }
                    if (rrrrr.ok.equals("1")){
                        thebeginning();
                        Log.d(TAG,"ROOT OK");
                    }

                }else {
                    h.sendEmptyMessage(03);
                    root_per=0;
                    getdailysaying();
                }
                //check root

                //设置存储类型

            }
        }).start();

    }
    public void thebeginning(){

        //正常入口处1
        new Thread(new Runnable() {
            @Override
            public void run() {
                tools.seten();
                setCommand.execCommand(new String[]{"mkdir /mnt/persist_atms","mount -t ext4 /dev/block/bootdevice/by-name/persist /mnt/persist_atms"},true,false);
                setCommand.resultCom r = setCommand.execCommand( new String[]{"[ -e /mnt/persist_atms/atms_core.conf ] && echo 1 || echo 0"},true,true);
                //Log.d(TAG,setCommand.execCommand( new String[]{"[ -e /mnt/persist_atms/atms_core.conf ] && echo 1 || echo 0"+"fedwf"},true,true).ok);
                try {
                    //扫描persist
                    Log.d("XX","a");

                    if (r.ok.equals("1")){
                        Log.d("XX","b");
                        String SETUPFILEPOSSI=setCommand.execCommand(new String[]{"cat /mnt/persist_atms/atms_core.conf"},true,true).ok.split("\n")[0];
                        setmsg=setCommand.execCommand( new String[]{"cat "+SETUPFILEPOSSI},true,true).ok.split("#");
                        Log.d(TAG,"test 0"+setmsg[0]);
                        //配置向上转型接口
                        setCommand.resultCom resultCom=setCommand.execCommand(new  String[]{"cat "+SETUPFILEPOSSI},true,true);
                        Log.d("NULLSETUP",resultCom.ok);
                        if (resultCom.ok.equals("")){
                            Log.d("XX","c");
                            Log.d("NULLSETUP","F");
                            h.sendEmptyMessage(01);
                        }else {
                            //真正的加载配置
                            if (setmsg[0] != null & setmsg[1]!=null) {
                                h.sendEmptyMessage(00);
                                settings.allsettings[0]=setmsg[0];
                                Log.d(TAG,"setmsg");
                                settings.allsettings[1]=setmsg[1];
                                Log.d(TAG,"setmsg1");
                                settings.allsettings[2]=setmsg[2];
                                settings.allsettings[3]=setmsg[3];
                                settings.allsettings[4]=setmsg[4];
                                settings.allsettings[5]=setmsg[5];
                                settings.allsettings[6]=setmsg[6];
                                settings.allsettings[7]=checkSysOrder();
                                Log.d(TAG,setmsg[7]);
                                settings.allsettings[8]=setmsg[8];
                                Log.d("XX","配置文件地址"+setmsg[8]);
                                //寻找系统毛垫
                                //音乐
                                if (settings.allsettings[1].equals("0")){
                                    hmedia.sendEmptyMessage(1);
                                }
                                //mke2fs -t ext4 /dev/block/bootdevice/by-name/persistbak
                                //shell
                                if(settings.allsettings[5]!=null && settings.allsettings[5].equals("0")!=true){
                                    tools.onshell(settings.allsettings[5]);
                                }
                                h.sendEmptyMessage(04);
                            }else {
                                h.sendEmptyMessage(01);
                                Log.d("XX","d");
                            }
                        }
                        //
                    }else if (r.ok.equals("0")){
                        //hello
                        Log.d(TAG,"hello");
                        //setup.txt
                        //initfile();
                        h.sendEmptyMessage(01);
                        Log.d(TAG,"hello_w");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    erromse=e.getMessage();
                }



            }
        }).start();
    }
    public String checkSysOrder(){
        String order="unknown";
        setCommand.resultCom r=setCommand.execCommand(new String[]{"[ -e /system/atms_system_order.conf ] && echo 0 || echo 1"},true,true);
        if (r.ok.equals("0")){
            Log.d("XX","order"+r.ok);
            order=setCommand.execCommand(new String[]{"cat /system/atms_system_order.conf"},true,true).ok;
            settings.allsettings[7]=order;
        }else if (r.ok.equals("1")){
            setSysOrder();
        }
        return order;
    }


    AlertDialog dialog;

    public void setSysOrder(){
        runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                final EditText inputServer = new EditText(MainActivity.this);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("请输入你在系统几，例如系统１填１，系统２填２然后点击ＯＫ将会自动退出软件，你需要重新启动软件。若此界面一直出现，请新建文件atms_system_order.conf内容填写你在系统几，然后将其复制到/system/目录下即可进入软件,实在不行点击取消，这将会导致少部分功能不正常，如自动备份等");
                builder.setTitle("SysOrder").setIcon(R.drawable.iconnull).setView(inputServer);
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        settings.allsettings[7]=String.valueOf(inputServer.getText());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("XX","echo "+settings.getSYSORDER()+" >/system/atms_system_order.conf");
                                setCommand.execCommand(new String[]{"mount -o remount,rw /","echo "+settings.getSYSORDER()+" >/system/atms_system_order.conf"},true,false);
                                runOnUiThread(new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.exit(0);
                                    }
                                }));
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        settings.allsettings[7]="unknown";
                        dialog.dismiss();
                    }
                });
              dialog = builder.show();

            }
        }));
    }
    //load systems
    public void loadsystemSelecter(){
        replaceFragment(new SysSelecter());
    }
    public void replaceFragment(Fragment frag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ts = fm.beginTransaction();
        ts.replace(R.id.allview_con, frag);
        ts.commit();

    }
    public void initfile() {
        //安装包地址

        //tools.preaik();
        //配置aik

        //
        setCommand.execCommand( new String[]{"echo \"/sdcard/highsys/setup.txt\" > /mnt/persist_atms/atms_core.conf"},true,true);
        File f = new File("/sdcard/highsys/setup.txt");
        if (f.exists()) {
            try {
                PrintWriter pw=new PrintWriter(f);
                pw.write("0#\n0#\n/sdcard/highsys/backups/#\n/sdcard/highsys/systems/#\n0#\nadmin#\nSystem order moved to /system/atms_system_order.conf#\n/sdcard/highsys/setup.txt#\nunknow");
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
        if(Integer.valueOf(settings.allsettings[0])==0){
           // sdtype.setText("UFS");
        }
        if(Integer.valueOf(settings.allsettings[0])==1){
        //    sdtype.setText("EMMC");
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
                    nosdcard.setIcon(R.drawable.iconnull);
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
        draw_view.closeDrawers();

        if (fingers.fingerresults==0){
            thebeginning();
        }
        if (fingers.fingerresults==-1){

        }
        if (setvalues.setvalues_i!=null){
            setvalues.setvalues_i.checkfingers();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //draw_view.closeDrawers();

        if (fingers.fingerresults==0){
            thebeginning();
        }
        if (fingers.fingerresults==-1){

        }
        if (setvalues.setvalues_i!=null){
            setvalues.setvalues_i.checkfingers();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //draw_view.closeDrawers();

        if (fingers.fingerresults==0){
            thebeginning();
        }
        if (fingers.fingerresults==-1){

        }
        if (setvalues.setvalues_i!=null){
            setvalues.setvalues_i.checkfingers();
        }
    }
    public  void changesys(){
        processdia.setIsBACKPRESS(processdia.BACKPRESS_DISABLE);

        Intent i =new Intent(MainActivity.this,processdia.class);
    }

    @Override
    public void onBackPressed() {
        if (backup_flag==0){
            Snackbar.make(draw_view, "再按一次退出", Snackbar.LENGTH_SHORT).show();
        }
        if (backup_flag==1){
            System.exit(0);
        }
        backup_flag++;
    }
}
