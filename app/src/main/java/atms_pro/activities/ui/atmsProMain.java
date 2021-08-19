package atms_pro.activities.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StatFs;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import atms_pro.activities.ui.adapters.*;
import atms_pro.activities.ui.objs.pro_obj;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.highsys.pages.allerros;
import com.highsys.pages.processdia;
import com.highsys.systemchanger.DownloadService;
import com.highsys.systemchanger.R;
import com.highsys.tool.FtpUtils;
import com.highsys.tool.setCommand;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import static com.highsys.systemchanger.MainActivity.context;

public class atmsProMain extends AppCompatActivity {
    public static int viplevel;
    public static String username;
    public static String userid;
//copy
private EndlessScrollview esvNews;
    int name_counter=0;
    int size_counter=0;
    private static String PASSWORD="UNKNOW";
    private static String USERNAME="NAMELESS";
    static String TAG ="ATMSPROMAIN :";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://121.5.103.76:3306/atms_pro";
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
    public static pro_obj selected_rec;
    Spinner rec_deviec_size;
    SwipeRefreshLayout swipeRefreshLayout;
    private proAdapter adapter;
    String _name="";
    String _size="";
    RecyclerView recyclerView;
    ScrollTextview noticeBoard;
    ScrollTextview sdcardsize;
    ScrollTextview isdoubled;
    TextView name;
    TextView id;
    TextView level;
    TextView mydevice;

    public static atmsProMain atmsProMain_instance=null;


