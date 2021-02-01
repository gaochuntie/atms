package com.highsys.systemchanger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.highsys.pages.install_mobile;

import java.io.File;

public class DownloadService extends Service {
    public DownloadTask downloadTask;
    public String downloadurl;
    public static DownloadListener listener;
    public DownloadBinder mbinder=new DownloadBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }
    public class DownloadBinder extends Binder{
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void startDownload(String url){
            if (downloadTask == null){
                downloadurl=url;
                downloadTask=new DownloadTask(listener);
                downloadTask.execute(downloadurl);
                Log.d("XX","Download rec :"+downloadurl);
                startForeground(1,getNotification("Working..."));

                //Snackbar.make(stepthree.snackview,"Downloading...",Snackbar.LENGTH_SHORT).show();
            }else {
         downloadTask=null;
                downloadurl=url;
                downloadTask=new DownloadTask(listener);
                downloadTask.execute(downloadurl);
                Log.d("XX","Download rec :"+downloadurl);
                startForeground(1,getNotification("Working..."));

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
    public static void setDownloadListener(DownloadListener l){
        listener=l;
    }
    public NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification getNotification(String title) {
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
        Notification notification = new Notification.Builder(this).setChannelId(CHANNEL_ONE_ID)
                .setSmallIcon(R.drawable.iconnull)
                .setContentTitle(title)
                .setOnlyAlertOnce(true)
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
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
