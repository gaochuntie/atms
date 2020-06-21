package com.highsys.systemchanger;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class steptwo extends Fragment {
    public List<Devices> Device_List=new ArrayList<>();
    public static RecyclerView dev_recyc;
    public static DevicesAdapter ada;
    public static TextView chosenname;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.steptwo, container, false);
        view.startAnimation(MainActivity.translateAnimation2);
        install_mobile.action_box.startAnimation(MainActivity.translateAnimation2);
        install_mobile.NEXTCODE=2;
        chosenname=view.findViewById(R.id.chosenname);
        dev_recyc=(RecyclerView) view.findViewById(R.id.devices_recycler);
        inittest();
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext());
        dev_recyc.setLayoutManager(layoutManager);
        DevicesAdapter adapter=new DevicesAdapter(Device_List);
        ada=adapter;
        dev_recyc.setAdapter(adapter);
        return view;
    }
    public void inittest(){
        for(int i=0;i<61;i++){
            Devices d = new Devices("XIAOMI REDMI NOTE 7 PRO");
            Device_List.add(d);
        }
    }
}