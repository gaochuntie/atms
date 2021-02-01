package com.highsys.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.highsys.atms_obj.settings;
import com.highsys.systemchanger.MainActivity;
import com.highsys.systemchanger.R;
import com.highsys.tool.setCommand;
import com.highsys.tool.tools;

import java.io.File;
import java.io.PrintWriter;

public class hello extends AppCompatActivity {
    TextView wel;
    String messages;
    Button go;
    EditText uname;
    Button ty0;
    Button ty1;
    Button help;
    Handler h =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==00){
                wel.setText(wel.getText()+messages);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        /////
        new Thread(new Runnable() {
            @Override
            public void run() {
                setCommand.resultCom r=setCommand.execCommand(new String[]{"[ -e /dev/block/mmcblk0 ] && echo EMMC || echo UFS"},true,true);
                messages=r.ok;
                if (messages.equals("EMMC")){
                    settings.allsettings[0]="1";
                }
                if (messages.equals("UFS")){
                    settings.allsettings[0]="0";
                }
                h.sendEmptyMessage(00);
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(hello.this,settings.getTempdir(),Toast.LENGTH_LONG).show();
                        }
                    });
                    tools.preaik(hello.this);
                }catch (Exception e ){
                    meterro(e.getMessage(), allerros.BACKPRESSENABLE);
                }

            }
        }).start();
        ///
        wel=findViewById(R.id.welcome);
        go=findViewById(R.id.hellogo);
       uname=findViewById(R.id.username);
      ty0=findViewById(R.id.type0);
      ty1=findViewById(R.id.type1);
       help=findViewById(R.id.help);
       ty0.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               settings.allsettings[0]="0";
               ty0.setBackgroundResource(R.color.sdtypeback);
               ty1.setBackgroundResource(R.drawable.winbut);
           }
       });
        ty1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                settings.allsettings[0]="1";
                ty1.setBackgroundResource(R.color.sdtypeback);
                ty0.setBackgroundResource(R.drawable.winbut);
            }
        });



        help.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i =new Intent(hello.this, thehelps.class);
               startActivity(i);
           }
       });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.allsettings[6]=String.valueOf(uname.getText());
                settings.allsettings[7]="System order moves to /system/atms_system_order.conf";
                initfile();
                Intent i =new Intent(hello.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void initfile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] sets=new String[20];
                sets[0]=settings.allsettings[0];
                sets[1]="0";
                sets[2]="/sdcard/highsys/backups/";
                sets[3]="/sdcard/highsys/systems/";
                sets[4]="/sdcard/highsys/imgwork/";
                sets[5]="0";
                sets[6]=settings.allsettings[6];
                sets[7]="System order moved to /system/atms_system_order.conf";
                sets[8]="/sdcard/highsys/setup.txt";
                sets[9]="unknown";
                try{
                    int isnull=0;
                    File f=new File("/sdcard/highsys/setup.txt");
                    if (f.exists()&&isnull!=-1){
                        PrintWriter pw =new PrintWriter("/sdcard/highsys/setup.txt");
                        for (int i=0;i<10;i++){
                            pw.write(sets[i]+"#\n");
                        }
                        pw.flush();
                        pw.close();
                    }else if (!f.exists()){
                    }else if (isnull==-1){
                    }
                }catch (Exception e){
                }
            }

        }).start();
    }
    public void meterro(String err,int backpres){
        settings.processcache=1;
        allerros.backpressed=backpres;
        allerros.erromsg=err;
        Intent ii =new Intent(hello.this,allerros.class);
        startActivity(ii);

    }
}
