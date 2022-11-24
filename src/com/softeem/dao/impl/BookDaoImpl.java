package com.softeem.dao.impl;


import com.softeem.bean.Book;
import com.softeem.dao.BookDao;

import com.softeem.utils.BaseDao;
import com.softeem.utils.JdbcUtils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class BookDaoImpl extends BaseDao implements BookDao {
    @Override
    public Integer queryForPageTotalCount()throws SQLException{
        String sql = "select count(*) from t_book";
        ScalarHandler<Long> handler = new ScalarHandler<>();
        Long query = queryRunner.query(sql, handler);
        return  query.intValue();
    }

    @Override
    public List<Book> queryForPageItems(int begin, int pageSize) throws SQLException {
        //开启下划线->驼峰转换所用 - start
        BeanProcessor bean = new GenerousBeanProcessor();
        RowProcessor processor = new BasicRowProcessor(bean);
        //开启下划线->驼峰转换所用 - end
        String sql = "select * from t_book order by id desc limit ? , ?";
        return queryRunner.query(sql,new BeanListHandler<Book>(Book.class,processor),begin,pageSize);
    }

    @Override
    public Integer queryForPageTotalCount(String name, String author, BigDecimal min, BigDecimal max) throws SQLException {
        StringBuilder sql =new StringBuilder( "select count(*) from t_book where 1 = 1 ");
        ArrayList list = new ArrayList();
        if (name != null && !"".equals(name)){
            sql.append(" and name like ? ");
            list.add("%"+name+"%");
        }

        if (author != null && !"".equals(author)){
            sql.append(" and author like ? ");
            list.add("%"+author+"%");
        }
        if(( min != null && min.signum() == 1)&&(max != null && max.signum() == 1)){

            //min大于max 交换min max
            if(min.compareTo(max)==1){
                BigDecimal temp = min;
                min = max;
                max = temp;
            }
            sql.append(" and price bewteen ? and ? ");
            list.add(min);
            list.add(max);
        }else if (min != null && min.signum() == 1){
            sql.append(" and price >= ? ");
            list.add(min);

        }else if (max != null && max.signum() == 1){
            sql.append(" and price <= ? ");
            list.add(max);
        }
        ScalarHandler<Long> handler = new ScalarHandler<>();
        Long query = queryRunner.query(sql.toString(), handler,list.toArray());
        return  query.intValue();
    }

    @Override
    public List<Book> queryForPageItems(int begin, int pageSize,String name,String author, BigDecimal min, BigDecimal max) throws SQLException {
        StringBuilder sql =new StringBuilder( "select * from t_book where 1 = 1 ");
        //开启下划线->驼峰转换所用 - start
        BeanProcessor bean = new GenerousBeanProcessor();
        RowProcessor processor = new BasicRowProcessor(bean);
        //开启下划线->驼峰转换所用 - end
        ArrayList list = new ArrayList();
        if (name != null && !"".equals(name)){
            sql.append(" and name like ? ");
            list.add("%"+name+"%");
        }

        if (author != null && !"".equals(author)){
            sql.append(" and author like ? ");
            list.add("%"+author+"%");
        }
        if(( min != null && min.signum() == 1)&&(max != null && max.signum() == 1)){

            //min大于max 交换min max
            if(min.compareTo(max)==1){
                BigDecimal temp = min;
                min = max;
                max = temp;
            }
            sql.append(" and price bewteen ? and ? ");
            list.add(min);
            list.add(max);
        }else if (min != null && min.signum() == 1){
            sql.append(" and price >= ? ");
            list.add(min);

        }else if (max != null && max.signum() == 1){
            sql.append(" and price <= ? ");
            list.add(max);
        }
        String end = " order by id desc limit ? , ? ";
        sql.append(end);
        list.add(begin);
        list.add(pageSize);
        return queryRunner.query(sql.toString(),new BeanListHandler<Book>(Book.class,processor),list.toArray());

    }

    @Override
    public List<Book> findAll() throws SQLException {
        //开启下划线->驼峰转换所用 - start
        BeanProcessor bean = new GenerousBeanProcessor();
        RowProcessor processor = new BasicRowProcessor(bean);
        //开启下划线->驼峰转换所用 - end
        BeanListHandler<Book> handler = new BeanListHandler<>(Book.class,processor);

        List<Book> BookList = queryRunner.query("select * from t_book order by id desc",handler );

        return BookList;
    }

    @Override
    public void save(Book Book) throws SQLException {
        queryRunner.update("insert into t_book values(null,?,?,?,?,?,?)",Book.getName(),Book.getAuthor(),Book.getPrice(),Book.getSales(),Book.getStock(),Book.getImgPath());
        System.out.println("Insert Successfully!");
    }

    @Override
    public void updateById(Book book) throws SQLException {
        Connection connection = JdbcUtils.getConnection();
        queryRunner.update(connection,"update t_book set name=?,author=?,price = ?, sales = ? ,stock = ? ,imgPath = ? where id = ?",book.getName(),book.getAuthor(),book.getPrice(),book.getSales(),book.getStock(),book.getImgPath(),book.getId());
        System.out.println("Update Successfully!");
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        queryRunner.update("delete from t_book where id = ? " , id);
        System.out.println("Delete Successfully!");
    }

    @Override
    public Book findById(Integer id) throws SQLException {
        //开启下划线->驼峰转换所用 - start
        BeanProcessor bean = new GenerousBeanProcessor();
        RowProcessor processor = new BasicRowProcessor(bean);
        //开启下划线->驼峰转换所用 - end
        BeanHandler<Book> handler = new BeanHandler<>(Book.class,processor);
        Book book = queryRunner.query("select * from t_book where id = ?", handler,id);
        System.out.println(book);
        return book;
    }

    @Override
    public List<Book> page(Integer pageNumber) throws SQLException {
        String sql = "select * from t_book limit ? , ?";
        BeanListHandler<Book> handler = new BeanListHandler<>(Book.class);
        List<Book> bookList = queryRunner.query(sql, handler, (pageNumber - 1) * pageSize, pageSize);
        return bookList;
    }

    @Override
    public Integer pageRecord() throws SQLException {
        String sql = "select count(*) from t_book";
        ScalarHandler<Long> handler = new ScalarHandler<>();
        Long i = queryRunner.query(sql, handler);
        return i.intValue();
    }
}
