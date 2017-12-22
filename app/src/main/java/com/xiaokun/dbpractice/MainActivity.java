package com.xiaokun.dbpractice;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiaokun.dbpractice.entity.LocationEntity;
import com.xiaokun.dbpractice.toast.ToastLoadUtil;
import com.xiaokun.dbpractice.toast.ToastTipUtil;
import com.xiaokun.dbpractice.toast.Toasts;
import com.xiaokun.dbpractice.ui.CityActivity;
import com.xiaokun.dbpractice.util.LocationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String LOADING = "定位中...";
    public static final String DOWN = "定位完成";
    public static final String SEND_SUCCESS = "发送成功";

    private Button btn1;
    private Button currentCity;
    private Button btn2;
    private Button btn3;
    private Button btn4;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        btn1 = (Button) findViewById(R.id.location);
        btn2 = (Button) findViewById(R.id.location2);
        btn3 = (Button) findViewById(R.id.button);
        btn4 = (Button) findViewById(R.id.button2);
        currentCity = (Button) findViewById(R.id.current_city);


        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.location:
                //申请权限
                applyLocation();
                break;
            case R.id.location2:
                ToastLoadUtil.showLoadTip(LOADING, ToastLoadUtil.VERTICAL);
                break;
            case R.id.button:
                ToastTipUtil.showTip(SEND_SUCCESS, 1500, ToastTipUtil.ICON_TYPE_SUCCESS);
                break;
            case R.id.button2:

                break;
            default:
                break;
        }
    }

    private void applyLocation()
    {
        RxPermissions rxPermissions = new RxPermissions(this);

        rxPermissions
                .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Boolean>()
                {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception
                    {
                        if (aBoolean)
                        {
                            //权限申请通过
                            LocationUtil.initLocation();
                            ToastLoadUtil.showLoadTip(LOADING, ToastLoadUtil.HORIZENTAL);
                            currentCity.setText(LOADING);
                        } else
                        {
                            //权限申请未通过
                            Toasts.showSingleShort("请通过定位权限");
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (ToastLoadUtil.isShowing())
            {
                ToastLoadUtil.dismissLoad();
                return true;
            } else
            {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReciver(LocationEntity locationEntity)
    {
        if (ToastLoadUtil.isShowing())
        {
            ToastLoadUtil.dismissLoad();
        }
        btn1.setText(DOWN);
        btn1.setEnabled(false);
        currentCity.setText(locationEntity.getCity());
        currentCity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toasts.showSingleShort("跳转city");
                Intent intent = new Intent(MainActivity.this, CityActivity.class);
                startActivity(intent);
            }
        });
    }

}
