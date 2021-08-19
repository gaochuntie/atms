package com.highsys.pages;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.highsys.systemchanger.DownloadService;
import com.highsys.systemchanger.MainActivity;
import com.highsys.systemchanger.MyApplication;
import com.highsys.systemchanger.R;
import com.highsys.tool.setCommand;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class install_mobile extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://121.5.103.76:3306/atms";
    private static final String USER = "pro_downloader";
    private static  final String axx="mPsbpLGefy";
    private static  final String title="5ZNNfp";

    static final String PASS = axx+title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_mobile);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        textView=findViewById(R.id.test1);
        gettestm();
    }
    public  void gettestm() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                Statement stmt = null;
                try{
                    // 注册 JDBC 驱动
                    Class.forName(JDBC_DRIVER);

                    // 打开链接
                    Log.d("MySql","连接数据库...");
                    conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);

                    // 执行查询
                    Log.d("MySql"," 实例化Statement对象...");
                    stmt = (Statement) conn.createStatement();
                    String sql;
                    sql = "SELECT * FROM rec_src";
                    ResultSet rs = stmt.executeQuery(sql);

                    // 展开结果集数据库
                    while(rs.next()){
                        // 通过字段检索
                        int id  = rs.getInt("id");
                        String title = rs.getString("title");
                        String version = rs.getString("version");
                        final sqlmsg sqlmsg=new sqlmsg();
                        sqlmsg.setMsg("ID: " + id+"\n"+"Title: " + title+"\n"+"Version: " + version);
                        // 输出数据
                        Log.d("MySql","ID: " + id);
                        Log.d("MySql","Title: " + title);
                        Log.d("MySql","Version: " + version);
                        Log.d("MySql","\n");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("ID: " + sqlmsg.getMsg());
                            }
                        });
                    }
                    // 完成后关闭
                    rs.close();
                    stmt.close();
                    conn.close();
                }catch(SQLException se){
                    // 处理 JDBC 错误
                    se.printStackTrace();
                }catch(Exception e){
                    // 处理 Class.forName 错误
                    e.printStackTrace();
                }finally{
                    // 关闭资源
                    try{
                        if(stmt!=null) stmt.close();
                    }catch(SQLException se2){
                    }// 什么都不做
                    try{
                        if(conn!=null) conn.close();
                    }catch(SQLException se){
                        se.printStackTrace();
                    }
                }
                Log.d("MySql","Goodbye!");
            }
        }).start();

    }

    @Override
    public void onClick(View view) {

    }
    class sqlmsg{
        String msg="null";

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}