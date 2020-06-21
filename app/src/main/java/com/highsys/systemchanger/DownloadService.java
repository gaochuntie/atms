package com.highsys.systemchanger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class DownloadService extends Service {
    private DownloadTask downloadTask;
    private String downloadurl;
    private DownloadListener listener=new DownloadListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("Downloading...",progress));
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccess() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Success",100));
            Toast.makeText(DownloadService.this,"Download Success",Toast.LENGTH_SHORT).show();
            stepthree.process.setText("下载成功");
            install_mobile.next.setVisibility(View.VISIBLE);
            stepthree.process_pic.setImageResource(R.drawable.dual);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onFaild() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Failed",-1));
            Toast.makeText(DownloadService.this,"Download Failed",Toast.LENGTH_SHORT).show();
            stepthree.process.setText("下载失败");

        }

        @Override
        public void onPaused() {
            downloadTask=null;
            Toast.makeText(DownloadService.this,"Paused",Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onCanceled() {
            downloadTask=null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,"Canceled",Toast.LENGTH_SHORT).show();

        }
    };

    private DownloadBinder mbinder=new DownloadBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }
    class DownloadBinder extends Binder{
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void startDownload(String url){
            if (downloadTask == null){
                downloadurl=url;
                downloadTask=new DownloadTask(listener);
                downloadTask.execute(downloadurl);
                startForeground(1,getNotification("Downloadimg",0));
                //Snackbar.make(stepthree.snackview,"Downloading...",Snackbar.LENGTH_SHORT).show();
            }
        }
        public void onPauseDownload(){
            if (downloadTask!=null){
                downloadTask.pauseDownload();
            }
        }
        public void cancelDownload(){
            if (downloadTask!=null){
                downloadTask.cancleDownload();
            }
            if (downloadurl!=null){
                String filename=downloadurl.substring(downloadurl.lastIndexOf("/"));
                String directory="/sdcard/highsys/temp";
                File file=new File(directory+filename);
                if (file.exists()){
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                //Snackbar.make(stepthree.snackview,"Canceled",Snackbar.LENGTH_SHORT).show();
            }
        }

    }
    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification getNotification(String title, int progress) {
        String CHANNEL_ONE_ID = "CHANNEL_ONE_ID";
        String CHANNEL_ONE_NAME = "CHANNEL_ONE_ID";
        NotificationChannel notificationChannel = null;
//进行8.0的判断
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(false);
            notificationChannel.setSound(null,null);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(this, install_mobile.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this).setChannelId(CHANNEL_ONE_ID)
                .setSmallIcon(R.drawable.iconnull)
                .setContentTitle(title)
                .setContentText(progress + "%")
                .setProgress(100, progress, false)
                .setOnlyAlertOnce(true)
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        if (progress<0){
             notification = new Notification.Builder(this).setChannelId(CHANNEL_ONE_ID)
                    .setSmallIcon(R.drawable.iconnull)
                    .setContentTitle(title)
                    .setContentText("下载失败")
                    .setOnlyAlertOnce(true)
                    .build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
        }else if (progress==100){
             notification = new Notification.Builder(this).setChannelId(CHANNEL_ONE_ID)
                    .setSmallIcon(R.drawable.iconnull)
                    .setContentTitle(title)
                    .setContentText("下载完成")
                    .setOnlyAlertOnce(true)
                    .build();
            notification.flags |= Notification.FLAG_NO_CLEAR;

        }
        return notification;
    }
 //     Intent intent=new Intent(this,install_mobile.class);
 //     PendingIntent pi=PendingIntent.getActivities(this,0,new Intent[]{intent},0);
 //     NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
 //     builder.setSmallIcon(R.drawable.iconnull);
 //     builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.iconnull));
 //     builder.setContentIntent(pi);
 //     builder.setContentTitle(title);
 //     if (progress>=0){
 //         builder.setContentText(progress+"%");
 //         builder.setProgress(100,progress,false);

 //     }
 //     return builder.build();
 // }
}
