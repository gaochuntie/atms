package com.highsys.systemchanger;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class tools {
    public static String errosmkbin;
    public static String compmsg=null;
    public static void onshell(String filepath){
        final String s=filepath;
        new Thread(new Runnable() {
            @Override
            public void run() {
                setCommand.resultCom rrr=null;
                String[] cmd=new String[200];
                try{
                    File f=new File(s);
                    FileReader fr=new FileReader(f);
                    BufferedReader br=new BufferedReader(fr);
                    int index=0;
                    while ((cmd[index]=br.readLine())!=null){
                        index++;
                    }
                    rrr=setCommand.execCommand(cmd,true,true);

                }catch (Exception e){
                    compmsg= e.getMessage();


                }
            }
        }).start();

    }
    public static void preaik(Context context) throws Exception {
       // UnzipFromAssets.copyAssetsFile2Phone((Activity) context,"aik.zip");
        UnzipFromAssets.unZip(MyApplication.getContext(),"aik.zip",settings.getTempdir(),true);
        UnzipFromAssets.copyAssetsFile2Phone((Activity) context,"bootctl");
        UnzipFromAssets.copyAssetsFile2Phone((Activity) context,"parted");
        UnzipFromAssets.copyAssetsFile2Phone((Activity) context,"prepare-highsys.zip");
        UnzipFromAssets.copyAssetsFile2Phone((Activity) context,"sgdisk");
        UnzipFromAssets.copyAssetsFile2Phone((Activity) context,"vbmeta.img");
        //Unzip(settings.getTempdir()+"aik.zip",settings.getTempdir());
        setCommand.execCommand(new String[]{"cp -r "+settings.getTempdir()+"* /data/highsys/"},true,false);
          // setCommand.resultCom rr=setCommand.execCommand(new String[]{"unzip "+settings.packpath+" -d /sdcard/highsys/temp"},true,false);
               // rr=setCommand.execCommand(new String[]{"unzip /sdcard/highsys/temp/assets/aik.zip -d /data/highsys/\n","mv /sdcard/highsys/temp/assets/parted /data/highsys/","unzip /sdcard/highsys/temp/assets/prepare-highsys.zip -d /data/highsys/zips/\n","mv /sdcard/highsys/temp/assets/bootctl /data/highsys/\n","mv /sdcard/highsys/temp/assets/vbmeta.img /data/highsys/\n"},true,false);
        setCommand.execCommand(new String[]{"chmod -R 777 /data/highsys/*\n"},true,false);
        setCommand.execCommand(new String[]{"cp /data/highsys/aik /system/bin/\n"},true,false);
                //clean
               // rr=setCommand.execCommand(new String[]{"rm -rf /sdcard/highsys/temp/*"},true,false);
        setCommand.execCommand(new String[]{"rm -rf /sdcard/highsys/temp/*"},true,false);
    }
    public static void mkbinUFS(String[] allmsg){
        try{
            PrintWriter pw =new PrintWriter("/sdcard/highsys/temp/update-binary");
            pw.write("#!/sbin/sh\n");
            pw.write("ZIP=$3\n");
            pw.write("cd /\n");
            pw.write("mkdir highsys\n");
            pw.write("cp /data/highsys/parted /highsys/\n");
            pw.write("chmod 777 /highsys/*\n");
            pw.write("cd /highsys/\n");
            pw.write("./parted /dev/block/sda rm "+allmsg[2]+" yes I\n");
            pw.write("./parted /dev/block/sda mkpart ext4 "+allmsg[4]+" "+(Integer.valueOf(allmsg[3])+Integer.valueOf(allmsg[4]))+"\n");
            pw.write("./parted /dev/block/sda mkpart ext4 "+(Integer.valueOf(allmsg[3])+Integer.valueOf(allmsg[4]))+" "+allmsg[5]+"G\n");
            pw.write("./parted /dev/block/sda name "+allmsg[2]+" highsys\n");
            pw.write("./parted /dev/block/sda name "+String.valueOf(Integer.valueOf(allmsg[2])+1)+" userdata\n");
            //
            //
            pw.flush();
            pw.close();
            setCommand.resultCom r=setCommand.execCommand(new String[]{"cp /sdcard/highsys/temp/update-binary /data/highsys/zips/META-INF/com/google/android/update-binary","cp -r /data/highsys/zips/META-INF /sdcard/"},true,false);
            comprass.compress("/sdcard/META-INF","/sdcard/prepare-highsys1.zip");
            tools.mkprea(allmsg);
        }catch (Exception e){
            errosmkbin=e.getMessage();
        }
    }
    public static void mkbinEMMC(String[] allmsg){
        try{
            PrintWriter pw =new PrintWriter("/sdcard/highsys/temp/update-binary");
            pw.write("#!/sbin/sh\n");
            pw.write("ZIP=$3\n");
            pw.write("cd /\n");
            pw.write("mkdir highsys\n");
            pw.write("cp /data/highsys/parted /highsys/\n");
            pw.write("chmod 777 /highsys/*\n");
            pw.write("cd /highsys/\n");
            pw.write("./parted /dev/block/mmcblk0 rm "+allmsg[2]+" yes I\n");
            pw.write("./parted /dev/block/mmcblk0 mkpart ext4 "+allmsg[4]+" "+(Integer.valueOf(allmsg[3])+Integer.valueOf(allmsg[4]))+"\n");
            pw.write("./parted /dev/block/mmcblk0 mkpart ext4 "+(Integer.valueOf(allmsg[3])+Integer.valueOf(allmsg[4]))+" "+allmsg[5]+"G\n");
            pw.write("./parted /dev/block/mmcblk0 name "+allmsg[2]+" highsys\n");
            pw.write("./parted /dev/block/mmcblk0 name "+String.valueOf(Integer.valueOf(allmsg[2])+1)+" userdata\n");
            //
            //
            pw.flush();
            pw.close();
            setCommand.resultCom r=setCommand.execCommand(new String[]{"cp /sdcard/highsys/temp/update-binary /data/highsys/zips/META-INF/com/google/android/update-binary","cp -r /data/highsys/zips/META-INF /sdcard/"},true,false);
            comprass.compress("/sdcard/META-INF","/sdcard/prepare-highsys1.zip");
            tools.mkpreb(allmsg);
        }catch (Exception e){
            errosmkbin=e.getMessage();
        }

    }
    public static void mkprea(String[] allmsg){
        try {
            PrintWriter pw =new PrintWriter("/sdcard/highsys/temp/update-binary");
            pw.write("#!/sbin/sh\n");
            pw.write("ZIP=$3");
            pw.write("umount /data");
            pw.write("mke2fs -t ext4 /dev/block/sda"+allmsg[2]+"\n");
            pw.write("mke2fs -t ext4 /dev/block/sda"+String.valueOf(Integer.valueOf(allmsg[2])+1)+"\n");
            pw.flush();
            pw.close();
            setCommand.resultCom r=setCommand.execCommand(new String[]{"cp /sdcard/highsys/temp/update-binary /data/highsys/zips/META-INF/com/google/android/update-binary","cp -r /data/highsys/zips/META-INF /sdcard/"},true,false);
            comprass.compress("/sdcard/META-INF","/sdcard/prepare-highsys2.zip");
        }catch (Exception e){
            errosmkbin=e.getMessage();
        }
    }
    public static void mkpreb(String[] allmsg){
        try {
            PrintWriter pw =new PrintWriter("/sdcard/highsys/temp/update-binary");
            pw.write("#!/sbin/sh\n");
            pw.write("ZIP=$3");
            pw.write("umount /data");
            pw.write("mke2fs -t ext4 /dev/block/sda"+allmsg[2]+"\n");
            pw.write("mke2fs -t ext4 /dev/block/sda"+String.valueOf(Integer.valueOf(allmsg[2])+1)+"\n");
            pw.flush();
            pw.close();
            setCommand.resultCom r=setCommand.execCommand(new String[]{"cp /sdcard/highsys/temp/update-binary /data/highsys/zips/META-INF/com/google/android/update-binary","cp -r /data/highsys/zips/META-INF /sdcard/"},true,false);
            comprass.compress("/sdcard/META-INF","/sdcard/prepare-highsys2.zip");
        }catch (Exception e){
            errosmkbin=e.getMessage();
        }
    }
    private static void Unzip(String zipFile, String targetDir) {
        int BUFFER = 4096; //这里缓冲区我们使用4KB，
        String strEntry; //保存每个zip的条目名称

        try {
            BufferedOutputStream dest = null; //缓冲输出流
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry; //每个zip条目的实例

            while ((entry = zis.getNextEntry()) != null) {

                try {
                    Log.i("Unzip: ","="+ entry);
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();

                    File entryFile = new File(targetDir + strEntry);

                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }

                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();
        } catch (Exception cwj) {
            cwj.printStackTrace();
        }
    }
    public static int seten()  {
        setCommand.resultCom r;
        r=setCommand.execCommand(new String[]{"cd /sdcard","mkdir highsys","cd highsys","mkdir backups","mkdir imgwork","mkdir music","mkdir systems","mkdir temp","touch setup.txt","cd /data","mkdir highsys","cd /data/highsys/","mkdir zips","cd /sdcard/highsys/imgwork","mkdir unpack","mkdir repack"},true,true);
        return r.result1;
    }
}
