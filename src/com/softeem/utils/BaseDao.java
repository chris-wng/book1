package com.softeem.utils;

import org.apache.commons.dbutils.*;

/**
 * BaseDao的目地就是去优化dao实现类
 */
public abstract class BaseDao {
    public QueryRunner queryRunner ;
    public int pageSize ;
    public BaseDao(){
        queryRunner = new QueryRunner(com.softeem.utils.MyDataSource.getDataSource());
        pageSize = 4 ;
    }

    public RowProcessor getProcessor() {
        //开启下划线->驼峰转换所用 - strat
        BeanProcessor bean = new GenerousBeanProcessor();
        RowProcessor processor = new BasicRowProcessor(bean);
        //开启下划线->驼峰转换所用 - end
        return processor;
    }
}
