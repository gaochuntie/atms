package com.highsys.pages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Slide;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.highsys.atms_obj.settings;
import com.highsys.systemchanger.MainActivity;
import com.highsys.systemchanger.R;
import com.highsys.tool.setCommand;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class setvalues extends Fragment {
    AlertDialog.Builder meterr;
    String[] sets=new String[8];
    EditText btn1;
    EditText btn2;
    public static setvalues setvalues_i;
    EditText btn3;
    Button cancel_;
    EditText btn4;
    EditText btn7;
    EditText btn8;
    EditText btn5;
    Button fingers;
    public int isnull=0;
    EditText btn6;
    Handler h;
    String erro=null;
    int ifgo;
    com.kyleduo.switchbutton.SwitchButton switchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_setvalues,container,false);
        setvalues_i=this;
        meterr=new AlertDialog.Builder(MainActivity.context);
        //handler
        h=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what==-1){
                    meterro(erro);
                }
                if(msg.what==01){
                    Toast.makeText(MainActivity.context,"操作已执行",Toast.LENGTH_SHORT).show();
                    ((MainActivity)MainActivity.context).loadsystemSelecter();
                    // Toast.makeText(setvalues.this,settings.allsettings[0]+settings.allsettings[1]+settings.allsettings[2]+settings.allsettings[3]+settings.allsettings[4],Toast.LENGTH_SHORT).show();

                }
                if (msg.what==-100){
                    erro="对不起，严禁出现null值，如果您不知道如何填写，请填写I don't know，如果软件因为不正确的配置崩溃，请删除/sdcard/highsys文件夹重新打开软件";
                    meterro(erro);
                }
            }
        };
        //
        cancel_=view.findViewById(R.id.cancle_);
        btn1=view.findViewById(R.id.btn1);
        btn2=view.findViewById(R.id.btn2);
        btn3=view.findViewById(R.id.btn3);
        btn4=view.findViewById(R.id.btn4);
        btn5=view.findViewById(R.id.btn5);
        btn6=view.findViewById(R.id.btn6);
        btn7=view.findViewById(R.id.btn7);
        btn8=view.findViewById(R.id.btn8);
        switchButton= (com.kyleduo.switchbutton.SwitchButton)
                view.findViewById(R.id.fingers);

        switchButton.setChecked(true);//设置为真，即默认为真
        switchButton.isChecked();//被选中
        switchButton.toggle();     //开关状态
        switchButton.setEnabled(true);//false为禁用按钮
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                com.highsys.pages.fingers.fingermode=0;
                Intent i =new Intent(MainActivity.context, com.highsys.pages.fingers.class);
                startActivity(i);
                // Toast.makeText(setvalues.this,"jfioadsjf",Toast.LENGTH_SHORT).show();
            }
        });
        cancel_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)MainActivity.context).loadsystemSelecter();
            }
        });
        Button ok=view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    settings.allsettings[0]= String.valueOf(btn1.getText());
                    settings.allsettings[1]= String.valueOf(btn2.getText());
                    settings.allsettings[2]= String.valueOf(btn3.getText());
                    settings.allsettings[3]= String.valueOf(btn4.getText());
                    settings.allsettings[4]= String.valueOf(btn5.getText());
                    settings.allsettings[5]= String.valueOf(btn6.getText());
                    settings.allsettings[8]=String.valueOf(btn7.getText());
                    final String sysorder=String.valueOf(btn8.getText());
                    settings.allsettings[7]=sysorder;

                    // sets[6]=settings.username;
                    // sets[7]=settings.userid;
                    //开始写入配置文件

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                isnull=0;
                                for (int i=0;i<6;i++){
                                    if (settings.allsettings[i]==null){
                                        isnull=-1;
                                        erro="请全部填写！";
                                        meterr.setCancelable(true);
                                        h.sendEmptyMessage(-1);
                                        break;
                                    }
                                }
                                File f=new File("/sdcard/highsys/setup.txt");
                                if ( isnull!=-1){
                                    if (!f.exists()){
                                        f.createNewFile();
                                    }
                                    PrintWriter pw =new PrintWriter("/sdcard/highsys/setup.txt");
                                    int i =0;
                                    settings.allsettings[7]="System order moved to /system/atms_system_order.conf";
                                    while ( i < 20 & settings.allsettings[i]!=null){
                                        pw.write(settings.allsettings[i]+"#\n");
                                        i++;
                                    }
                                    settings.allsettings[7]=sysorder;
                                    pw.flush();
                                    pw.close();
                                    setCommand.execCommand(new String[]{"mv /sdcard/highsys/setup.txt "+settings.getSETUPFILE(),"echo "+settings.getSETUPFILE()+" > /mnt/persist_atms/atms_core.conf","echo "+sysorder+" >/system/atms_system_order.conf"},true,false);
                                    Log.d("XX","mv /sdcard/highsys/setup.txt"+settings.getSETUPFILE()+"\n"+"echo "+settings.getSETUPFILE()+" /mnt/persist_atms/atms_core.conf");
                                    h.sendEmptyMessage(01);
                                }else if (!f.exists()){
                                    erro="文件不存在！";
                                    h.sendEmptyMessage(-1);
                                }else if (isnull==-1){
                                    erro="不要因为看不懂就空着，你是小学生蛮？";
                                    meterr.setCancelable(true);
                                    h.sendEmptyMessage(-1);

                                }
                            }catch (Exception e){
                                erro=e.getMessage();
                                h.sendEmptyMessage(-1);
                            }
                        }
                    }).start();

                }catch (Exception e){

                }


                // Toast.makeText(setvalues.this,btn1.getText(),Toast.LENGTH_LONG).show();
            }
        });
        btn1.setText(settings.getSDCARDTYPE());
        btn2.setText("1");
        btn3.setText(settings.getBACKUPSDIR());
        btn4.setText(settings.getSYSTEMFILE());
        btn5.setText(settings.getIMAGEWORKDIR());
        btn6.setText(settings.getSHELLPATH());
        btn7.setText(settings.getSETUPFILE());
        btn8.setText(settings.getSYSORDER());
        checkfingers();
        return view;
    }

    public void meterro(String err){
        meterr.setMessage("\n"+err);
        meterr.show();
    }
    public void checkfingers(){
        setCommand.resultCom r=setCommand.execCommand(new String[]{"[ -e /data/highsyslock ] && echo 0 || echo 1"},true,true);
        if (r.ok.equals("0")){
            switchButton.setCheckedNoEvent(true);
        }
        if (r.ok.equals("1")){
           switchButton.setCheckedNoEvent(false);
        }
    }


}
