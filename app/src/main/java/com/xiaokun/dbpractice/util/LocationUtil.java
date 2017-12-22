package com.xiaokun.dbpractice.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.xiaokun.dbpractice.R;
import com.xiaokun.dbpractice.config.Constants;
import com.xiaokun.dbpractice.entity.LocationEntity;
import com.xiaokun.dbpractice.toast.Toasts;

import org.greenrobot.eventbus.EventBus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import static com.xiaokun.dbpractice.App.mPref;


/**
 * Created by 肖坤 on 2017/9/27.
 * 定位帮助类
 */
public class LocationUtil
{
    public static AMapLocationClient locationClient;
    public static AMapLocationClientOption locationOption;
    public static LatLng latLng;
    private static boolean isInit = false;
    private static LocationEntity locationEntity;

    /**
     * 初始化定位
     */
    public static void initLocation()
    {
        //初始化client
        locationClient = getLocationClient();
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        locationEntity = new LocationEntity();
        locationClient.startLocation();
    }

    private static AMapLocationClient getLocationClient()
    {
        if (locationClient == null)
        {
            locationClient = new AMapLocationClient(AppUtils.getAppContext());
        }
        return locationClient;
    }

    public static void startLocation()
    {
        if (locationClient != null)
        {
            locationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    public static void stopLocation()
    {
        if (locationClient != null)
        {
            locationClient.stopLocation();
        }
    }

    /**
     * 默认配置
     *
     * @return
     */
    private static AMapLocationClientOption getDefaultOption()
    {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(20000);
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    static AMapLocationListener locationListener = new AMapLocationListener()
    {
        @Override
        public void onLocationChanged(AMapLocation location)
        {
            if (null != location)
            {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0)
                {
                    double latitude = location.getLatitude();//纬度
                    double longitude = location.getLongitude();//经度
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mPref.put(Constants.LAT, latitude + "");
                    mPref.put(Constants.LONG, longitude + "");
                    String city = location.getCity();
                    String cityCode = location.getCityCode();
                    location.getCityCode();
                    mPref.put(Constants.LOCATION_CITY, city);
                    mPref.put(Constants.LOCATION_CITY_CODE, cityCode);
                    if (TextUtils.isEmpty(locationEntity.getCity()) && !TextUtils.isEmpty(city))
                    {
                        locationEntity.setCity(city);
                        locationEntity.setLat(latitude);
                        locationEntity.setLng(longitude);
                        EventBus.getDefault().post(locationEntity);
                    }

                    if (!isInit)
                    {
                        // 设置行政区划查询监听
                        DistrictSearch districtSearch = new DistrictSearch(AppUtils.getAppContext());
                        districtSearch.setOnDistrictSearchListener(onDistrictSearchListener);
                        // 查询中国的区划
                        DistrictSearchQuery query = new DistrictSearchQuery();
                        query.setKeywords(city);
                        districtSearch.setQuery(query);
                        // 异步查询行政区
                        districtSearch.searchDistrictAsyn();
                    }
                } else
                {
                    mPref.put(Constants.LOCATION_CITY, "武汉市");
                    mPref.put(Constants.LOCATION_CITY_CODE, "420100");
                    Toasts.showSingleShort(AppUtils.getAppContext().getResources().getString(R.string.location_error) + location.getErrorCode());
                }
            }
        }
    };


    static DistrictSearch.OnDistrictSearchListener onDistrictSearchListener = new DistrictSearch.OnDistrictSearchListener()
    {
        @Override
        public void onDistrictSearched(DistrictResult districtResult)
        {
            if (districtResult != null)
            {
                if (districtResult.getAMapException().getErrorCode() == AMapException.CODE_AMAP_SUCCESS)
                {
                    isInit = true;
                    List<DistrictItem> districts = districtResult.getDistrict();
                    LatLonPoint center = districts.get(0).getCenter();
                    double latitude = center.getLatitude();
                    double longitude = center.getLongitude();
                    mPref.put(Constants.CURRENT_CITY_CENTER_LAT, latitude + "");
                    mPref.put(Constants.CURRENT_CITY_CENTER_LNG, longitude + "");
                } else
                {
                    Toasts.showSingleShort(districtResult.getAMapException().getErrorMessage());
                }
            }
        }
    };

    public LocationEntity getLocationEntity()
    {
        return locationEntity;
    }

    public static String sha1(Context context)
    {
        try
        {
            PackageInfo info = null;
            try
            {
                info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), PackageManager.GET_SIGNATURES);
            } catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();
            }
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++)
            {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                {
                    hexString.append("0");
                }
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
