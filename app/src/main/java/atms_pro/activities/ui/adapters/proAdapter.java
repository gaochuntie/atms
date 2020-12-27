package com.highsys.adapters;

import com.highsys.atms_obj.settings;
import com.highsys.atms_obj.rec_obj;
import com.highsys.fragments.rec_frag;
import com.highsys.pages.processdia;

import android.animation.ObjectAnimator;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.highsys.systemchanger.DownloadListener;
import com.highsys.systemchanger.DownloadService;
import com.highsys.systemchanger.MainActivity;
import com.highsys.systemchanger.MyApplication;
import com.highsys.systemchanger.R;
import com.highsys.tool.Actions;
import com.highsys.tool.setCommand;
import com.highsys.tool.tools;

import java.util.List;

public class recAdapter extends RecyclerView.Adapter<recAdapter.ViewHolder> {
    Animation animation=AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.fragment_enter);
    public  String TAG="recAdapter";
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
                rec_frag.selected_rec=d;
                Log.d(TAG,"recat successfully");
                processdia.showPrecessWindows(view.getContext(), d.getRectitle(), d.getRecdetails(), processdia.BACKPRESS_ENABLE, new Actions() {
                    @Override
                    public void action() {
                        rec_frag.downloadBinder.cancelDownload();
                        processdia.setProcess(processdia.PROCESS_FINISH);
                    }
                }, new Actions() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void action() {
                       processdia.setProcess(processdia.PROCESS_FINISH);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        processdia.showPrecessWindows(view.getContext(), "Installing", "0%", processdia.BACKPRESS_DISABLE, true, new Actions() {
                           @Override
                           public void action() {
                               //install rec
                               DownloadService.setDownloadListener(new DownloadListener() {
                                   @Override
                                   public void onProgress(int progress) {
                                       processdia.setPromsg(progress+"%");
                                       Log.d(TAG,String.valueOf(progress));
                                   }

                                   @Override
                                   public void onSuccess() {
                                       processdia.setPromsg("正在准备...");
                                       new Thread(new Runnable() {
                                           @Override
                                           public void run() {
                                               setCommand.execCommand(new String[]{"mkdir /data/atms_cache","mount /dev/block/bootdevice/by-name/cache /data/atms_cache","echo  --updata_package=SDCARD:"+ tools.StringStartTrim(settings.getTempdir()+rec_frag.selected_rec.getRec_remote_local().substring(rec_frag.selected_rec.getRec_remote_local().lastIndexOf("/")+1),"/")+" >>/data/atms_cache/recovery/command"},true,false);
                                               //setCommand.execCommand(new String[]{"dd if="+settings.getTempdir()+rec_frag.selected_rec.getRec_remote_local().substring(rec_frag.selected_rec.getRec_remote_local().lastIndexOf("/")+1)+" of=/dev/block/bootdevice/by-name/recovery"},true,true);
                                               //Log.d(TAG,"dd if="+settings.getTempdir()+rec_frag.selected_rec.getRec_remote_local().substring(rec_frag.selected_rec.getRec_remote_local().lastIndexOf("/")+1)+" of=/dev/block/bootdevice/by-name/recovery");
                                               rec_frag.downloadBinder.cancelDownload();
                                               processdia.setPromsg("安装将在5s后开始，你的设备将会重启" + "\n请不要关闭本软件...");
                                               processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                               try {
                                                   Thread.sleep(5000);
                                               } catch (InterruptedException e) {
                                                   e.printStackTrace();
                                               }
                                               //重启进recovery
                                               setCommand.execCommand(new String[]{"reboot recovery"},true,false);
                                           }
                                       }).start();
                                       Log.d(TAG,"Download successfully");
                                   }

                                   @Override
                                   public void onFaild() {
                                       new Thread(new Runnable() {
                                           @Override
                                           public void run() {
                                               try {
                                                   Thread.sleep(600);
                                               } catch (InterruptedException e) {
                                                   e.printStackTrace();
                                               }
                                               processdia.setPromsg("下载失败");
                                               processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                               processdia.closeGIF(true);

                                           }
                                       }).start();

                                       Log.d(TAG,"Download faild");
                                   }

                                   @Override
                                   public void onPaused() {

                                       Log.d(TAG,"Download paused");
                                   }

                                   @Override
                                   public void onCanceled() {

                                       Log.d(TAG,"Download canceled");
                                   }
                               });
                               rec_frag.downloadBinder.startDownload(rec_frag.selected_rec.getRec_remote_local());
                           }
                       });
                    }
                },"返回","刷入");

            }
        });
        holder.rec_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Install recovery");
                rec_obj d=rec_list.get(holder.getAdapterPosition());
                rec_frag.selected_rec=d;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        processdia.setProcess(processdia.PROCESS_FINISH);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        processdia.showPrecessWindows(MainActivity.context, "Installing", "0%", processdia.BACKPRESS_DISABLE, true, new Actions() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void action() {
                                processdia.setiS_BUTTON_PRESSED_ENABLE(true,false);
                                //install rec
                                DownloadService.setDownloadListener(new DownloadListener() {
                                    @Override
                                    public void onProgress(int progress) {
                                        processdia.setPromsg(progress + "%");
                                        Log.d(TAG, String.valueOf(progress));
                                    }

                                    @Override
                                    public void onSuccess() {
                                        processdia.setPromsg("正在刷入...");
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                setCommand.execCommand(new String[]{"mkdir /data/atms_cache","mount /dev/block/bootdevice/by-name/cache /data/atms_cache","echo  --updata_package=SDCARD:"+ tools.StringStartTrim(settings.getTempdir()+rec_frag.selected_rec.getRec_remote_local().substring(rec_frag.selected_rec.getRec_remote_local().lastIndexOf("/")+1),"/")+" >>/data/atms_cache/recovery/command"},true,false);
                                                //setCommand.execCommand(new String[]{"dd if="+settings.getTempdir()+rec_frag.selected_rec.getRec_remote_local().substring(rec_frag.selected_rec.getRec_remote_local().lastIndexOf("/")+1)+" of=/dev/block/bootdevice/by-name/recovery"},true,true);
                                                //Log.d(TAG,"dd if="+settings.getTempdir()+rec_frag.selected_rec.getRec_remote_local().substring(rec_frag.selected_rec.getRec_remote_local().lastIndexOf("/")+1)+" of=/dev/block/bootdevice/by-name/recovery");
                                                rec_frag.downloadBinder.cancelDownload();
                                                processdia.setPromsg("环境配置完成,安装将在重启recovery后开始.你还有机会备份数据．若备份完成,并且确保您到设备没有AVB2.0校验或其已经被关闭后,点击开始");processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                                processdia.closeGIF(true);
                                            }
                                        }).start();
                                        Log.d(TAG, "Download successfully");
                                    }

                                    @Override
                                    public void onFaild() {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(600);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                processdia.setPromsg("下载失败");
                                                processdia.setIsBACKPRESS(processdia.BACKPRESS_ENABLE);
                                                processdia.closeGIF(true);

                                            }
                                        }).start();

                                        Log.d(TAG, "Download faild");
                                    }

                                    @Override
                                    public void onPaused() {

                                        Log.d(TAG, "Download paused");
                                    }

                                    @Override
                                    public void onCanceled() {

                                        Log.d(TAG, "Download canceled");
                                    }
                                });
                                rec_frag.downloadBinder.startDownload(rec_frag.selected_rec.getRec_remote_local());
                                processdia.setiS_BUTTON_PRESSED_ENABLE(true,true);
                            }
                        }, new Actions() {
                            @Override
                            public void action() {
                                setCommand.execCommand(new String[]{"reboot recovery"},true,false);
                            }
                        },"开始");
                    }
                }).start();
                //
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
        //holder.rec_at.startAnimation(animation);
        ObjectAnimator.ofFloat(holder.rec_at,"translationX",-200,0).setDuration(500).start();
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
