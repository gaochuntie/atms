package com.highsys.systemchanger;

import android.app.Application;
import android.content.Context;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.MaterialStyle;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        DialogX.init(this);
        //设置主题样式
        DialogX.globalStyle = MaterialStyle.style();

//设置亮色/暗色（在启动下一个对话框时生效）
        DialogX.globalTheme = DialogX.THEME.LIGHT;
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
