package com.highsys.systemchanger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class advence extends Fragment {
    View view;
    Button unpack;
    Button repack;
    Button cleanup;
    Button flashrec;
    Button allrun;
    TextView workpath;
    Button virtualsd;
    Button noavb;
    Button contrsys1;
    Button contrsys2;
    Button contrsys3;
    Button contrsys4;
    Button installsys;
    Button installmobile;
    ///////
    String workmsg;
    String worktitle;
    setCommand.resultCom r;
    //
    Handler h =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==8) {
                showpross(workmsg, worktitle);
            }
            if (msg.what==06){
                // pd.dismiss();
                hitpross();
            }
            if (msg.what==01){
                Toast.makeText(MyApplication.getContext(),"打包完成赶紧刷入测试吧！",Toast.LENGTH_LONG).show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_advence,container,false);
        MainActivity.nav_view.setCheckedItem(R.id.advence);
        initview();
        return view;
    }

    public void initview(){
        contrsys1=view.findViewById(R.id.contsys1);;
        contrsys2=view.findViewById(R.id.contsys2);
        contrsys3=view.findViewById(R.id.contsys3);
        contrsys4=view.findViewById(R.id.contsys4);
        unpack=view.findViewById(R.id.unpack);
        repack=view.findViewById(R.id.repack);
        cleanup=view.findViewById(R.id.cleanup);
        flashrec=view.findViewById(R.id.flashrec);
        allrun=view.findViewById(R.id.allrun);
        workpath=view.findViewById(R.id.workpath);
        workpath=view.findViewById(R.id.workpath);
        noavb=view.findViewById(R.id.noavb2);
        workpath.setText(settings.allsettings[4]);
        virtualsd=view.findViewById(R.id.virtualsd);
        installsys=view.findViewById(R.id.installsys);
        installmobile=view.findViewById(R.id.insmobile);
        //
        contrsys1.startAnimation(MainActivity.translateAnimation);
        contrsys2.startAnimation(MainActivity.translateAnimation);
        contrsys3.startAnimation(MainActivity.translateAnimation);
        contrsys4.startAnimation(MainActivity.translateAnimation);
        unpack.startAnimation(MainActivity.translateAnimation);
        repack.startAnimation(MainActivity.translateAnimation);
        cleanup.startAnimation(MainActivity.translateAnimation);
        flashrec.startAnimation(MainActivity.translateAnimation);
        allrun.startAnimation(MainActivity.translateAnimation);
        workpath.startAnimation(MainActivity.translateAnimation);
        noavb.startAnimation(MainActivity.translateAnimation);
        virtualsd.startAnimation(MainActivity.translateAnimation);
        installsys.startAnimation(MainActivity.translateAnimation);
        installmobile.startAnimation(MainActivity.translateAnimation);
        //////////////////////////////////////////
        installsys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MyApplication.getContext(),installsystems.class);
                startActivity(i);
                    }
        });
        contrsys1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r=setCommand.execCommand(new String[]{"mkdir /mnt/system1","mkdir /mnt/userdata1","su -c mount -t ext4 /dev/block/by-name/system1 /mnt/system1","mount -t ext4 /dev/block/by-name/systembak /mnt/system1","mount -t ext4 /dev/block/by-name/userdata1 /mnt/userdata1","mount -t ext4 /dev/block/by-name/userdatabak /mnt/userdata1"},true,true);
                Toast.makeText(MyApplication.getContext(),"请到/mnt/目录下面找到对应系统文件夹编辑！请勿管理当前系统！" ,Toast.LENGTH_LONG).show();
            }
        });
        contrsys2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r=setCommand.execCommand(new String[]{"mkdir /mnt/system2","mkdir /mnt/userdata2","mount -t ext4 /dev/block/by-name/system2 /mnt/system2","mount -t ext4 /dev/block/by-name/systembak /mnt/system2","mount -t ext4 /dev/block/by-name/userdata2 /mnt/userdata1","mount -t ext4 /dev/block/by-name/userdatabak /mnt/userdata2"},true,false);
                Toast.makeText(MyApplication.getContext(),"请到/mnt/目录下面找到对应系统文件夹编辑！请勿管理当前系统！",Toast.LENGTH_LONG).show();
            }
        });
        contrsys3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r=setCommand.execCommand(new String[]{"mkdir /mnt/system3","mkdir /mnt/userdata3","mount -t ext4 /dev/block/by-name/system3 /mnt/system3","mount -t ext4 /dev/block/by-name/systembak /mnt/system3","mount -t ext4 /dev/block/by-name/userdata3 /mnt/userdata1","mount -t ext4 /dev/block/by-name/userdatabak /mnt/userdata3"},true,false);
                Toast.makeText(MyApplication.getContext(),"请到/mnt/目录下面找到对应系统文件夹编辑！请勿管理当前系统！",Toast.LENGTH_LONG).show();
            }
        });
        contrsys4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContext(),"当前系统请自行管理",Toast.LENGTH_LONG).show();
            }
        });
        noavb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r=setCommand.execCommand(new String[]{"dd if=/data/highsys/vbmeta.img of=/dev/block/by-name/vbmeta"},true,false);
                Toast.makeText(MyApplication.getContext(),"关闭成功：刷入官方包还需要再次关闭！",Toast.LENGTH_LONG).show();
            }
        });
        virtualsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MyApplication.getContext(),virtualsdcard.class);
                //Toast.makeText(MyApplication.getContext(),"正在加班更新中",Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        });
        installmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MyApplication.getContext(),com.highsys.systemchanger.install_mobile.class);
                startActivity(i);
            }
        });
        unpack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        workmsg="明月几时有？等我解压完\n请稍后...";
                        worktitle="正在解压";
                        h.sendEmptyMessage(8);
                        r=setCommand.execCommand(new String[]{"cd /data/highsys/","./unpackimg.sh "+settings.allsettings[4]+"*.img","cp -r /data/highsys/ramdisk "+settings.allsettings[4]},true,true);
                        h.sendEmptyMessage(06);
                        worktitle="解压完成！";
                        workmsg="明月天天有！请到您的img目录找到ramgisk编辑吧，记得回来打包哟！如果不再使用请一定点击清除工作文件否则下一次解压无法正常工作！";
                        h.sendEmptyMessage(8);
                        try {
                            Thread.sleep(6000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        h.sendEmptyMessage(06);
                    }
                }).start();
            }
        });
        repack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        workmsg="明月几时有？等我打包完\n请稍后...";
                        worktitle="正在打包";
                        h.sendEmptyMessage(8);
                        r=setCommand.execCommand(new String[]{"mv "+settings.allsettings[4]+"ramdisk /data/highsys/"},true,true);
                        r=setCommand.execCommand(new String[]{"cd /data/highsys/","./repackimg.sh"},true,true);
                        r=setCommand.execCommand(new String[]{"mv /data/highsys/*.img "+settings.allsettings[4]},true,true);
                        h.sendEmptyMessage(06);
                        worktitle="打包完成！";
                        workmsg="明月天天有！请到您的img目录找到image-new.img刷入吧，记得点击清理哟！如果不清理下一次无法正常工作！";
                        h.sendEmptyMessage(8);
                        try {
                            Thread.sleep(6000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        h.sendEmptyMessage(06);
                    }
                }).start();
            }
        });
        cleanup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r=setCommand.execCommand(new String[]{"rm -rf /data/highsys/ramdisk/*","rm -rf /data/highsys/split_img"},true,true);
            }
        });
        flashrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        allrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"管理员QQ:364988395 | Rec Maker QQ : 2094858273",Snackbar.LENGTH_LONG).show();
            }
        });
    }
    public void showpross(String pross,String title){
        settings.processcache=0;
        settings.processmsg=pross;
        settings.processtitle=title;
        // pd.show();
        Intent i =new Intent(MyApplication.getContext(),processdia.class);
        startActivity(i);
    }
    public void hitpross(){
        settings.processcache=1;
    }


}
