package com.highsys.systemchanger;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class installsystems extends AppCompatActivity {
    setCommand.resultCom r=null;
    TextView results;
    TextView anous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_installsystems);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();

        }
        anous=findViewById(R.id.anous);
        Animation translateAnimation = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.fragment_enter_pop);
        //执行动画
        anous.startAnimation(translateAnimation);
        //执行动画
        anous.startAnimation(translateAnimation);
    }
}
