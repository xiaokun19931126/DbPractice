package com.xiaokun.dbpractice.entity;

/**
 * @author xiaokun
 * @date 2017/12/22
 */

public class LocationEntity
{
    private double lat;
    private double lng;
    private String city;

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public double getLng()
    {
        return lng;
    }

    public void setLng(double lng)
    {
        this.lng = lng;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    @Override
    public String toString()
    {
        return "LocationEntity{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", city='" + city + '\'' +
                '}';
    }
}
