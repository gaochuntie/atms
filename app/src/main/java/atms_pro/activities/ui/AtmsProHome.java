package atms_pro.activities.ui;

import com.google.android.material.snackbar.Snackbar;
import com.highsys.atms_obj.rec_obj;
import com.highsys.pages.allerros;
import com.highsys.pages.processdia;
import com.highsys.systemchanger.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AtmsProHome extends AppCompatActivity implements TextWatcher,View.OnClickListener  {
private String userid;
private int level;
    private static String PASSWORD="UNKNOW";
    private static String USERNAME="NAMELESS";
    static String TAG ="ATMSPROHOME";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://121.5.103.76:3306/atms_pro";
    private static final String USER = "pro_downloader";
    private static  final String axx="mPsbpLGefy";
    private static  final String title="5ZNNfp";

    static final String PASS = axx+title;

    private EditText user;
    private EditText password;
    private Button loginBtn;
    private LinearLayout leftHand;
    private LinearLayout rightHand;
    private CheckBox atms_licence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmspro_home);

        initViews();
    }

    public void initViews(){
        atms_licence=findViewById(R.id.atms_license);
        user = findViewById(R.id.et_user);
        password = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.bt_login);
        leftHand = findViewById(R.id.iv_left);
        rightHand = findViewById(R.id.iv_right);

        loginBtn.setOnClickListener(this);
        //监听内容改变 -> 控制按钮的点击状态
        user.addTextChangedListener(this);
        password.addTextChangedListener(this);

        //监听EidtText的焦点变化 -> 控制是否需要捂住眼睛
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true){
                    //捂住眼睛
                    close();
                }else{
                    //打开
                    open();
                }
            }
        });

    }

    /**
     * 当有控件获得焦点focus 自动弹出键盘
     * 1. 点击软键盘的enter键 自动收回键盘
     * 2. 代码控制 InputMethodManager
     *    requestFocus
     *    showSoftInput:显示键盘 必须先让这个view成为焦点requestFocus
     *
     *    hideSoftInputFromWindow 隐藏键盘
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            //隐藏键盘
            //1.获取系统输入的管理器
            InputMethodManager inputManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            //2.隐藏键盘
            inputManager.hideSoftInputFromWindow(user.getWindowToken(),0);

            //3.取消焦点
            View focusView = getCurrentFocus();
            if (focusView != null) {
                focusView.clearFocus(); //取消焦点
            }

            //getCurrentFocus().clearFocus();

            //focusView.requestFocus();//请求焦点
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //判断两个输入框是否有内容
        if (user.getText().toString().length() > 0 &&
                password.getText().toString().length() > 0){
            //按钮可以点击
            loginBtn.setEnabled(true);
        }else{
            //按钮不能点击
            loginBtn.setEnabled(false);
        }
    }

    public void close(){
        //左边
        RotateAnimation rAnim = new RotateAnimation(0, -250,0f,0f);
        rAnim.setDuration(500);
        rAnim.setFillAfter(true);


        TranslateAnimation down = (TranslateAnimation) AnimationUtils.loadAnimation(this,R.anim.hand_down_anim);
        leftHand.startAnimation(down);
        rightHand.startAnimation(down);
    }

    public void open(){
        //左边

        RotateAnimation rAnim = new RotateAnimation(-250,0,0f,0f);
        rAnim.setDuration(500);
        rAnim.setFillAfter(true);


        TranslateAnimation up = (TranslateAnimation) AnimationUtils.loadAnimation(this,R.anim.hand_up_anim);
        leftHand.startAnimation(up);
        rightHand.startAnimation(up);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_login:

                if (atms_licence.isChecked()){
                    processdia.showPrecessWindows(this,"正在登录...","正在检查用户存在性及合法性",processdia.BACKPRESS_DISABLE,true);
                    checkUserRemote(user.getText().toString(),password.getText().toString());
                }else{
                    Snackbar.make(loginBtn,"请同意使用条例...",Snackbar.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    private void checkUserRemote(final String username, String pass){

        PASSWORD=pass;
        USERNAME=username;
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                Statement stmt = null;
                // 注册 JDBC 驱动
                try{
                    Class.forName(JDBC_DRIVER);

                    // 打开链接
                    Log.d("MySql","连接数据库...");
                    conn = (Connection) DriverManager.getConnection(DB_URL,USER,axx+title);

                    // 执行查询
                    Log.d("MySql"," 实例化Statement对象...");
                    stmt = (Statement) conn.createStatement();
                    String sql;
                    sql = "Select * from atms_pro.viplist where username='" + USERNAME + "' and password='" + PASSWORD + "'";
                    ResultSet rs = stmt.executeQuery(sql);

                    // 展开结果集数据库
                    if (rs.next()) {
                        //in this case enter when at least one result comes it means user is valid
                        //bock??
                        userid=rs.getString("userid");
                        level=rs.getInt("viplevel");
                        String isblock="Select * from atms_pro.viplist where username='" + USERNAME + "' and isblocked='0'";
                        ResultSet rs1 = stmt.executeQuery(isblock);
                        if (rs1.next()){
                            //this account is available
                            runOnUiThread(new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    processdia.setProcess(processdia.PROCESS_FINISH);
                                    startActivity(new Intent(AtmsProHome.this,atmsProMain.class)
                                    .putExtra("username",username)
                                    .putExtra("userid",userid)
                                                    .putExtra("viplevel",level)
                                    );
                                    finish();
                                }
                            }));

                        }else{
                            //this account is blocked
                            runOnUiThread(new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    processdia.setProcess(processdia.PROCESS_FINISH);
                                    Snackbar.make(loginBtn,"此帐号涉嫌违规使用已被屏蔽",Snackbar.LENGTH_SHORT).show();
                                }
                            }));
                        }

                    } else {
                        //in this case enter when  result size is zero  it means user is invalid
                        runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                processdia.setProcess(processdia.PROCESS_FINISH);
                                Snackbar.make(loginBtn,"用户名或密码无效",Snackbar.LENGTH_SHORT).show();
                            }
                        }));

                    }
                    // 完成后关闭
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    allerros.showErroMsg(getApplicationContext(),ex.getMessage(),allerros.BACKPRESSENABLE);
                    ex.printStackTrace();

                } catch(Exception e){
                    allerros.showErroMsg(getApplicationContext(),e.getMessage(),allerros.BACKPRESSENABLE);
                    // 处理 Class.forName 错误
                    e.printStackTrace();
                }finally{
                    // 关闭资源
                    try{
                        if(stmt!=null) stmt.close();
                    }catch(SQLException se2){
                    }// 什么都不做
                    try{
                        if(conn!=null) conn.close();
                    }catch(SQLException se){
                        se.printStackTrace();
                    }
                }
                Log.d("MySql","Goodbye!");
                //
                //////////////////
            }
        }).start();

    }
}
