package com.highsys.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.highsys.tool.tools;
import com.highsys.atms_obj.settings;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.fragment.app.Fragment;

import com.highsys.systemchanger.MainActivity;
import com.highsys.systemchanger.R;
import com.highsys.tool.ShellUtils;

import org.apache.tools.ant.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class shellBox_frag extends Fragment {
    String workdir="mnt";
    int shell_type=0;
    TextView outmsg;
    String tem;
    EditText entershell;
    TextView su;
    TextView cd;
    String tips="\t\t\t欢迎使用ATMS多系统管理器\n\t\t使用方法\n\t\t-1-点击【分区】显示分区表信息\n\t\t-2-点击【扫描】打印存在的系统(依赖配置文件)并显示管理按钮，左滑即可出现\n\t\t-3-点击清除清空命令行缓存区\n\t\t-4-点击对应系统的按钮后，将会自动进入这个系统工作目录下面并列出可管理的项目\n\t\t-5-请按照完成的安卓shell命令向其中写入或者读取文件即可\n\t\t-6-如果有多条命令需要输入，请使用半角分割分号【;】\n\t\t-7-注意：此工具不是完整的终端模拟器，请不要当做终端模拟器使用\n\t\t-8-注意：不支持交互式程序，请将参数使用\n\t\t-【　echo 参数 | 交互式程序　】这种形式传递给交互式程序\n\t\t-9-列出可实现的操作：\n\t\t\t-给其他系统装字体\n\t\t\t-修复其他系统\n\t\t\t-拷贝其他系统的数据\n\t\t\t-修改其他系统配置\n\t\t\t-给其他系统安装应用程序\n\t\t\t-备份其他系统\n\t\t-10-注意：ATMS多系统其他终端命令行工具无法访问，本人已经测试N次，不然我不会花时间做这个垃圾终端\n\t\t-11-切换目录请在bash shell里面输入目标目录然后点击[CD]按钮";
    Button clean;
    List<Button_sys> sysBtnList=new ArrayList<Button_sys>();
    int scan_flag=0;
    Button scansys;
    public String TAG="shellBox_frag";
    RelativeLayout parent;
    Button stop;
    View.OnClickListener onClickListener;
    Button showpartition;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.shellbox,container,false);
        outmsg=(TextView) view.findViewById(R.id.outmsg);
        entershell=(EditText) view.findViewById(R.id.entershell);
        clean=(Button) view.findViewById(R.id.cleanboard);
        onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"click");
            }
        };
        scansys=view.findViewById(R.id.scansys);
        su=view.findViewById(R.id.su);
        cd=view.findViewById(R.id.cd);
        parent=view.findViewById(R.id.action_button_parent);
        showpartition=(Button) view.findViewById(R.id.showpatition);
        stop=(Button)view.findViewById(R.id.stopshell);
        outmsg.setText(tips);

        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shell_type=1;
                workdir=entershell.getText().toString();
                tem="echo 当前工作目录;pwd";
                getShellResult_dir(new String[]{"ATMS_ROOT=/mnt/"},workdir,false);
            }
        });
        scansys.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "ResourceType"})
            @Override
            public void onClick(View view) {
                if (scan_flag==0){
                    LinearLayout linearLayout=new LinearLayout(getContext());
                    RelativeLayout.LayoutParams params ;
                    params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, tools.dip2px(getContext(),50));
                    params.addRule(RelativeLayout.RIGHT_OF,R.id.scansys);
                    params.setMargins(tools.dip2px(getContext(),3),tools.dip2px(getContext(),3),tools.dip2px(getContext(),3),tools.dip2px(getContext(),3));
                    parent.addView(linearLayout,params);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    for (int i=0;i<sysBtnList.size();i++){
                        linearLayout.addView(sysBtnList.get(i));
                    }
                    scan_flag++;
                }
                tem="ls /dev/block/bootdevice/by-name/userdata*;ls /dev/block/bootdevice/by-name/system*;echo 请查看是否有类似于userdatabak或者userdata1这样的输出，若没有，将不支持,并且bak结尾的分区只能通过SYS1按钮管理";
                getShellResult(false);

                }
        });
        showpartition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settings.getSDCARDTYPE().equals(0)){
                    tem="sgdisk /dev/block/sda --print;sgdisk /dev/block/sde --print";
                    entershell.setText("正在工作...");
                    getShellResult(false);
                }else {
                    tem="sgdisk /dev/block/mmcblk0 --print";
                    entershell.setText("正在工作...");
                    getShellResult(false);
                }

            }
        });
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outmsg.setText(tips);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)MainActivity.context).replaceFragment(new shellBox_frag());
            }
        });
        entershell.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    tem=entershell.getText().toString();
                    entershell.setText("");
                    if (event.getAction()==KeyEvent.ACTION_DOWN){
                        //result=shellbox.execCommand(new String[]{entershell.getText().toString()},true,true);
                        //if (result.errorMsg==null || result.errorMsg.equals("")){
                        //    outmsg.setText(outmsg.getText().toString()+"\n"+"# "+entershell.getText().toString()+"\n"+"\n"+result.successMsg);
                        //}else if (result.successMsg==null || result.successMsg.equals("")){
                        //    outmsg.setText(outmsg.getText().toString()+"\n"+"# "+entershell.getText().toString()+"\n"+result.errorMsg+"\n");
                        //}else {
                        //    outmsg.setText(outmsg.getText().toString()+"\n"+"# "+entershell.getText().toString()+"\n"+result.errorMsg+"\n"+result.successMsg);
                        //}
                        //entershell.setText("");
                        if (!tem.equals("")){
                            entershell.setText("正在工作...");
                            if (shell_type==0){
                                getShellResult(true);
                            }else {
                                getShellResult_dir(new String[]{"ATMS_ROOT=/mnt/"},workdir,true);
                            }

                            //outmsg.setText(outmsg.getText().toString()+"\n"+"# "+tem+"\n"+ShellUtils.exec(tem,true,true).successMsg);
                        }

                    }
                }
                return(event.getKeyCode() == KeyEvent.KEYCODE_ENTER);

            }
        });
        getSysBtni();
        return  view;
    }
    public void getSysBtni(){
        for (int i=0;i<SysSelecter.sysobjList.size();i++){
            final Button_sys b=new Button_sys(getContext());
            b.setText("Sys"+(i+1));
            b.index_sys=(i+1);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    entershell.setTextColor(Color.RED);
                    su.setTextColor(Color.RED);
                    su.setText("ATMS #");
                    if (b.index_sys==1){
                        tem="mkdir /mnt/systembak;mkdir /mnt/userdatabak;mount /dev/block/bootdevice/by-name/systembak /mnt/systembak;mount /dev/block/bootdevice/by-name/userdatabak /mnt/userdatabak";
                        getShellResult(false);
                    }
                    tem= "mkdir /mnt/system"+b.index_sys+";mkdir /mnt/userdata"+b.index_sys+";mount /dev/block/bootdevice/by-name/system"+b.index_sys+" /mnt/system"+b.index_sys+";mount /dev/block/bootdevice/by-name/userdata"+b.index_sys+" /mnt/userdata"+b.index_sys+";ls /mnt/"+";echo 当前工作目录/mnt/，你现在可以使用linux命令操作各个system和userdata内容了,注意，切换目录请在bash shell里面输入你想要cd的目录然后点击[CD]按钮";
                    getShellResult_dir(new String[]{"ATMS_ROOT=/mnt/"},"/mnt/",false);
                    Toast.makeText(getContext(),"Sys "+b.index_sys+" Touched",Toast.LENGTH_SHORT).show();

                    shell_type=1;
                    workdir="/mnt/";
                }
            });
            sysBtnList.add(b);
        }
    }
    String result = "unknow";
    public void getShellResult(final boolean isshowcommand) {
        result="";
        new Thread(new Runnable() {
            @Override
            public void run() {

                Runtime mRuntime = Runtime.getRuntime();
                try {
                    Process mProcess = mRuntime.exec("su -c "+tem.trim());
                    InputStream is = mProcess.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader mReader = new BufferedReader(isr);
                    String string;
                    while ((string = mReader.readLine()) != null) {
                        result = result + string + "\n";
                    }
                    if (result.equals("")){
                        result="command not know or other unknown erro.未找到命令或其他错误";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isshowcommand){
                            outmsg.setText(outmsg.getText().toString()+"\n"+"# "+tem+"\n"+result);
                        }else {
                            outmsg.setText(outmsg.getText().toString()+"\n"+"# "+"\n"+result);
                        }

                        entershell.setText("");
                    }
                });
            }
        }).start();

        //
    }
    public void getShellResult_dir(final String[] env, final String dir, final boolean isshowcommand) {
        result="";
        new Thread(new Runnable() {
            @Override
            public void run() {

                Runtime mRuntime = Runtime.getRuntime();
                try {
                    Process mProcess = mRuntime.exec("su -c "+tem.trim(),env,new File(dir));
                    InputStream is = mProcess.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader mReader = new BufferedReader(isr);
                    String string;
                    while ((string = mReader.readLine()) != null) {
                        result = result + string + "\n";
                    }
                    if (result.equals("")){
                        result="command not know or other unknown erro.未找到命令或其他错误";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isshowcommand){
                            outmsg.setText(outmsg.getText().toString()+"\n"+"# "+tem+"\n"+result);
                        }else {
                            outmsg.setText(outmsg.getText().toString()+"\n"+"# "+"\n"+result);
                        }

                        entershell.setText("");
                    }
                });
            }
        }).start();

        //
    }

    @SuppressLint("AppCompatCustomView")
    class Button_sys extends Button {
        int index_sys=0;
        public Button_sys(Context context) {
            super(context);
        }
    }
}
