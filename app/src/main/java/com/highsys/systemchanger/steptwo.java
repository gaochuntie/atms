package com.highsys.systemchanger;

import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class steptwo extends Fragment {
    public List<Devices> Device_List=new ArrayList<>();
    public static RecyclerView dev_recyc;
    public static DevicesAdapter ada;
    public static TextView chosenname;
    public static String TAG="steptwo";
    public SwipeRefreshLayout refreshLayout;
    Handler h =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                ada.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.steptwo, container, false);
        view.startAnimation(MainActivity.translateAnimation2);
        install_mobile.action_box.startAnimation(MainActivity.translateAnimation2);
        install_mobile.NEXTCODE=2;
        chosenname=view.findViewById(R.id.chosenname);
        dev_recyc=(RecyclerView) view.findViewById(R.id.devices_recycler);
        refreshLayout=view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                inittest();
            }
        });
        inittest();
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext());
        dev_recyc.setLayoutManager(layoutManager);
        DevicesAdapter adapter=new DevicesAdapter(Device_List);
        ada=adapter;
        dev_recyc.setAdapter(adapter);
        return view;
    }
    public void inittest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Device_List.clear();
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("http://175.24.94.146/highsysrec/reclist.txt")
                            .build();
                    Response response=client.newCall(request).execute();
                    StringBuffer first= new StringBuffer(response.body().string());
                    Log.d(TAG,first.toString());
                    String[] second=first.toString().split("\n");
                    Log.d(TAG,second[0]);
                    Log.d(TAG,second[1]);
                    for (int i =0;i<second.length;i++){
                        String [] temp=second[i].split("#");
                        Devices tem=new Devices(temp[1]);
                        tem.id=Integer.valueOf(temp[0]);
                        tem.address=temp[2];
                        Device_List.add(tem);

                    }
                   h.sendEmptyMessage(1);

                    //Snackbar.make(dev_recyc,first.toString(),Snackbar.LENGTH_SHORT).show();

                }catch (Exception e ){
                    Log.d(TAG,e.getMessage());
                    Snackbar.make(dev_recyc,e.getMessage(),Snackbar.LENGTH_LONG).show();
                }

            }
        }).start();


       // for(int i=0;i<61;i++){
       //     Devices d = new Devices("XIAOMI REDMI NOTE 7 PRO");
       //     Device_List.add(d);
       // }
    }
}