package com.highsys.systemchanger;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.DoubleBuffer;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class install_mobile extends AppCompatActivity implements View.OnClickListener {
    public static FrameLayout steps;
    public static int STEPONE = 1;
    public static int STEPTWO = 2;
    public static int STEPTHREE = 3;
    public static int NEXTCODE = 1;
    public static View action_box;
    public static Button next;
    public static AppCompatActivity ac;
    public static Button stop;
    public static int DEVICEID = -1;
    public static Devices selecteddevice;
    public static final int REQUEST_CODE = 1;
    private DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
             downloadBinder=(DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_install_mobile);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        ac=this;
        stop = findViewById(R.id.stop);
        stop.setOnClickListener(this);
        next = findViewById(R.id.next);
        next.setOnClickListener(this);
        action_box = findViewById(R.id.action_box);
        action_box.startAnimation(MainActivity.translateAnimation);
        steps = findViewById(R.id.steps);
        replaceFragment(new setpone());
        requestPermission(this);
        //申请权限
        if (ContextCompat.checkSelfPermission(install_mobile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(install_mobile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.FOREGROUND_SERVICE},
                    REQUEST_CODE);

        }
        Intent intent=new Intent(this,DownloadService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                setCommand.execCommand(new String[]{"dd if=/dev/block/bootdevice/by-name/recovery of=/sdcard/highsys/backups/backup_rec.img"},true,false);

            }
        }).start();
    }

    public void replaceFragment(Fragment frag) {
        if (NEXTCODE == 2) {

        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ts = fm.beginTransaction();
        ts.replace(R.id.steps, frag);
        ts.commit();

    }

    //设备API大于6.0时，主动申请权限
    private void requestPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (NEXTCODE == 1) {
                    replaceFragment(new steptwo());
                    Log.d("install_mobile", "STEPONE");
                }
                if (NEXTCODE == 2) {
                    if (DEVICEID == -1) {
                        Snackbar.make(v, "请选择合适的设备", Snackbar.LENGTH_SHORT).show();
                    } else {
                        downloadBinder.startDownload(selecteddevice.getAddress());
                        Log.d("install_mobile",selecteddevice.getAddress());
                        replaceFragment(new stepthree());
                        //startDownload
                    }

                    Log.d("install_mobile", "STEPTWO");
                }
                if (NEXTCODE==3){
                    replaceFragment(new stepfour());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setCommand.execCommand(new String[]{"dd if=/sdcard/highsys/temp/"+selecteddevice.getName()+" of=/dev/block/bootdevice/by-name/recovery"},true,false);
                        }
                    }).start();
                }
                if (NEXTCODE==4){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setCommand.execCommand(new String[]{"reboot recovery"},true,false);
                        }
                    }).start();
                }
                break;
            case R.id.stop:
                if (NEXTCODE==2){
                    downloadBinder.onPauseDownload();
                }
                Toast.makeText(MyApplication.getContext(), "recovery已恢复", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setCommand.execCommand(new String[]{"dd if=/sdcard/highsys/backups/backup_rec.img of=/dev/block/bootdevice/by-name/recovery"},true,false);

                    }
                }).start();
                finish();
                break;
            default:
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(action_box, "没有权限", Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
    public  void refreshdevice(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                steptwo.ada.notifyDataSetChanged();
            }
        });
    }

}