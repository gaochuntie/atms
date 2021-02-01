package com.highsys.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.highsys.atms_obj.settings;
import com.highsys.pages.allerros;
import com.highsys.pages.processdia;
import com.highsys.systemchanger.MainActivity;
import com.highsys.systemchanger.R;
import com.highsys.tool.Actions;
import com.highsys.tool.UnzipFromAssets;
import com.highsys.tool.comprass;
import com.highsys.tool.setCommand;

import org.apache.tools.ant.taskdefs.BUnzip2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirmwareKit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirmwareKit extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FirmwareKit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirmwareKit.
     */
    // TODO: Rename and change types and number of parameters
    public static FirmwareKit newInstance(String param1, String param2) {
        FirmwareKit fragment = new FirmwareKit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    Button tester;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_firmware_kit, container, false);
        //tostart vars
        card1=view.findViewById(R.id.card1);
        card2=view.findViewById(R.id.card2);
        card3=view.findViewById(R.id.card3);
        card4=view.findViewById(R.id.card4);
        choose=view.findViewById(R.id.choose_disk_bt);
        check=view.findViewById(R.id.check_disk_bt);
        back=view.findViewById(R.id.back_disk_bt);
        //
        card1.setCardBackgroundColor(Color.YELLOW);
        tester=view.findViewById(R.id.scan_disk_bt);
        tester.setOnClickListener(this);
        choose.setOnClickListener(this);
        check.setOnClickListener(this);
        back.setOnClickListener(this);
        //
        alertDialog_notice=new AlertDialog.Builder(getContext());
        getNotice();
        //unzip file
        UnzipFromAssets.copyAssetsDir2Phone(getActivity(),"META-INF");
        UnzipFromAssets.copyAssetsDir2Phone(getActivity(),"ramdisk");
        return view;
    }
     String log="FIRM";
    TreeMap<String,String> partitions=new TreeMap<>();
    Map<String,String> checkedpart=new TreeMap<>();
    ProgressDialog builder;
    String backupto=settings.getBACKUPSDIR();
    String[] partnames=null;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.scan_disk_bt:
                 builder = ProgressDialog.show(getContext(), "加载中", "正在扫描磁盘，请稍候...", true, false, null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //tostart  set Data_dir

                        Log.d(log,settings.getTempdir());
                        setCommand.execCommand(new String[]{"mv  "+settings.getTempdir()+"ramdisk /data/highsys/","chmod 777 /data/highsys/ramdisk/Block_Device_Name/*"},true,false);
                        //
                        //String s=setCommand.execCommand(new String[]{"ls -l /dev/block/by-name/userdata"},true,true).ok;
                        //String s0=s.substring(s.lastIndexOf(" ")+1);
                        String s0="/dev/block/by-name/userdata";
                        //String[] s1= setCommand.execCommand(new String[]{"cat /proc/partitions | sed 's/^/@&/g' | sed 's/[ \\t]/+/g'"},true,true).ok.split("@");
                        String[] s1= setCommand.execCommand(new String[]{"sh /data/highsys/ramdisk/Block_Device_Name/Block_Device_Name.sh"},true,true).ok.split("!");
                        Log.d(log,s1[0]);
                        for (int i=0;i<s1.length;i++){
                            String[]  tmp=s1[i].split("=");
                            Log.d(log, "List Size : "+String.valueOf(tmp.length));
                            if (!s0.equals(tmp[0])){
                                partitions.put(tmp[1],tmp[0]);
                                Log.d(log,tmp[0]);
                                Log.d(log,tmp[1]);
                            }
                        }
                        Log.d(log, String.valueOf(partitions.size()));
                        Log.d(log,partitions.get("system"));
                        setCommand.execCommand(new String[]{"rm -rf /data/highsys/ramdisk/Block_Device_Name/by-name.log"},true,false);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                card1.setCardBackgroundColor(Color.GREEN);
                                builder.dismiss();
                            }
                        });
                    }
                }).start();
                break;
            case R.id.choose_disk_bt:
                showMutilAlertDialog(view,partitions);
                break;
            case R.id.check_disk_bt:


                final EditText inputServer = new EditText(getContext());
                inputServer.setHint("请输入一个存在并且空间足够目录");
                inputServer.setText(backupto);
                inputServer.setFocusable(true);
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("目标地址")
                        .setIcon(R.drawable.iconnull)
                        .setMessage("请输入一个控件足够的目录,如果是分区,需要以全局命名空间打形式挂载,注意,本工具不会备份data分区！并且请禁止将备份分区作为目标分区,否ze死循环,且目标分区所有数据都会被破坏！")
                        .setView(inputServer)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder1.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                card3.setCardBackgroundColor(Color.GREEN);
                                backupto = inputServer.getText().toString();
                            }
                        });
                builder1.show();
                break;
            case R.id.back_disk_bt:

                final String[] checkpartitions = new String[checkedpart.size()];
                partnames=new String[checkedpart.size()];
                Set<String> set1 = checkedpart.keySet();
                int counter=0;
                for (String i : set1) {
                    checkpartitions[counter]=checkedpart.get(i);
                    partnames[counter]=i;
                    Log.d(log,"checkedpartitions 00412: "+checkpartitions[counter]);
                    counter++;

                }

                File file=new File(backupto+"backupList.list");
                File restore=new File(backupto+"restoreList.list");
                File meta=new File(settings.getTempdir()+"META-INF");
                File cache=new File(backupto+"cache");
                File script=new File(backupto+"cache/META-INF/com/google/android/update-binary");
                BufferedWriter bufferedWriter = null;
                BufferedWriter bufferedWriter1=null;
                BufferedWriter scriptwriter=null;
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    if (!restore.exists()){
                        restore.createNewFile();
                    }
                    if (!cache.exists()){
                        cache.mkdir();
                        if (meta.exists()){
                            meta.renameTo(new File(cache.getPath()+"/META-INF"));
                        }else {
                            allerros.showErroMsg(getContext(),"文件夹META-INF不存在,关闭软件重试",allerros.BACKPRESSDISABLE);
                            break;
                        }
                    }
                    if (!script.exists()){
                        allerros.showErroMsg(getContext(),"恢复脚本初始文件不存在！关闭软件",allerros.BACKPRESSDISABLE);
                        break;
                    }
                    scriptwriter=new BufferedWriter(new FileWriter(script,true));


                    //Log.d(log,"checked partitions 51315 : "+checkpartitions[0]);
                    bufferedWriter=new BufferedWriter(new FileWriter(file));
                    bufferedWriter1=new BufferedWriter(new FileWriter(restore));
                    for (int a=0;a<checkpartitions.length;a++){
                        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        Log.d(log,"writer 5585: "+checkpartitions[a]);
                        bufferedWriter.write(checkpartitions[a]+"\n");
                        bufferedWriter.flush();

                        bufferedWriter1.write(partnames[a]+"_atms.bak.img 恢复到　"+checkpartitions[a]+"\n");
                        bufferedWriter1.flush();

                        //question : No free space to unzip imgs in /
                        scriptwriter.write("unzip $3 "+partnames[a]+"_atms.bak.img"+" -d /atms"+"\n");
                        scriptwriter.write("ui_print \"-- Restoring "  +  partnames[a]  +   "  to "+checkpartitions[a]+  "\""+"\n");
                        scriptwriter.write("dd if=/atms/"+partnames[a]+"_atms.bak.img"+" of="+checkpartitions[a]+"\n");
                        scriptwriter.write("rm -rf /atms/"+partnames[a]+"_atms.bak.img"+"\n");
                        scriptwriter.flush();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (bufferedWriter!=null){
                        try {
                            bufferedWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                final String script_path=backupto+"backupList.list";
                Log.d(log,"ListPath ："+script_path);
                if (script_path.equals("")){
                    Log.d(log,"Null warning");
                    Snackbar.make(view,"严禁为空　Null Erro !",Snackbar.LENGTH_LONG).show();
                }else {
                    processdia.showPrecessWindows(MainActivity.context, "Custom  Backuping", "Backup from script file ...\nPlease wait...Back up to" + backupto, processdia.BACKPRESS_DISABLE, true, new Actions() {
                        @Override
                        public void action() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int flag=0;
                                    setCommand.resultCom resultCom = new setCommand.resultCom(225);
                                        try {
                                            //getlist
                                            List<String> partionList=new ArrayList<String>();
                                         for (int v=0;v<checkpartitions.length;v++){
                                                    partionList.add("dd if="+checkpartitions[v]+" of="+backupto+"cache/"+partnames[v]+"_atms.bak.img");
                                                Log.d(log,"Partition path : "+partionList.get((v)));
                                            }
                                            String[] strs1 = partionList.toArray(new String[partionList.size()]);
                                            resultCom=setCommand.execCommand(strs1,true,true);


                                            comprass.compressWithoutBaseDir(backupto+"cache",backupto+"firmware_atms.zip");
                                            //end compress
                                        }catch (Exception e) {
                                            flag=1;
                                            //erro样本
                                            e.printStackTrace();
                                            processdia.showPrecessWindows(MainActivity.context, "Erro", e.getMessage(), processdia.BACKPRESS_ENABLE, new Actions() {
                                                @Override
                                                public void action() {
                                                    processdia.setProcess(processdia.PROCESS_FINISH);
                                                }
                                            }, "返回");
                                        }
                                            //endBackup
                                    if (flag!=1) {
                                        processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);

                                        try {
                                            Thread.sleep(2000);
                                        } catch (Exception w) {
                                            w.printStackTrace();
                                        }
                                        processdia.closeGIF(true);
                                        processdia.setPromsg("Backup  completely (完成)!请保存好的备份文件，一共有一个restoreList.list供备份参考，一个firmware_atms.zip是恢复备份的底层打卡刷包，backupList.list供参考的备份列表！！ Result(结果) : \n" +  resultCom.error + "\nPress back to back.");
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                card4.setCardBackgroundColor(Color.GREEN);
                                            }
                                        });
                                        //finished
                                    }


                                }
                            }).start();
                        }
                    });
                }
                break;
            default:
                break;

        }
    }
    //tostart
    //vars
    CardView card1;
    CardView card2;
    CardView card3;
    CardView card4;
    Button choose;
    Button check;
    Button back;
    AlertDialog.Builder alertDialog_notice;
    // vars

    //

    private AlertDialog alertDialog3;
    public void showMutilAlertDialog(View view,TreeMap<String,String> map){
        final String[] items = new String[map.size()];
        final boolean[] checkedItems=new boolean[map.size()];
        Set<String> set1 = map.keySet();
        int counter=0;
        for (String i : set1) {
            items[counter]=i;
            checkedItems[counter]=true;
            counter++;
        }


        for(int i=0;i<items.length;i++){
            Log.d(log,"ｉｔｅｍｓ　：　"+items[i]);
        }
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("请选择需要备份的分区");
        /**
         *第一个参数:弹出框的消息集合，一般为字符串集合
         * 第二个参数：默认被选中的，布尔类数组
         * 第三个参数：勾选事件监听
         */
        checkedpart= (Map<String, String>) partitions.clone();
        alertBuilder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if (isChecked){
                    checkedpart.put(items[i],partitions.get(items[i]));
                    //Toast.makeText(getContext(), "选择" + items[i], Toast.LENGTH_SHORT).show();
                }else {
                    checkedpart.remove(items[i]);
                    //Toast.makeText(getContext(), "取消选择" + items[i], Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog3.dismiss();
                card2.setCardBackgroundColor(Color.GREEN);
                card3.setCardBackgroundColor(Color.YELLOW);
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog3.dismiss();
            }
        });
        alertDialog3 = alertBuilder.create();
        alertDialog3.show();
    }
    public void scan_disk(){
        if (settings.getSDCARDTYPE()=="ufs"){

        }else if (settings.getSDCARDTYPE()=="emmc"){

        }else {
            Snackbar.make(tester,"配置文件错误",Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setCommand.execCommand(new String[]{"rm -rf /data/highsys/ramdisk/Block_Device_Name/by-name.log"},true,false);
    }
    public void getNotice(){
        final String notice="严禁备份userdata分区!　若是为了解决双系统密码问题而来，无需备份system vendor 虚拟sd splash recovery boot 以此缩小输出文件大小,你也可以自己摸索，哪些分区是不必备份的，恢复时请勿中断，否则容易导致底层损坏而黑砖！ \n$$$$$$$$$$z\n作者只是个高一的，如果可以，对我一点点的支持［主菜单＞支持］也是对整个教育界的点滴付出^-^";
        alertDialog_notice.setMessage(notice);
        alertDialog_notice.setIcon(R.drawable.iconnull);
        alertDialog_notice.setTitle("通知");
        alertDialog_notice.setCancelable(true);
        alertDialog_notice.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder()
                        .url("http://59.110.167.17/atms_msg/firmware_notice.txt")
                        .build();
                try {
                    Response response=client.newCall(request).execute();
                    final String msg=response.body().string();
                    Log.d(log,msg);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog_notice.setMessage(notice+msg);

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog_notice.show();
                    }
                });
            }
        }).start();

    }
}