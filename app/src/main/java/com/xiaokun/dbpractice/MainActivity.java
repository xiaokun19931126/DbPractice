package com.xiaokun.dbpractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
                //huoqu quanxian
                break;
            default:

                break;
        }
    }
}
