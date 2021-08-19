package com.highsys.fragments;
import com.highsys.adapters.recAdapter;
import com.highsys.atms_obj.rec_obj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.highsys.pages.allerros;
import com.highsys.pages.processdia;
import com.highsys.systemchanger.DownloadService;
import com.highsys.systemchanger.R;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.highsys.systemchanger.MainActivity.*;


public class rec_frag extends Fragment {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://121.5.103.76:3306/atms";
    private static final String USER = "pro_downloader";
    private static  final String axx="mPsbpLGefy";
    private static  final String title="5ZNNfp";

    static final String PASS = axx+title;
    Spinner rec_deviec_name;
    int dia_fag=0;
    String[] name_list=new String[]{"All","刷新"};
    String[] size_list=new String[]{"All","512g","256g","128g","64g","32g","16g","8g"};
    ArrayAdapter<String> name_adapter;
    ArrayAdapter<String> size_adapter;
    public static rec_obj selected_rec;
    Spinner rec_deviec_size;
    SwipeRefreshLayout swipeRefreshLayout;
    private recAdapter adapter;
    public String TAG="rec_frag";
    String _name="";
    String _size="";
    RecyclerView recyclerView;
    private List<rec_obj> reclist=new ArrayList<>();
    Handler h=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    recyclerView.setAdapter(adapter);
                    break;
                case 8:
                    break;
                default:
                    break;
            }
        }
    };
    public static DownloadService.DownloadBinder downloadBinder;


    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder=(DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recovery_list,container,false);
        reclist.clear();
         recyclerView=(RecyclerView) view.findViewById(R.id.rec_recy);
         rec_deviec_name=(Spinner)view.findViewById(R.id.rec_device_name);
        rec_deviec_size=(Spinner)view.findViewById(R.id.rec_device_size);

        name_adapter=new ArrayAdapter<String>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,name_list);
        rec_deviec_name.setAdapter(name_adapter);

        size_adapter=new ArrayAdapter<String>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,size_list);
        rec_deviec_size.setAdapter(size_adapter);

        rec_deviec_name.setSelection(0,true);
        rec_deviec_size.setSelection(0,true);
        rec_deviec_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dia_fag++;
                Log.d(TAG,"Select :"+name_list[i]);
                Log.d(TAG,"Select :"+l);
                if (name_list[i].equals("All")){
                    _name="";
                }else {
                    _name=name_list[i];
                }
                reloadrecBySizeAndName();
                if (dia_fag>=3){
                    //忽略自动选择的刷新

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        rec_deviec_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                swipeRefreshLayout.setRefreshing(true);
                Log.d(TAG,"Select :"+size_list[i]);
                if (size_list[i].equals("All")){
                    _size="";
                }else {
                    _size=size_list[i];
                }
                if (dia_fag==1){
                    reloadrecBySizeAndName();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.rec_fresh);
         swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
         swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 reloadrecBySizeAndName();
             }
         });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new recAdapter(reclist);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        load_rec_device_namelist();
        //reloadrecBySizeAndName();
        Intent intent=new Intent(getContext(),DownloadService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent,connection,BIND_AUTO_CREATE);
        return view;
    }
      public void  reloadrecBySizeAndName(){
        if (dia_fag!=1){
            processdia.showPrecessWindows(recyclerView.getContext(),"加載 Loading","正在加載recovery列表，請稍候...",processdia.BACKPRESS_DISABLE,true);
        }
         new Thread(new Runnable() {
            @Override
            public void run() {

                //really get
                try {
                    Thread.sleep(1000);
                    reclist.clear();
                    //

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
                        sql = "SELECT * FROM atms.rec_src";
                        ResultSet rs = stmt.executeQuery(sql);

                        // 展开结果集数据库
                        while(rs.next()){
                            // 通过字段检索
                            String [] temp=new String[7];
                            temp[1]=rs.getString("title");
                            temp[2]=rs.getString("version");
                            temp[3]=rs.getString("details");
                            temp[4]=rs.getString("device_name");
                            temp[5]=rs.getString("remote");
                            temp[6]=rs.getString("size");
                            rec_obj tem=new rec_obj(temp[1],temp[2],temp[3],temp[5],temp[4],temp[6]);
                            if (tem.getSize().equals(_size) || _size.equals("")){
                                if (tem.getName().equals(_name) || _name.equals("")){
                                    Log.d(TAG,"Device000001 : "+_name+"|"+_size);
                                    reclist.add(tem);
                                    Log.d(TAG,tem.getRectitle()+":"+tem.getRec_remote_local());
                                }
                                Log.d(TAG,"For while test 00002");
                            }
                            Log.d(TAG,"For while test 00001");
                        }
                        // 完成后关闭
                        rs.close();
                        stmt.close();
                        conn.close();
                    }catch(SQLException se){
                        allerros.showErroMsg(getContext(),se.getMessage(),allerros.BACKPRESSDISABLE);
                        // 处理 JDBC 错误
                        se.printStackTrace();
                    }catch(Exception e){
                        allerros.showErroMsg(getContext(),e.getMessage(),allerros.BACKPRESSDISABLE);
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
                    //
                    //////////////////
                }catch (Exception e){
                    allerros.showErroMsg(getContext(),e.getMessage(),allerros.BACKPRESSDISABLE);
                    e.printStackTrace();
                }
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        if (swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

                processdia.setProcess(processdia.PROCESS_FINISH);
            }
        }).start();
    }
    public void load_rec_device_namelist(){
        processdia.showPrecessWindows(recyclerView.getContext(),"加載 Loading","正在加載recovery列表，請稍候...",processdia.BACKPRESS_DISABLE,true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //devicenamelist
                    StringBuffer f= new StringBuffer("All");
                    //
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
                        sql = "SELECT DISTINCT device_name FROM atms.rec_src";
                        ResultSet rs = stmt.executeQuery(sql);

                        // 展开结果集数据库
                        while(rs.next()){
                            // 通过字段检索
                            f=new StringBuffer(f.toString()+"\n"+rs.getString("device_name"));
                        }
                        // 完成后关闭
                        rs.close();
                        stmt.close();
                        conn.close();
                    }catch(SQLException se){
                        allerros.showErroMsg(getContext(),se.getMessage(),allerros.BACKPRESSDISABLE);
                        f=new StringBuffer("All\nconnect faild");
                        // 处理 JDBC 错误
                        se.printStackTrace();
                    }catch(Exception e){
                        allerros.showErroMsg(getContext(),e.getMessage(),allerros.BACKPRESSDISABLE);
                        f=new StringBuffer("All\nconnect faild");
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
                    //
                    Log.d(TAG,f.toString());
                    name_list=f.toString().split("\n");
                    Log.d(TAG,"Found name : "+name_list[2]);
                    //load_rec_device_namelist();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            h.sendEmptyMessage(8);
                            rec_deviec_name.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,name_list));
                        }
                    });
                }catch (Exception e){
                    processdia.setProcess(processdia.PROCESS_FINISH);
                    allerros.showErroMsg(getContext(),e.getMessage(),allerros.BACKPRESSDISABLE);
                    e.printStackTrace();
                }
            }
        }).start();

    }
    //Debug
    public void testsys(){

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        downloadBinder=null;
        selected_rec=null;
    }
}
