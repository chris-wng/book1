package com.softeem.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

//单例模式
public class MyDataSource {

    public static DataSource dataSource = null ;

    private MyDataSource(){
    }

    public static DataSource getDataSource(){
        if(dataSource == null) {
            try {
                Properties pro = new Properties();
                pro.load(MyDataSource.class.getClassLoader().getResourceAsStream("druid.properties"));
                //创建一个数据源对象
                dataSource = DruidDataSourceFactory.createDataSource(pro);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataSource;
    }
}
