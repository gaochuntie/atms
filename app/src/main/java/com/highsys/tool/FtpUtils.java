package com.highsys.tool;

import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/*
 * Author:Maxwell
 * Date:2020-02-04
 */
public class FtpUtils {

    private static final String TAG = FtpUtils.class.getSimpleName();
    private FTPClient ftpClient;

    public FTPClient getFTPClient(String ftpHost, int ftpPort,
                                  String ftpUserName, String ftpPassword) {
        if (ftpClient == null) {
            ftpClient = new FTPClient();
        }
        if (ftpClient.isConnected()) {
            return ftpClient;
        }
        Log.d(TAG, "ftpHost:" + ftpHost + ",ftpPort:" + ftpPort);

        try {
            // connect to the ftp server
            // set timeout
            ftpClient.setConnectTimeout(50000);
            // 设置中文编码集，防止中文乱码
            ftpClient.setControlEncoding("UTF-8");

            ftpClient.connect(ftpHost, ftpPort);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                Log.d(TAG, "connect fail: replyCode:" + replyCode);
                return ftpClient;
            }
            Log.d(TAG, "connect success: replyCode" + replyCode);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return null;
        }

        Log.d(TAG, "ftpUserName:" + ftpUserName + ",ftpPassword:" + ftpPassword);
        // login on the ftp server
        try {

            ftpClient.login(ftpUserName, ftpPassword);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                Log.d(TAG, "login fail: replyCode:" + replyCode);
                return ftpClient;
            }

            Log.d(TAG, "login success: replyCode:" + replyCode);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return null;
        }

        return ftpClient;
    }


    /**
     * 关闭FTP方法
     *
     * @param ftp
     * @return
     */
    public boolean closeFTP(FTPClient ftp) {

        try {
            ftp.logout();
        } catch (Exception e) {
            Log.e(TAG, "FTP关闭失败");
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    Log.e(TAG, "FTP关闭失败");
                }
            }
        }

        return false;

    }


    /**
     * 下载FTP下指定文件
     *
     * @param ftp      FTPClient对象
     * @param filePath FTP文件路径
     * @param fileName 文件名
     * @param downPath 下载保存的目录
     * @return true:success, false:fail
     */
    public boolean downLoadFTP(FTPClient ftp, String filePath, String fileName,
                               String downPath) {
        // 默认失败
        boolean flag = false;
        FTPFile[] files;
        // 跳转到文件目录
        try {
            ftp.changeWorkingDirectory(filePath);
            // 获取目录下文件集合
            ftp.enterLocalPassiveMode();
            files = ftp.listFiles();
        } catch (IOException e) {
            Log.e(TAG, "downLoadFTP: " + e);
            e.printStackTrace();
            return false;
        }

        for (FTPFile file : files) {
            // 取得指定文件并下载
            if (file.getName().equals(fileName)) {
                Log.d(TAG, "fileName:" + fileName);
                File downFile = new File(downPath + File.separator
                        + file.getName());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(downFile);
                    // 绑定输出流下载文件,需要设置编码集，不然可能出现文件为空的情况
                    flag = ftp.retrieveFile(new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1), fos);
                    // 下载成功删除文件,看项目需求
                    // ftpClient.deleteFile(new String(fileName.getBytes("UTF-8"),"ISO-8859-1"));
                    fos.flush();
                    if (flag) {
                        Log.d(TAG, "Params downloaded successful.");
                    } else {
                        Log.e(TAG, "Params downloaded failed.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return flag;
    }

    /**
     * FTP文件上传工具类
     *
     * @param ftp      ftpClient
     * @param filePath filePath
     * @param ftpPath  ftpPath
     * @return true:success, false:fail
     */
    public boolean uploadFile(FTPClient ftp, String filePath, String ftpPath) {
        boolean flag = false;
        InputStream in = null;
        try {
            // 设置PassiveMode传输
            ftp.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if (!ftp.changeWorkingDirectory(ftpPath)) {
                ftp.makeDirectory(ftpPath);
            }
            //跳转目标目录
            ftp.changeWorkingDirectory(ftpPath);

            //上传文件
            File file = new File(filePath);
            in = new FileInputStream(file);
            String tempName = ftpPath + File.separator + file.getName();
            flag = ftp.storeFile(new String(tempName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1), in);
            if (flag) {
                Log.d(TAG, "上传成功");
            } else {
                Log.e(TAG, "上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "上传失败");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
}
