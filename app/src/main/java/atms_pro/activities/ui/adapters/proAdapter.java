package atms_pro.activities.ui.adapters;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.highsys.atms_obj.settings;
import com.highsys.pages.processdia;
import com.highsys.systemchanger.DownloadListener;
import com.highsys.systemchanger.DownloadService;
import com.highsys.systemchanger.MainActivity;
import com.highsys.systemchanger.MyApplication;
import com.highsys.systemchanger.R;
import com.highsys.tool.Actions;
import com.highsys.tool.setCommand;
import com.highsys.tool.tools;

import java.util.List;

import atms_pro.activities.ui.atmsProMain;
import atms_pro.activities.ui.objs.pro_obj;

public class proAdapter extends RecyclerView.Adapter<proAdapter.ViewHolder> {
    Animation animation=AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.fragment_enter);
    public  String TAG="proAdapter";
    private List<pro_obj> rec_list;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pro_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.rec_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_obj d=rec_list.get(holder.getAdapterPosition());
                Log.d(TAG,"recat successfully");

            }
        });
        holder.rec_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"OPEN DETAILS WINDOWS");
                pro_obj d=rec_list.get(holder.getAdapterPosition());
                atmsProMain.atmsProMain_instance.showItemDetails(d);
                //
            }
        });
        return holder;
    }
    public void addData(int position,pro_obj d) {
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
        pro_obj d = rec_list.get(position);
        holder.rec_title.setText(d.getRectitle());
        holder.rec_version.setText(d.getRecversion());
        holder.rec_details.setText(d.getRecdetails());
        holder.require_level.setText(d.getRequire_level());
        holder.sdcard_cutway.setText(d.getSdcard_cutway());

        if (atmsProMain.viplevel>=Integer.valueOf(d.getRequire_level())){
            holder.warnings.setTextColor(Color.GREEN);
            holder.warnings.setText("您有权使用此条内容");

        }else{
            holder.warnings.setTextColor(Color.RED);
            holder.warnings.setText("您无权使用此条内容");

        }


        //holder.rec_at.startAnimation(animation);
        ObjectAnimator.ofFloat(holder.rec_at,"translationX",-200,0).setDuration(500).start();
    }

    @Override
    public int getItemCount() {
        return rec_list.size();
    }
    public proAdapter(List<pro_obj> reclist){
        rec_list=reclist;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rec_title;
        TextView rec_version;
        TextView rec_details;
        TextView require_level;
        TextView sdcard_cutway;
        TextView warnings;
        ImageButton rec_install;
        LinearLayout rec_at;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sdcard_cutway=itemView.findViewById(R.id.sdcarddetails);
            warnings=itemView.findViewById(R.id.atms_warnings);
            require_level=itemView.findViewById(R.id.atms_viplevel);
            rec_title=(TextView)itemView.findViewById(R.id.rec_title);
            rec_version=(TextView)itemView.findViewById(R.id.rec_version);
            rec_details=(TextView)itemView.findViewById(R.id.rec_details);
            rec_install=(ImageButton) itemView.findViewById(R.id.install_rec);
            rec_at=(LinearLayout) itemView.findViewById(R.id.rec_content);
        }
    }
    public  void install_rec(pro_obj rec){
        final pro_obj s=rec;
        Log.d(TAG,"install_rec");
    }
}
