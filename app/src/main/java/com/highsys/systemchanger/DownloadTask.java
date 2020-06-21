package com.highsys.systemchanger;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask <String,Integer,Integer>{
    public static final  int TYPE_SUCCESS =0;
    public static final int TYPE_FAULED =1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED=3;


    @Override
    protected Integer doInBackground(String... strings) {
        InputStream is=null;
        RandomAccessFile savedfile=null;
        File file=null;
        try {
            long downloadlengh=0;
            String downloadUrl=strings[0];
            String filename=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory="/sdcard/highsys/temp";
            file=new File(directory+filename);
            if (file.exists()){
                downloadlengh=file.length();
            }
            long contentlength=getContentLength(downloadUrl);
            if (contentlength==0){
                return  TYPE_FAULED;
            }else if (contentlength==downloadlengh){
                return TYPE_SUCCESS;
            }
            OkHttpClient client=new OkHttpClient();
             Request request =new Request.Builder()
                    .addHeader("RANGE","bytes="+downloadlengh+"-")
                    .url(downloadUrl)
                    .build();
            Response response=client.newCall(request).execute();
            if (response!=null){
                is=response.body().byteStream();
                savedfile=new RandomAccessFile(file,"rw");
                savedfile.seek(downloadlengh);;
                byte[] b =new byte[1024];
                int total=0;
                int len;
                while ((len=is.read(b))!=-1){
                    if (isCancelled()){
                        return TYPE_CANCELED;
                    }else if (isPaused){
                        return TYPE_PAUSED;
                    }else {
                        total+=len;
                        savedfile.write(b,0,len);
                        //
                        int progress=(int)((total+downloadlengh)*100/contentlength);
                        publishProgress(progress);
                    }

                }
                response.body().close();
                return  TYPE_SUCCESS;
            }
        } catch (Exception e) {
            onProgressUpdate(-100);
            Log.d("Erros_DownloadTask",e.getMessage());
        }finally {
            try{
                if (is!=null){
                    is.close();
                }
                if (savedfile!=null){
                    savedfile.close();
                }
                if (isCancelled() && file!=null){
                    file.delete();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return TYPE_FAULED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
     int progress=values[0];
     if (progress > lastProgress &&progress>=0){
         listener.onProgress(progress);
         lastProgress=progress;
         stepthree.process.setText(progress+"%");
         if (progress==100){
             stepthree.process.setText("下载完成请继续");
         }
     }
     if (progress==-100){
         stepthree.process.setText("下载失败");
         Snackbar.make(stepthree.snackview,"Download Failed",Snackbar.LENGTH_SHORT).show();
     }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case TYPE_SUCCESS :
                listener.onSuccess();
                break;
            case TYPE_FAULED :
                listener.onFaild();
                break;
            case TYPE_PAUSED :
                listener.onPaused();
                break;
            case TYPE_CANCELED :
                listener.onCanceled();
                break;
            default:
                break;

        }
    }
    public void pauseDownload(){
        isPaused=true;
    }
    public void cancleDownload(){
        idCanceled=true;
    }
    private long getContentLength(String downloadurl) throws IOException{
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(downloadurl)
                .build();
        Response response=client.newCall(request).execute();
        if (response!=null && response.isSuccessful()){
            long contenlength=response.body().contentLength();
            return contenlength;
        }
        return 0;
    }

    private DownloadListener listener;
    private boolean idCanceled = false;
    private boolean isPaused=false;
    private int lastProgress;
    public DownloadTask(DownloadListener listener){
        this.listener=listener;
    }
}
