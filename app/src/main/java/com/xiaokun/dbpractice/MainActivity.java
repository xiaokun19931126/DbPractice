package com.xiaokun.dbpractice;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.location);
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
                        } else
                        {
                            //权限申请未通过
                        }
                    }
                });
    }
}
