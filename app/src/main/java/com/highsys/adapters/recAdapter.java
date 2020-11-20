package com.highsys.adapters;

import com.highsys.atms_obj.settings;
import com.highsys.atms_obj.rec_obj;
import com.highsys.pages.processdia;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.highsys.systemchanger.MainActivity;
import com.highsys.systemchanger.MyApplication;
import com.highsys.systemchanger.R;
import com.highsys.tool.setCommand;

import java.util.List;

public class recAdapter extends RecyclerView.Adapter<recAdapter.ViewHolder> {
    Animation animation=AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.fragment_enter);
    public static String TAG="recAdapter";
    private List<rec_obj> rec_list;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.rec_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rec_obj d=rec_list.get(holder.getAdapterPosition());
                Log.d(TAG,"recat successfully");
                Toast.makeText(view.getContext(),"Rec parent",Toast.LENGTH_SHORT).show();

            }
        });
        holder.rec_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Install recovery");
            }
        });
        return holder;
    }
    public void addData(int position,rec_obj d) {
//      在list中添加数据，并通知条目加入一条
        rec_list.add(position,d);
        //添加动画
        notifyItemInserted(position);
    }
    //  删除数据
    public void removeData(int position) {
        rec_list.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        rec_obj d = rec_list.get(position);
        holder.rec_title.setText(d.getRectitle());
        holder.rec_version.setText(d.getRecversion());
        holder.rec_details.setText(d.getRecdetails());
        holder.rec_at.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return rec_list.size();
    }
    public  recAdapter (List<rec_obj> reclist){
        rec_list=reclist;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rec_title;
        TextView rec_version;
        TextView rec_details;
        ImageButton rec_install;
        LinearLayout rec_at;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rec_title=(TextView)itemView.findViewById(R.id.rec_title);
            rec_version=(TextView)itemView.findViewById(R.id.rec_version);
            rec_details=(TextView)itemView.findViewById(R.id.rec_details);
            rec_install=(ImageButton) itemView.findViewById(R.id.install_rec);
            rec_at=(LinearLayout) itemView.findViewById(R.id.rec_content);
        }
    }
    public  void install_rec(rec_obj rec){
        final rec_obj s=rec;
        Log.d(TAG,"install_rec");
    }
}
