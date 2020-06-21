package com.highsys.systemchanger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {
    private List<Devices> Devices_List;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item,parent,false);
     final ViewHolder holder=new ViewHolder(view);
      holder.devicename.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Devices d=Devices_List.get(holder.getAdapterPosition());
              install_mobile.DEVICEID=d.getId();
              install_mobile.selecteddevice=d;
              steptwo.chosenname.setText("选择了 ： "+d.getName());
              Snackbar.make(v,"您选择了： "+d.getName(),Snackbar.LENGTH_SHORT).show();

          }
      });
      return holder;
    }
    public void addData(int position,Devices d) {
//      在list中添加数据，并通知条目加入一条
        Devices_List.add(position,d);
        //添加动画
        notifyItemInserted(position);
    }
    //  删除数据
    public void removeData(int position) {
        Devices_List.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Devices d = Devices_List.get(position);
        holder.devicename.setText(d.name);
        holder.devicename.startAnimation(MainActivity.translateAnimation);
    }

    @Override
    public int getItemCount() {
        return Devices_List.size();
    }
    public DevicesAdapter (List<Devices> devicelist){
        Devices_List=devicelist;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView devicename;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            devicename=(TextView)itemView.findViewById(R.id.devicename);
        }
    }
}
