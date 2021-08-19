package com.highsys.adapters;

import com.highsys.atms_obj.settings;
import com.highsys.atms_obj.sysobj;
import com.highsys.pages.processdia;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.highsys.systemchanger.MainActivity;
import com.highsys.systemchanger.MyApplication;
import com.highsys.systemchanger.R;
import com.highsys.tool.setCommand;

import java.util.List;

public class SystemAdapter extends RecyclerView.Adapter<SystemAdapter.ViewHolder> {
    Animation animation=AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.botttom_in);
    public static String TAG="SystemAdapter";
    private List<sysobj> sysobj_List;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sys_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.sys_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sysobj d=sysobj_List.get(holder.getAdapterPosition());
                Log.d(TAG,"Choose successfully");
                holder.sys_at.startAnimation(animation);
                Snackbar.make(v,"您选择了： "+d.getID(),Snackbar.LENGTH_SHORT).show();
                //切换
                changesys(d);
                //
                processdia.showPrecessWindows(MainActivity.context,"Selecter","正在切换到系统"+d.getID(),0,true);
                //Log.d(TAG,d.getPartition_tab_mmcblk0());

            }
        });
        return holder;
    }
    public void addData(int position,sysobj d) {
//      在list中添加数据，并通知条目加入一条
        sysobj_List.add(position,d);
        //添加动画
        notifyItemInserted(position);
    }
    //  删除数据
    public void removeData(int position) {
        sysobj_List.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        sysobj d = sysobj_List.get(position);
        holder.sysname.setText("系统"+d.getID());
        holder.sys_at.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return sysobj_List.size();
    }
    public  SystemAdapter (List<sysobj> syslist){
        sysobj_List=syslist;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView sysname;
        LinearLayout sys_at;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sysname=(TextView)itemView.findViewById(R.id.sysname);
            sys_at=(LinearLayout)itemView.findViewById(R.id.sys_at_view);
        }
    }
    public  void changesys(sysobj sys){
        final sysobj s=sys;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("change",String.valueOf(s.getCORE_TYPE()));
                if (settings.getSDCARDTYPE().equals("0")){
                    //ufs
                    Log.d("change",s.getBoot_path());
                    Log.d("change",s.getDtbo_path());
                    Log.d("change",s.getID());
                    Log.d("change",s.getPartition_tab_sda());
                    Log.d("change",s.getPartition_tab_sdb());
                    Log.d("change",s.getPartition_tab_sdc());
                    Log.d("change",s.getPartition_tab_sdd());
                    Log.d("change",s.getPartition_tab_sde());
                    Log.d("change",s.getPartition_tab_sdf());
                    setCommand.execCommand(new String[]{"dd if="+s.getPartition_tab_sda()+" bs=512 count=1024 of=/dev/block/sda","dd if="+s.getPartition_tab_sde()+" bs=512 count=1024 of=/dev/block/sde","dd if="+s.getPartition_tab_sdf()+" bs=512 count=1024 of=/dev/block/sdf","dd if="+s.getPartition_tab_sdb()+" bs=512 count=1024 of=/dev/block/sdb","dd if="+s.getPartition_tab_sdc()+" bs=512 count=1024 of=/dev/block/sdc","dd if="+s.getPartition_tab_sdd()+" bs=512 count=1024 of=/dev/block/sdd","dd if="+s.getBoot_path()+" of=/dev/block/bootdevice/by-name/boot","dd if="+s.getDtbo_path()+" of=/dev/block/bootdevice/by-name/dtbo","reboot"},true,false);
                }
                if (settings.getSDCARDTYPE().equals("1")){
                    //emmc
                    Log.d("change",s.getBoot_path());
                    Log.d("change",s.getDtbo_path());
                    Log.d("change",s.getID());
                    Log.d("change",s.getPartition_tab_mmcblk0());
                    setCommand.execCommand(new String[]{"dd if="+s.getPartition_tab_mmcblk0()+" bs=512 count=1024 of=/dev/block/mmcblk0","dd if="+s.getBoot_path()+" of=/dev/block/bootdevice/by-name/boot","dd if="+s.getDtbo_path()+" of=/dev/block/bootdevice/by-name/dtbo","reboot"},true,false);

                }
            }
        }).start();

    }
}
