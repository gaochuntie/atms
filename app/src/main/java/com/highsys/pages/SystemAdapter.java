package com.highsys.pages;

import android.transition.Transition;
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
                Log.d(TAG,d.getPartition_tab());

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
}
