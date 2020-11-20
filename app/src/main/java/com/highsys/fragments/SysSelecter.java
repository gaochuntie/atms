package com.highsys.pages;
import com.highsys.adapters.SystemAdapter;
import com.highsys.atms_obj.settings;
import com.highsys.atms_obj.sysobj;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.highsys.systemchanger.R;
import com.highsys.tool.setCommand;

import java.util.ArrayList;
import java.util.List;


public class SysSelecter extends Fragment {
    private TextView nosysfound;
    private SystemAdapter adapter;
    private List<sysobj> sysobjList=new ArrayList<>();
    private SwipeRefreshLayout reloadsys;
    private  Handler h =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what==0){
                adapter.notifyDataSetChanged();

                reloadsys.setRefreshing(false);
            }
            if (msg.what==-1){
                nosysfound.setVisibility(View.VISIBLE);
            }
            super.handleMessage(msg);

        }
    };

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.sys_selecter,container,false);
        reloadsys();
        RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.sys_recy);
        nosysfound=view.findViewById(R.id.nosystemfound);
        nosysfound.setVisibility(View.INVISIBLE);
        reloadsys=(SwipeRefreshLayout) view.findViewById(R.id.reloadsys);
        reloadsys.setColorSchemeColors(R.color.design_default_color_primary_dark);
        reloadsys.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                reloadsys();

            }
        });
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new SystemAdapter(sysobjList);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public void reloadsys(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                setCommand.resultCom resultCom=null;
                if (settings.getSDCARDTYPE().equals("0")){
                    resultCom=setCommand.execCommand(new String[]{"cd "+settings.getSYSTEMFILE(),"ls -m sda*"},true,true);
                }else if (settings.getSDCARDTYPE().equals("1")){
                   resultCom=setCommand.execCommand(new String[]{"cd "+settings.getSYSTEMFILE(),"ls -m sys*"},true,true);
                }
                String s=new String(resultCom.ok);
                Log.d("Syslecter", resultCom.ok);
                s=s.replaceAll("[^0-9a-zA-Z]","");
                Log.d("Syslecter", s);
                String[] sl=s.split("sys");
                sysobjList.clear();
                for (int i =1;i<sl.length;i++){
                    if (settings.getSDCARDTYPE().equals("0")){
                        //ufs
                        sysobjList.add(new sysobj(sl[(i)],settings.getSYSTEMFILE()+"dtbo"+sl[i],settings.getSYSTEMFILE()+"boot"+sl[i],settings.getSYSTEMFILE()+"sda"+sl[i],settings.getSYSTEMFILE()+"sde"+sl[i] ,settings.getSYSTEMFILE()+"sdf"+sl[i]));
                    } else if (settings.getSDCARDTYPE().equals("1")){
                        //emmc
                        sysobjList.add(new sysobj(sl[(i)],settings.getSYSTEMFILE()+"dtbo"+sl[i],settings.getSYSTEMFILE()+"boot"+sl[i],settings.getSYSTEMFILE()+"sys"+sl[i]) );

                    }

                    Log.d("Syslecter",String.valueOf(i));
                }
                if (sysobjList.size()==0) {
                    h.sendEmptyMessage(-1);
                }

                h.sendEmptyMessage(0);


            }
        }).start();
    }
    //Debug
    public void testsys(){
        for (int i =0;i<5;i++){
            sysobj s =new sysobj("a","f","f","f");
            sysobjList.add(s);
        }
    }

}
