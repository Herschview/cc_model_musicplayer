package com.miniccmusicplayer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hersch.musicplayer.R;
import com.miniccmusicplayer.bean.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginInActivity extends AppCompatActivity {
    private Button registerBtn;
    private Button loginBtn;
    private EditText userEdit;
    private EditText pwdEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        userEdit = (EditText) findViewById(R.id.login_user_edit);
        pwdEdit = (EditText) findViewById(R.id.login_pwd_edit);
        registerBtn = (Button) findViewById(R.id.login_register_btn);
        loginBtn = (Button) findViewById(R.id.login_login_btn);
        registerBtn.setOnClickListener(listener);
        loginBtn.setOnClickListener(listener);
        checkCurrentUser();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_register_btn:
                    Intent intent = new Intent(LoginInActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.login_login_btn:
                    //进行用户名以及密码的判断
                    LoginCurrentUser();
                    break;
            }
        }
    };

    /**
     * 检测本地用户缓存
     */
    public void checkCurrentUser(){
        BmobUser bmobUser = BmobUser.getCurrentUser(this, MyUser.class);
        if (bmobUser!=null) {
            String userName = bmobUser.getUsername();
            String password = (String)BmobUser.getObjectByKey(getApplicationContext(),"password");
            userEdit.setText(userName);
            pwdEdit.setText(password);
        }
    }
    /**
     * 检测登录操作
     */
    public void LoginCurrentUser() {
        BmobUser bmobUser = new BmobUser();
        String username = userEdit.getText().toString();
        String pwd = pwdEdit.getText().toString();
        //非空
        if (username != null && pwd != null) {
            bmobUser.setUsername(username);
            bmobUser.setPassword(pwd);
            bmobUser.login(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginInActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(getApplicationContext(), "用户名不存在或密码错误", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "用户名不存在或密码错误", Toast.LENGTH_SHORT).show();
        }
    }
}