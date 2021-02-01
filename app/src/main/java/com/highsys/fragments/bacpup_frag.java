package com.highsys.fragments;

import com.google.android.material.snackbar.Snackbar;
import com.highsys.atms_obj.settings;
import com.highsys.pages.processdia;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.highsys.systemchanger.MainActivity;
import com.highsys.systemchanger.MyApplication;
import com.highsys.systemchanger.R;
import com.highsys.tool.Actions;
import com.highsys.tool.setCommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class bacpup_frag extends Fragment implements View.OnClickListener{
    CardView ufs;
    CardView emmc;
    CardView old_option;
    CardView gengeral_back;
    CardView general_partition;
    CardView custom_back;
    Button sda;
    Button sdb;
    Button sdc;
    Button sdd;
    Button sde;
    Button sdf;
    Button backup_old;

    Button mmcblk0;
    Button boot_back;
    Button dtbo_back;
    Button persist_back;
    Button system_back;
    Button vendor_back;
    Button userdata_back;
    Button cust_back;
    EditText backup_list;
    Button custom_back_go;
    Animation frag_in;
    Animation frag_out;
    TextView core_type;
    TextView backup_dir;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        View view=inflater.inflate(R.layout.backup_frag,container,false);
        frag_in= AnimationUtils.loadAnimation(MyApplication.getContext(),R.anim.fragment_enter);
        frag_out= AnimationUtils.loadAnimation(MyApplication.getContext(),R.anim.fragment_exit_pop);
        frag_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (settings.getSDCARDTYPE()){
                    case "0":
                        emmc.setVisibility(View.GONE);
                        //emmc.setVisibility(View.GONE);
                        break;

                    case "1" :
                        ufs.setVisibility(View.GONE);
                        //ufs.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //
        old_option=view.findViewById(R.id.old_option);
        backup_old=view.findViewById(R.id.backup_old);
        core_type=view.findViewById(R.id.core_type);
        backup_dir=view.findViewById(R.id.backup_dir);
        ufs=view.findViewById(R.id.ufs);
        emmc=view.findViewById(R.id.emmc);
        gengeral_back=view.findViewById(R.id.generalback);
        general_partition=view.findViewById(R.id.general_partition);
        sda=view.findViewById(R.id.sda);
        sdb=view.findViewById(R.id.sdb);
        sdc=view.findViewById(R.id.sdc);
        sdd=view.findViewById(R.id.sdd);
        sde=view.findViewById(R.id.sde);
        sdf=view.findViewById(R.id.sdf);
        mmcblk0=view.findViewById(R.id.mmcblk0);
        boot_back=view.findViewById(R.id.boot_backup);
        dtbo_back=view.findViewById(R.id.dtbo_backup);
        persist_back=view.findViewById(R.id.persist_back);
        system_back=view.findViewById(R.id.system_part);
        vendor_back=view.findViewById(R.id.vendor_part);
        userdata_back=view.findViewById(R.id.userdata_part);
        cust_back=view.findViewById(R.id.cust_part);
        custom_back_go=view.findViewById(R.id.backup_btn_custom);
        backup_list=view.findViewById(R.id.backup_listfile);
        old_option.startAnimation(frag_in);
        ufs.startAnimation(frag_in);
        emmc.startAnimation(frag_in);
        general_partition.startAnimation(frag_in);
        gengeral_back.startAnimation(frag_in);
        //

        backup_dir.setText(settings.getBACKUPSDIR());
//setClickListener
        backup_old.setOnClickListener(this);
        sda.setOnClickListener(this);
        sdb.setOnClickListener(this);
        sdc.setOnClickListener(this);
        sdd.setOnClickListener(this);
        sde.setOnClickListener(this);
        sdf.setOnClickListener(this);
        mmcblk0.setOnClickListener(this);
        boot_back.setOnClickListener(this);
        dtbo_back.setOnClickListener(this);
        persist_back.setOnClickListener(this);
        system_back.setOnClickListener(this);
        vendor_back.setOnClickListener(this);
        userdata_back.setOnClickListener(this);
        cust_back.setOnClickListener(this);
        custom_back_go.setOnClickListener(this);
        //
        switch (settings.getSDCARDTYPE()){
            case "0":
                core_type.setText("UFS");
                emmc.startAnimation(frag_out);

                //emmc.setVisibility(View.GONE);
                break;
            case "1" :
                core_type.setText("EMMC");
                ufs.startAnimation(frag_out);
                //ufs.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sda:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping SDA to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                               setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/sda bs=512 count=1024 of="+settings.getBACKUPSDIR()+"sda_back"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.sdb:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping SDB to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/sdb bs=512 count=1024 of="+settings.getBACKUPSDIR()+"sdb_back"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.sdc:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping SDC to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/sdc bs=512 count=1024 of="+settings.getBACKUPSDIR()+"sdc_back"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.sdd:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping SDD to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/sdd bs=512 count=1024 of="+settings.getBACKUPSDIR()+"sdd_back"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.sde:

                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping SDE to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/sde bs=512 count=1024 of="+settings.getBACKUPSDIR()+"sde_back"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.sdf:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping SDF to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/sdf bs=512 count=1024 of="+settings.getBACKUPSDIR()+"sdf_back"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.mmcblk0:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping MMCBLK0 to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/mmcblk0 bs=512 count=1024 of="+settings.getBACKUPSDIR()+"mmcblk0_back"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.boot_backup:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping BOOT to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/bootdevice/by-name/boot of="+settings.getBACKUPSDIR()+"boot_back.img"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.dtbo_backup:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping Dtbo to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/bootdevice/by-name/dtbo of="+settings.getBACKUPSDIR()+"dtbo_back.img"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.persist_back:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping Persist to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/bootdevice/by-name/persist of="+settings.getBACKUPSDIR()+"persist_back.img"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.system_part:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping System to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/bootdevice/by-name/system of="+settings.getBACKUPSDIR()+"system_back.img"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.vendor_part:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping vendor to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/bootdevice/by-name/vendor of="+settings.getBACKUPSDIR()+"vendor_back.img"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.userdata_part:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping userdata to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/bootdevice/by-name/userdata of="+settings.getBACKUPSDIR()+"userdata_back.img"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.cust_part:
                processdia.showPrecessWindows(MainActivity.context, "Backuping", "Backuping cust to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/bootdevice/by-name/cust of="+settings.getBACKUPSDIR()+"cust_back.img"},true,true);
                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
                break;
            case R.id.backup_btn_custom:
                final String script_path=backup_list.getText().toString();
                Log.d("XX","ListPath ："+script_path);
                if (script_path.equals("")){
                    Log.d("XX","Null warning");
                    Snackbar.make(custom_back_go,"严禁为空　Null Erro !",Snackbar.LENGTH_LONG).show();
                }else {
                    processdia.showPrecessWindows(MainActivity.context, "Custom  Backuping", "Backup from script file ...\nPlease wait...Back up to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE, true, new Actions() {
                        @Override
                        public void action() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    File script_file = new File(script_path);
                                    Log.d("XX",script_path+">filepath");
                                    setCommand.resultCom resultCom = new setCommand.resultCom(225);
                                    if (script_file.exists()){
                                        Log.d("XX","ListFile found ");
                                        //startBackup
                                        try {
                                            //getlist
                                            FileReader fileReader=new FileReader(script_file);
                                            BufferedReader bufferedReader=new BufferedReader(fileReader);
                                            int l =0;
                                            String cache_line="";
                                            List<String> partionList=new ArrayList<String>();
                                            while ((cache_line=bufferedReader.readLine())!=null){

                                                if (cache_line!="") {
                                                    l++;
                                                    partionList.add("dd if="+cache_line+" of="+settings.getBACKUPSDIR()+l+"_atms.bak");
                                                }
                                                Log.d("XX","Partition path : "+partionList.get((l-1)));
                                            }
                                            String[] strs1 = partionList.toArray(new String[partionList.size()]);
                                            resultCom=setCommand.execCommand(strs1,true,true);

                                        }catch (Exception e) {
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
                                        processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);

                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        processdia.closeGIF(true);
                                        processdia.setPromsg("Backup  completely (完成)!请保存好你的备份清单文件！恢复要用！！！！！ Result(结果) : \n" + resultCom.ok + "||" + resultCom.error + "\nPress back to back.");

                                    }else if (!script_file.exists()){
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("XX","ListFIle not found");
                                        processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                        processdia.closeGIF(true);
                                        processdia.setPromsg("未找到文件　(File not found) ");
                                    }

                                    //finished
                                    }
                            }).start();
                        }
                    });
                }
                break;
            case R.id.backup_old:
                processdia.showPrecessWindows(MainActivity.context, "Auto Backuping", "Auto backup to" + settings.getBACKUPSDIR(), processdia.BACKPRESS_DISABLE,true, new Actions() {
                    @Override
                    public void action() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                setCommand.resultCom resultCom=new setCommand.resultCom(1);
                                resultCom.ok="Null Point Exception";
                                resultCom.error="Null Point Exception";
                                switch (settings.getSDCARDTYPE()){
                                    case "0":
                                        resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/sda bs=512 count=1024 of="+settings.getBACKUPSDIR()+"sda"+settings.getSYSORDER(),"dd if=/dev/block/sde bs=512 count=1024 of="+settings.getBACKUPSDIR()+"sde"+settings.getSYSORDER(),"dd if=/dev/block/sdf bs=512 count=1024 of="+settings.getBACKUPSDIR()+"sdf"+settings.getSYSORDER(),"dd if=/dev/block/bootdevice/by-name/boot of="+settings.getBACKUPSDIR()+"boot"+settings.getSYSORDER(),"dd if=/dev/block/bootdevice/by-name/dtbo of="+settings.getBACKUPSDIR()+"dtbo"+settings.getSYSORDER()},true,true);
                                        break;
                                    case "1":
                                        resultCom= setCommand.execCommand(new String[]{"dd if=/dev/block/mmcblk0 bs=512 count=1024 of="+settings.getBACKUPSDIR()+"sys"+settings.getSYSORDER(),"dd if=/dev/block/bootdevice/by-name/boot of="+settings.getBACKUPSDIR()+"boot"+settings.getSYSORDER(),"dd if=/dev/block/bootdevice/by-name/dtbo of="+settings.getBACKUPSDIR()+"dtbo"+settings.getSYSORDER()},true,true);
                                        break;
                                    default:
                                        break;
                                }

                                //finished
                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                processdia.closeGIF(true);
                                processdia.setPromsg("Backup  completely ! Result : \n"+resultCom.ok +"||"+ resultCom.error+"\nPress back to back.");
                            }
                        }).start();
                    }
                });
            default:
                break;
        }
    }
}
