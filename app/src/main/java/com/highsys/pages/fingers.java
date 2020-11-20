package com.highsys.systemchanger;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.highsys.atms_obj.settings;


public class fingers extends AppCompatActivity {
    public static int fingerresults=-1;
    int counter=0;
    setCommand.resultCom r;
    int fingertimes=4;
    public static int fingermode = -1;
    TextView fingeruser;
    TextView fingerresult;
    FingerprintManagerCompat manager = FingerprintManagerCompat.from(this);
    Handler h =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==-1){
                fingertimes--;
                if (fingertimes!=0){
                    fingerresult.setText("指纹错误您还有"+fingertimes+"次机会");
                }
                if (fingertimes==0){
                    fingerresult.setText("错误次数过多，请稍后重试");
                }

            }
            if (msg.what==0){
                fingerresults=0;
                if (fingermode==0){
                    setCommand.resultCom r=setCommand.execCommand(new String[]{"[ -e /data/highsyslock ] && echo 0 || echo 1"},true,true);
                    if (r.ok.equals("0")){
                        fingerdelet();
                        Toast.makeText(fingers.this,"关闭指纹成功！",Toast.LENGTH_SHORT).show();
                        // Intent i =new Intent(fingers.this,setvalues.class);
                        // startActivity(i);
                        finish();
                    }
                    if (r.ok.equals("1")){
                        fingercreat();
                        Toast.makeText(fingers.this,"录取成功！",Toast.LENGTH_LONG).show();
                        //Intent i =new Intent(fingers.this,setvalues.class);
                        //startActivity(i);
                        finish();
                }
                }
                if (fingermode==1){
                    Toast.makeText(fingers.this,"验证成功",Toast.LENGTH_SHORT).show();
                    fingers.super.onBackPressed();
                }


            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_fingers);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        fingertimes=4;
        fingerresult = findViewById(R.id.fingersresult);
        fingeruser = findViewById(R.id.fingeruser);
        fingeruser.setText("验证 " + settings.allsettings[6] + " 的身份");
        //
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.USE_FINGERPRINT};
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 0);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            }
        }

        //
        checkFingerPrint();
       // manager = FingerprintManagerCompat.from(this);
       // manager.authenticate(null, 0, null, new MyCallBack(), null);
        }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){//
            case 0://如果申请权限回调的参数
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"申请成功",Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder nosdcard = new AlertDialog.Builder(fingers.this);

                    Toast.makeText(this,"拒绝权限",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
    public void meterro(String err,int backpres) {
        settings.processcache = 1;
        allerros.backpressed = backpres;
        allerros.erromsg = err;
        Intent ii = new Intent(fingers.this, allerros.class);
        startActivity(ii);
    }

        public void fingercreat(){
        try {
             r=setCommand.execCommand(new String[]{"[ -e /data/highsyslock ] && echo 0 || echo 1"},true,true);
            String resultexec=r.ok;
            r=setCommand.execCommand(new String[]{"touch /data/highsyslock"},true,true);
        }catch (Exception e ){
            meterro(e.getMessage(),0);
        }
    }
    public void fingerdelet(){
        try {
            r=setCommand.execCommand(new String[]{"rm -f /data/highsyslock"},true,true);
        }catch (Exception e ){
            meterro(e.getMessage(),0);
        }
    }
    private void checkFingerPrint(){
        FingerprintManagerCompat.from(this).authenticate(null,0,null,new MyCallBack(),null);
    }
    public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback{
        private static final String TAG = "MyCallBack";
        @Override
        public void onAuthenticationFailed(){
            Log.d(TAG,"指纹错误");
            h.sendEmptyMessage(-1);
        }
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result){
            Log.d(TAG,"指纹正确");
            h.sendEmptyMessage(0);
        }
    }

    @Override
    public void onBackPressed() {
        if (fingers.fingermode==0){
            super.onBackPressed();
        }
        if (fingermode==1){
            if (counter==0){
                Toast.makeText(fingers.this,"再按一次退出",Toast.LENGTH_SHORT).show();
            }
            if (counter==1){
                System.exit(0);
            }
            counter++;
        }

    }
}



