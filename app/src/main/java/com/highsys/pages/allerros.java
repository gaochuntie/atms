package com.highsys.pages;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.highsys.systemchanger.R;

import org.apache.tools.ant.types.resources.comparators.Content;

public class allerros extends AppCompatActivity {
    public static String erromsg = null;
    public static int backpressed = 0;
    public static int BACKPRESSENABLE = 1;
    public static int BACKPRESSDISABLE =0;
    TextView tv;
    public int indexc=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_allerros);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        tv = findViewById(R.id.erromsg);
        tv.setText(allerros.erromsg);
    }

    public void onBackPressed() {
        if (allerros.backpressed == 0) {
            if (indexc == 0) {
                Toast.makeText(allerros.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
            if (indexc == 1) {
                System.exit(0);
            }
            indexc++;
        }
        if (allerros.backpressed == 1) {
            finish();
        }
    }
    public static void showErroMsg(Context context,String emsg,int isCancelAble){
        erromsg=erromsg+"\n  >>"+emsg;
        backpressed=isCancelAble;
        context.startActivity(new Intent(context,allerros.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backpressed=0;
    }
}