     private List<pro_obj> reclist=new ArrayList<>();
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
    public void getdailysaying(){

            String msg="Welcome to ATMS";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("http://121.5.103.76/atms_msg/pro_notice.txt")
                            .build();
                    try {
                        Response response=client.newCall(request).execute();
                        final String msg=response.body().string();
                        Log.d(TAG,msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                noticeBoard.setText(msg);
                                noticeBoard.setMovementMethod(ScrollingMovementMethod.getInstance()); //滚动文本
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }).start();
    }
    //
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atms_pro_main);


        atmsProMain_instance=(atmsProMain)this;
        Intent intent1=getIntent();
        username=intent1.getStringExtra("username");
        userid=intent1.getStringExtra("userid");
        viplevel=intent1.getIntExtra("viplevel",0);
        Toolbar toolbar = findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //copy
        reclist.clear();
        recyclerView=(RecyclerView) findViewById(R.id.rec_recy);
        rec_deviec_name=(Spinner)findViewById(R.id.rec_device_name);
        rec_deviec_size=(Spinner)findViewById(R.id.rec_device_size);

        name_adapter=new ArrayAdapter<String>(atmsProMain.this,R.layout.support_simple_spinner_dropdown_item,name_list);
        rec_deviec_name.setAdapter(name_adapter);

        size_adapter=new ArrayAdapter<String>(atmsProMain.this,R.layout.support_simple_spinner_dropdown_item,size_list);
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
                if (name_counter>=1){
                    Log.d("COUNTER NAME :", String.valueOf(name_counter));
                    swipeRefreshLayout.setRefreshing(true);
                    reloadrecBySizeAndName();
                }

                if (dia_fag>=3){
                    //忽略自动选择的刷新
                }
                name_counter++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        rec_deviec_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d(TAG,"Select :"+size_list[i]);
                if (size_list[i].equals("All")){
                    _size="";
                }else {
                    _size=size_list[i];
                }
                if (size_counter>=0){
                    Log.d("COUNTER SIZE :", String.valueOf(size_counter));
                    swipeRefreshLayout.setRefreshing(true);
                    reloadrecBySizeAndName();
                }
                size_counter++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.rec_fresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadrecBySizeAndName();
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(atmsProMain.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new proAdapter(reclist);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        load_rec_device_namelist();
        //reloadrecBySizeAndName();
        Intent intent=new Intent(atmsProMain.this,DownloadService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);

        ImageView imageView = (ImageView) findViewById(R.id.imagetaichi);
        //动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_runcircle);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        imageView.startAnimation(animation);
        esvNews = (EndlessScrollview) findViewById(R.id.scrollable_view);
        noticeBoard=findViewById(R.id.pro_notice_board);
        sdcardsize=findViewById(R.id.sdcardsize);
        isdoubled=findViewById(R.id.isdoublesys);
        getdailysaying();
       queryStorage();
       check_doublesystem();
        noticeBoard.setMovementMethod(ScrollingMovementMethod.getInstance()); //滚动文本

        name=findViewById(R.id.username);
        name.setText(username);
        level=findViewById(R.id.viplevel);
        switch (viplevel){
            case 0 :
                level.setText("不正常帐号");
                break;
            case 1 :
                level.setText("普通帐号");
                break;
            case 2 :
                level.setText("纪念版会员");
                break;
            case 3 :
                level.setText("普通会员");
                break;
            case 4 :
                level.setText("高级会员");
                break;
            case 5 :
                level.setText("新时代会员");

                break;
            case 6 :
                level.setText("死机党会员");
                break;
            case 8 :
                level.setText("死机党会员");
                break;
            case 9 :
                level.setText("永久会员");
                break;
            case 10 :
                level.setText("测试工号");
                break;
            case 11 :
                level.setText("ATMS开发者");
                break;
            default:break;
        }
        mydevice=findViewById(R.id.mydevice);
        mydevice.setText(Build.DEVICE);

        noticeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(atmsProMain.this);
                alert.setIcon(R.drawable.taichi);
                alert.setTitle("通知");
                alert.setMessage(noticeBoard.getText().toString());
                alert.setCancelable(true);
                alert.create().show();
            }
        });

    }

    public void check_doublesystem(){
        setCommand.resultCom r=setCommand.execCommand(new String[]{"ls /dev/block/bootdevice/by-name/userdata*"},true,true);
        if (r.ok.equals("/dev/block/bootdevice/by-name/userdata")){
            isdoubled.setText("未安装双系统");
        }else{
            isdoubled.setText("已经安装双系统");
        }
    }
    public void queryStorage(){
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());

        //存储块总数量
        long blockCount = statFs.getBlockCount();
        //块大小
        long blockSize = statFs.getBlockSize();
        //可用块数量
        long availableCount = statFs.getAvailableBlocks();
        //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
        long freeBlocks = statFs.getFreeBlocks();
        //这两个方法是直接输出总内存和可用空间，也有getFreeBytes
        //API level 18（JELLY_BEAN_MR2）引入
        long totalSize = statFs.getTotalBytes();
        long availableSize = statFs.getAvailableBytes();

        sdcardsize.setText("本机: "+ getUnit(totalSize));
        Log.d("statfs","availableSize = " + getUnit(availableSize));

        //这里可以看出 available 是小于 free ,free 包括保留块。
        Log.d("statfs","total = " + getUnit(blockSize * blockCount));
        Log.d("statfs","available = " + getUnit(blockSize * availableCount));
        Log.d("statfs","free = " + getUnit(blockSize * freeBlocks));
    }

    private String[] units = {"B", "KB", "MB", "GB", "TB"};

    /**
     * 单位转换
     */
    private String getUnit(float size) {
        int index = 0;
        while (size > 1024 && index < 4) {
            size = size / 1024;
            index++;
        }
        return String.format(Locale.getDefault(), " %.2f %s", size, units[index]);
    }
    public  void  reloadrecBySizeAndName(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (atmsProMain.class){


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
                        sql = "SELECT * FROM atms_pro.pro_list";
                        ResultSet rs = stmt.executeQuery(sql);

                        // 展开结果集数据库
                        while(rs.next()){
                            // 通过字段检索
                            String [] temp=new String[10];
                            temp[1]=rs.getString("title");
                            temp[2]=rs.getString("version");
                            temp[3]=rs.getString("details");
                            temp[4]=rs.getString("device_name");
                            temp[5]=rs.getString("remote");
                            temp[6]=rs.getString("size");
                            temp[7]=rs.getString("require_level");
                            temp[8]=rs.getString("sdcard_cutway");
                            String [] required_items= rs.getString("required_settings").split(":");
                            pro_obj tem=new pro_obj(temp[1],temp[2],temp[3],temp[5],temp[4],temp[6],temp[7],temp[8],required_items);
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
                        allerros.showErroMsg(atmsProMain.this,se.getMessage(),allerros.BACKPRESSDISABLE);
                        // 处理 JDBC 错误
                        se.printStackTrace();
                    }catch(Exception e){
                        allerros.showErroMsg(atmsProMain.this,e.getMessage(),allerros.BACKPRESSDISABLE);
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
                    allerros.showErroMsg(atmsProMain.this,e.getMessage(),allerros.BACKPRESSDISABLE);
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
            }
        }).start();
    }
    public void load_rec_device_namelist(){
        processdia.showPrecessWindows(atmsProMain.this,"加載 Loading","正在加載设备列表，請稍候...",processdia.BACKPRESS_DISABLE,true);

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
                        sql = "SELECT DISTINCT device_name FROM atms_pro.pro_list";
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
                        allerros.showErroMsg(atmsProMain.this,se.getMessage(),allerros.BACKPRESSDISABLE);
                        f=new StringBuffer("All\nconnect faild");
                        // 处理 JDBC 错误
                        se.printStackTrace();
                    }catch(Exception e){
                        allerros.showErroMsg(atmsProMain.this,e.getMessage(),allerros.BACKPRESSDISABLE);
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
                            rec_deviec_name.setAdapter(new ArrayAdapter<String>(atmsProMain.this,R.layout.support_simple_spinner_dropdown_item,name_list));
                            swipeRefreshLayout.setRefreshing(true);
                            processdia.setProcess(processdia.PROCESS_FINISH);
                            reloadrecBySizeAndName();

                        }
                    });
                }catch (Exception e){
                    processdia.setProcess(processdia.PROCESS_FINISH);
                    allerros.showErroMsg(atmsProMain.this,e.getMessage(),allerros.BACKPRESSDISABLE);
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





    ProgressDialog progressDialog;
    PopupWindow popupWindow;
    View contentView;
    Button cancel;
    Button isntall;
    public static EditText[] required_items;
    LinearLayout items_parent;
    String [] device_prop;
    TextView isaccesss;
    TextView install_instruction;
    @SuppressLint("ResourceType")
    public void showItemDetails(final pro_obj item){
        required_items=new EditText[item.getRequired_items().length];
        contentView =getLayoutInflater().inflate(R.layout.atms_pro_details,null);
        ////
        cancel=contentView.findViewById(R.id.cancer_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        isaccesss=contentView.findViewById(R.id.isaccess);
        install_instruction=contentView.findViewById(R.id.install_instruction);
        isntall=contentView.findViewById(R.id.install_buttton);
        if (Integer.valueOf(item.getRequire_level())<=viplevel){
            isaccesss.setVisibility(View.GONE);
            isntall.setVisibility(View.VISIBLE);
        }else{
            isaccesss.setVisibility(View.VISIBLE);
            isntall.setVisibility(View.INVISIBLE);
        }
        install_instruction.setText(item.getRecdetails().toString());
        //

        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画，指定刚才定义的style
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        // 按下android回退物理键 PopipWindow消失解决

        items_parent=contentView.findViewById(R.id.items_parent);

        //add views
        for (int i=0;i<item.getRequired_items().length;i++){
            EditText editText=new EditText(this);
            editText.setHint("请输入 "+item.getRequired_items()[i]+" 的值");

            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin=20;
            layoutParams.rightMargin=10;
            layoutParams.topMargin=10;
            layoutParams.bottomMargin=10;
            editText.setPadding(15,10,5,10);
            editText.setLayoutParams(layoutParams);
            editText.setMaxLines(1);
            editText.setBackgroundResource(R.drawable.editview_shape);
            required_items[i]=editText;
            items_parent.addView(editText);
        }

        isntall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 device_prop=new String[required_items.length+1];
                 device_prop[device_prop.length-1]="ATMS_VERSION=release_20210101";
                for (int a=0;a<required_items.length;a++){
                    device_prop[a]=item.getRequired_items()[a]+"="+required_items[a].getText().toString();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedWriter out = new BufferedWriter(new FileWriter("/sdcard/highsys/temp/device.prop"));

                            for (int i =1;i<device_prop.length;i++){
                                out.write(device_prop[i]+"\n");
                                out.flush();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    popupWindow.dismiss();

                                    progressDialog=new ProgressDialog(atmsProMain.this);
                                    progressDialog.setTitle("准备中");
                                    progressDialog.setIcon(R.drawable.iconnull);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setMessage("正在配置环境...稍后将会重启，请勿中断...");
                                    progressDialog.show();
                                    downloadFile(item);
                                }
                            });
                            out.close();
                        }catch (Exception e){
                            allerros.showErroMsg(atmsProMain.atmsProMain_instance,e.getMessage(),allerros.BACKPRESSENABLE);
                        }
                    }
                }).start();
            }
        });

    }

    private void downloadFile(pro_obj proObj){
        final pro_obj PROJECT=proObj;

        new Thread(new Runnable() {
            @Override
            public void run() {
                FtpUtils ftpUtils=new FtpUtils();
                boolean result= ftpUtils.downLoadFTP(ftpUtils.getFTPClient("121.5.103.76",21,"pro_downloader","JxMXJTGFSnhL8mNT"),"ADDON/",PROJECT.getRec_remote_local().substring(PROJECT.getRec_remote_local().lastIndexOf("/")+1),"/sdcard/highsys/temp/");
                if (result){
                    setCommand.execCommand(new String[]{"mkdir /mnt/atms_cache","mount /dev/block/bootdevice/by-name/cache /mnt/atms_cache","echo -e \"--update_package=/sdcard/highsys/temp/"+PROJECT.getRec_remote_local().substring(PROJECT.getRec_remote_local().lastIndexOf("/")+1)+"\" > /mnt/atms_cache/recovery/command","touch 79AE5BCD.log /mnt/atms_cache/","echo start_at_20200101 >>/mnt/atms_cache/79AE5BCD.log","reboot recovery"},true,false);
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            allerros.showErroMsg(atmsProMain.this,"失败啦...不知道怎么回事.可能没网吧!",allerros.BACKPRESSENABLE);
                        }
                    });
                }
            }
        }).start();
         }
}
