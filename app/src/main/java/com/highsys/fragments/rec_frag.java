package com.highsys.fragments;
import com.highsys.adapters.SystemAdapter;
import com.highsys.adapters.recAdapter;
import com.highsys.atms_obj.rec_obj;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.highsys.systemchanger.MainActivity;
import com.highsys.systemchanger.R;
import com.highsys.tool.setCommand;

import java.util.ArrayList;
import java.util.List;

import static com.highsys.systemchanger.MainActivity.*;


public class rec_frag extends Fragment {
    private recAdapter adapter;
    private List<rec_obj> reclist=new ArrayList<>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recovery_list,container,false);
        testsys();
        adapter=new recAdapter(reclist);
        RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.rec_recy);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public void reloadsys() {

    }
    //Debug
    public void testsys(){
        for (int i =0;i<15;i++){
            rec_obj s =new rec_obj("Redmi_note_7_pro","1.0.0","jfaoihjgdilshgoiasdddddddddddddddddh","http://175.24.94.146/fa.rec");
            reclist.add(s);
        }
    }

}
