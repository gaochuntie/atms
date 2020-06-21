package com.highsys.systemchanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.SynthesisRequest;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class virtualsdcard extends AppCompatActivity {
    TextView cmdmsg;
    TextView tips;
    Button start;
    Button runing;
    Button forcrstop;
    EditText inputms;
    int sdtype;
    AlertDialog.Builder al;
    int a=0;

    setCommand.resultCom r;
    int index=0;
    String[] allmsg=new String[10];
    Handler h =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==01){

            }
        }
    };
    /*  0存储类型
    * 1分区表上线
    * 2最后一个分区号
    * 3新建分区大小
    * 4倒数第二分区结束为止
    * 5最大结束位置      */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_virtualsdcard);
        getWindow().setEnterTransition(new Slide().setDuration(500));
        getWindow().setExitTransition(new Slide().setDuration(1000));
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }
        al=new AlertDialog.Builder(virtualsdcard.this);
        Toast.makeText(virtualsdcard.this,"您已经同意所有责任不由作者承担！否则请退出！",Toast.LENGTH_LONG).show();
        initview();


    }
    public void initview(){
        cmdmsg=findViewById(R.id.cmdmsg);
        tips=findViewById(R.id.tips);
        start=findViewById(R.id.start);
        runing=findViewById(R.id.running);
        forcrstop=findViewById(R.id.stopfocly);
        inputms=findViewById(R.id.inputmsg);
        tips.setText("操作提示："+"请安装busybox，此为必须选项，然后点击开始按钮按照提示输入参数即可");
        //////
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tips.setText("操作提示："+"请输入芯片类型emmc或者ufs小写，点击继续");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Integer.valueOf(settings.allsettings[0])==0){
                            r=setCommand.execCommand(new String[]{"dd if=/dev/block/sda bs=512 count=1024 of=/data/highsys/sdabak "},true,true);
                        }
                        if (Integer.valueOf(settings.allsettings[0])==1){
                            r=setCommand.execCommand(new String[]{"dd if=/dev/block/mmcblk0 bs=512 count=1024 of=/data/highsys/mmcblk0bak "},true,true);
                        }

                    }
                }).start();
            }
        });
        runing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index==0){
                    String s = String.valueOf(inputms.getText());
                   if (s.equals("ufs")){
                       sdtype=0;
                      allmsg[0]="0";
                      inputms.setText("");
                   }
                   if (s.equals("emmc")){
                       sdtype=1;
                       allmsg[0]="1";
                       inputms.setText("");
                   }else if (!s.equals("emmc") && !s.equals("ufs")){
                       Toast.makeText(virtualsdcard.this,"对不起您输入的类型无效请立即退出重新操作！",Toast.LENGTH_LONG).show();
                   }
                    tips.setText("操作提示："+"请点击继续哦");

                }
                if (index==1){
                    tips.setText("操作提示："+"请输入Partition table holds up to 128 entries类似的一行的数字，然后继续哦");
                    if (sdtype==0){
                        r=setCommand.execCommand(new String[]{"sgdisk /dev/block/sda --print"},true,true);
                        cmdmsg.setText(r.ok+"\n"+r.error);
                    }
                    if (sdtype==1){
                        r=setCommand.execCommand(new String[]{"sgdisk /dev/block/mmcblk0 --print"},true,true);
                        cmdmsg.setText(r.ok+"\n"+r.error);
                    }
                }
                if (index==2){
                    allmsg[1]=String.valueOf(inputms.getText());
                    inputms.setText("");
                    tips.setText("操作提示："+"请写出最后一分区对应的左边数字然后点击继续");
                }
                if(index==3){
                    allmsg[2]=String.valueOf(inputms.getText());
                    Toast.makeText(virtualsdcard.this,"请确保没有超过存储上限，否则黑砖，即加上最后一个分区的大小要小于Logical前的数字！",Toast.LENGTH_LONG).show();
                    inputms.setHint("请勿超过存储上限，否则黑砖，即加上最后一个分区的大小要小于Logical前的数字！");
                    inputms.setText("");
                    if (Integer.valueOf(allmsg[1])<=Integer.valueOf(allmsg[2])){
                        if (allmsg[0].equals("0")){
                            r=setCommand.execCommand(new String[]{"sgdisk --resize-table 128 /dev/block/sda"},true,false);
                        }
                        if (allmsg[0].equals("1")){
                            r=setCommand.execCommand(new String[]{"sgdisk --resize-table 128 /dev/block/mmcblk0"},true,false);
                        }
                        al.setTitle("警告");
                        al.setMessage("您的分区表上线过小，已经被我扩大，请重启手机打开本软件重新操作即可，但是这可能会对您的设备造成黑砖后果，您现在可以选择重启继续操作，也可以选择返回点击中止操作即可恢复，捐赠项目，本人不承担任何责任！");
                        al.setNegativeButton("重启继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               r=setCommand.execCommand(new String[]{"reboot"},true,false);
                            }
                        });
                        al.show();
                    }
                    if (Integer.valueOf(allmsg[1])>Integer.valueOf(allmsg[2])){
                        tips.setText("操作提示："+"请输入您要新建的虚拟sd卡大小，单位mb，然后点击继续");
                    }
                }
                if (index==4){
                    if (allmsg[0].equals("0")){
                        r=setCommand.execCommand(new String[]{"cd /data/highsys/","chmod 777 ./parted","./parted /dev/block/sda p"},true,true);
                        cmdmsg.setText(r.ok+"\n"+r.error);
                    }
                    if (allmsg[0].equals("1")){
                        r=setCommand.execCommand(new String[]{"cd /data/highsys/","chmod 777 ./parted","./parted /dev/block/mmcblk0 p"},true,true);
                        cmdmsg.setText(r.ok+"\n"+r.error);
                    }
                    allmsg[3]=String.valueOf(inputms.getText());
                    inputms.setHint("纯数字无单位");
                    tips.setText("请输入倒数第二个分区号右边第二个很大的数字");
                    inputms.setText("");
                }
                if (index==5){
                    if (sdtype==0){
                        r=setCommand.execCommand(new String[]{"sgdisk /dev/block/sda --print"},true,true);
                        cmdmsg.setText(r.ok+"\n"+r.error);
                    }
                    if (sdtype==1){
                        r=setCommand.execCommand(new String[]{"sgdisk /dev/block/mmcblk0 --print"},true,true);
                        cmdmsg.setText(r.ok+"\n"+r.error);
                    }
                    inputms.setHint("纯数字无单位");
                    allmsg[4]=String.valueOf(inputms.getText());
                    tips.setText("请输入Logical前面的那个纯数字");
                    inputms.setText("");
                }
                if (index==6){
                   allmsg[5]=String.valueOf(inputms.getText());
                    inputms.setText("警告！！！");
                    tips.setText("警告：是否要继续，这可能导致你的设备黑砖，在重启前您都有机会恢复-中止");
                    cmdmsg.setText("如果您同意冒险请点击继续");
                }
                if (index==7){
                    cmdmsg.setText("Highsys\n请将/sdcard/下面的prepare-highsys1和2拷贝的电脑！务必必须！\n请做好准备点击继续进入rec手动依次，依次刷入/sdcard/下面的prepare-highsys1.zip然后重启到rec！重要，然后再刷入prepare-highsys2补丁文件！然后格式化data开机向进入群聊853314225获取一键激活虚拟sd卡面具模块即可，注意，这些都是免费的！只求个关注，但是您不妨捐赠1人民币（开始>支持我），1人民币也是对我莫大的支持！");
                    tips.setText("您如果不会9008救砖是小事情，如果您不想花钱花时间跑售后就是大事情");
                    inputms.setText("您仍然有机会停止！");
                    if (Integer.valueOf(allmsg[0])==0){
                        r=setCommand.execCommand(new String[]{"cp /data/highsys/zips/META-INF/com/google/android/update-binary /sdcard/highsys/temp/"},true,false);
                        tools.mkbinUFS(allmsg);
                        cmdmsg.setText(cmdmsg.getText()+"请查看结果：null为成功，其他输出为失败请自行百度翻译错误意思然后汇报给我QQ2041469901\n"+tools.errosmkbin);
                    }
                    if (Integer.valueOf(allmsg[0])==1){
                        r=setCommand.execCommand(new String[]{"cp /data/highsys/zips/META-INF/com/google/android/update-binary /sdcard/highsys/temp/"},true,false);
                        tools.mkbinEMMC(allmsg);
                        cmdmsg.setText(cmdmsg.getText()+"请查看结果：null为成功，其他输出为失败请自行百度翻译错误意思然后汇报给我QQ2041469901\n"+tools.errosmkbin);
                    }

                     }



                index++;
            }
        });
        forcrstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allmsg[0].equals("0")){
                    r=setCommand.execCommand(new String[]{"dd if=/data/highsys/sdabak bs=512 count=1024 of=/dev/block/sda"},true,false);
                }
                if (allmsg[0].equals("1")){
                    r=setCommand.execCommand(new String[]{"dd if=/data/highsys/mmcblk0bak bs=512 count=1024 of=/dev/block/mmcblk0"},true,false);
                }
             Toast.makeText(virtualsdcard.this,"恢复完毕！",Toast.LENGTH_LONG).show();
            }
        });
    }
}
