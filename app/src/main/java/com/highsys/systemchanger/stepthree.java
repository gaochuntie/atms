package com.highsys.systemchanger;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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

import pl.droidsonroids.gif.GifImageView;

public class stepthree extends Fragment {
    public static TextView process;
    public List<Devices> Device_List=new ArrayList<>();
    public static RecyclerView dev_recyc;
    public static DevicesAdapter ada;
    public static TextView chosenname;
    public static View snackview;
    public static GifImageView process_pic;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stepthree, container, false);
        process=view.findViewById(R.id.process_percent);
        process_pic=view.findViewById(R.id.process_pic);
        view.startAnimation(MainActivity.translateAnimation2);
        install_mobile.action_box.startAnimation(MainActivity.translateAnimation2);
        install_mobile.NEXTCODE=3;
        install_mobile.next.setVisibility(View.GONE);
        snackview=container;

        return view;
    }
}
