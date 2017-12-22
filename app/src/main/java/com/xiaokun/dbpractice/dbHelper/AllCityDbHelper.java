package com.xiaokun.dbpractice.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author xiaokun
 * @date 2017/12/22
 */

public class AllCityDbHelper extends SQLiteOpenHelper
{

    private static String DB_PATH = "/data/data/com.xiaokun.dbpractice/databases/";
    private static String DB_NAME = "all_cities.db";
    private static final int DB_VERSION = 3;
    private Context mContext;

    public AllCityDbHelper(Context context)
    {
        this(context, DB_PATH + DB_NAME, null, DB_VERSION);
    }

    public AllCityDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

    /**
     * 创建数据库,本来应该放在onCreate中，但是一旦数据库存在onCreate方法就不会触发
     *
     * @throws IOException
     */
    public void createDataBase() throws IOException
    {
        boolean isExist = checkDataBase();
        //如果数据库不存在就创建数据库
        if (!isExist)
        {
            File dir = new File(DB_PATH);
            if (dir != null)
            {
                dir.mkdirs();
            }
            File dbFile = new File(DB_PATH + DB_NAME);
            //如果数据库文件存在，则删除刚创建的file
            if (dbFile.exists())
            {
                dbFile.delete();
            }
            SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            copyDataBase();
        }
    }

    /**
     * 检测是否存在
     *
     * @return
     */
    public boolean checkDataBase()
    {
        SQLiteDatabase checkDb = null;
        String dbPath = DB_PATH + DB_NAME;
        try
        {
            checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e)
        {
            e.printStackTrace();
        } finally
        {
            if (checkDb != null)
            {
                checkDb.close();
            }
        }
        return checkDb != null ? true : false;
    }

    /**
     * 复制
     *
     * @throws IOException
     */
    private void copyDataBase() throws IOException
    {
        InputStream inputStream = mContext.getAssets().open("cities.db");
        String filename = DB_PATH + DB_NAME;
        OutputStream outputStream = new FileOutputStream(filename);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

}
