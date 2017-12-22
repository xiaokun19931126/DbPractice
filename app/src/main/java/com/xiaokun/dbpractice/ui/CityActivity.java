package com.xiaokun.dbpractice.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaokun.dbpractice.R;
import com.xiaokun.dbpractice.dbHelper.AllCityDbHelper;
import com.xiaokun.dbpractice.entity.City;
import com.xiaokun.dbpractice.widget.SidebarView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaokun
 * @date 2017/12/22
 */

public class CityActivity extends AppCompatActivity
{
    private EditText mCityEtSearch;
    private RelativeLayout mCityFraContentLayout;
    private RecyclerView mCityRvCityList;
    private TextView mCityTvShow;
    private RecyclerView mCityRvSearchResult;
    private TextView mCityTvNoResult;
    private SidebarView mCitySidebar;
    private SQLiteDatabase writableDatabase;
    private Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initView();
        initListener();
        initData();
    }

    private void initView()
    {
        mCityEtSearch = (EditText) findViewById(R.id.city_et_search);
        mCityFraContentLayout = (RelativeLayout) findViewById(R.id.city_fra_content_layout);
        mCityRvCityList = (RecyclerView) findViewById(R.id.city_rv_city_list);
        mCityTvShow = (TextView) findViewById(R.id.city_tv_show);
        mCityRvSearchResult = (RecyclerView) findViewById(R.id.city_rv_search_result);
        mCityTvNoResult = (TextView) findViewById(R.id.city_tv_no_result);
        mCitySidebar = (SidebarView) findViewById(R.id.city_sidebar);


    }

    private void initListener()
    {

    }

    private List<City> cities = new ArrayList<>();

    private void initData()
    {
        AllCityDbHelper allCityDbHelper = new AllCityDbHelper(this);
        try
        {
            allCityDbHelper.createDataBase();
            writableDatabase = allCityDbHelper.getWritableDatabase();
            cursor = writableDatabase.rawQuery("select * from city", null);
            while (cursor.moveToNext())
            {
                int name = cursor.getColumnIndex("name");
                int pinyin = cursor.getColumnIndex("pinyin");
                String cityName = cursor.getString(name);
                String cityPinyin = cursor.getString(pinyin);
                City city = new City(cityName, cityPinyin);
                cities.add(city);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
