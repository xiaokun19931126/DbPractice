package com.xiaokun.dbpractice.entity;

/**
 * @author xiaokun
 * @date 2017/12/22
 */

public class City
{
    private String name;
    private String pinyin;

    public City(String name, String pinyin)
    {
        this.name = name;
        this.pinyin = pinyin;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPinyin()
    {
        return pinyin;
    }

    public void setPinyin(String pinyin)
    {
        this.pinyin = pinyin;
    }

    @Override
    public String toString()
    {
        return "City{" +
                "name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }
}
