package com.task.dd.greenbox.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mob.MobSDK;
import com.task.dd.greenbox.MainActivity;
import com.task.dd.greenbox.bean.BeanLab;
import com.task.dd.greenbox.database.DBSchema;
import com.task.dd.greenbox.tool.Util;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.gui.RegisterPage;

/**
 * Created by dd on 2017/3/15.
 */

public class PhoneLoginActivity extends AppCompatActivity {
    private static final String USER_PHONE="PhoneLoginActivity.user_phone";
    private static  boolean back=false;
    private BeanLab beanLab;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobSDK.init(this,"25ab1dd39eb22","16d823f2b664b9a2809ebc034091dc54");
        beanLab=BeanLab.get(getApplicationContext());
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if(result == cn.smssdk.SMSSDK.RESULT_COMPLETE){
                    @SuppressWarnings("unchecked")
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    //应用了第三方验证码登录 ，没有办法在获得手机号码之前进行是数据库的查询。
                    Cursor cursor= beanLab.queryPhone(DBSchema.Table.NAME,new String[]{"phone"},"phone=?",new String[]{phone});
                    if (cursor.getCount() >= 1){
                        Util.showToast(getApplicationContext(),"手机号已注册，直接登录");

                        Intent i=newIntent(PhoneLoginActivity.this,MainActivity.class,phone);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i=newIntent(PhoneLoginActivity.this,PassWordActivity.class,phone);
                        startActivity(i);
                        finish();
                    }

                }
            }
        });
        registerPage.show(this);
        finish();



    }
    public static Intent newIntent(Context context,Class c ,String phone){
        Intent i= new Intent(context,c);
        i.putExtra(USER_PHONE,phone);
        return i;
    }



}

