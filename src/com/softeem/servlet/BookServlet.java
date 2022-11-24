package com.softeem.servlet;

import com.softeem.bean.Book;
import com.softeem.service.BookService;
import com.softeem.service.impl.BookServiceImpl;
import com.softeem.utils.BaseServlet;
import com.softeem.utils.Page;
import com.softeem.utils.WebUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Chris
 */
@WebServlet(name = "BookServlet", value = "/BookServlet")
public class BookServlet extends BaseServlet {
    BookService bookService = new BookServiceImpl();
    protected void searchPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String name = request.getParameter("name")==null ? "":request.getParameter("name");
        String author = request.getParameter("author")==null ? "":request.getParameter("author");
        Integer min = WebUtils.parseInt(request.getParameter("min"),0);
        Integer max = WebUtils.parseInt(request.getParameter("max"),0);
        int pageNo = WebUtils.parseInt(request.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(request.getParameter("pageSize"), Page.PAGE_SIZE);
        try {
            Page<Book> page = bookService.pageByPrice(pageNo, pageSize,name,author,new BigDecimal(min), new BigDecimal(max));
            page.setUrl("BookServlet?action=searchPage&name="+name+"&author="+author+"&min="+(min==0?"":min)+"&max="+(max==0?"":max)+"&");
            request.setAttribute("page", page);
            request.setAttribute("name", name);
            request.setAttribute("author", author);
            request.setAttribute("min", min);
            request.setAttribute("max", max);

            request.getRequestDispatcher("/home.jsp").forward(request, response);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    protected void queryById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String pageNo = request.getParameter("pageNo");
        try {
            Book book = bookService.queryBookById(Integer.valueOf(id));
            request.setAttribute("book", book);
            request.setAttribute("pageNo",pageNo);
            request.getRequestDispatcher("/pages/manager/book_edit.jsp").forward(request, response);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        Book book = new Book();
        String pageNo = request.getParameter("pageNo");
        if (ServletFileUpload.isMultipartContent(request)) {
            //创建FileItemFactory 工厂实现类
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            // 创建用于解析上传数据的工具类ServletFileUpload 类
            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
            try {
                // 解析上传的数据，得到每一个表单项FileItem
                List<FileItem> list = servletFileUpload.parseRequest(request);
                //循环这6段数据并处理它们
                for (FileItem fileItem : list) {
                    //判断那些是普通表单项,还是上传的文件类型
                    if (fileItem.isFormField()) {
                        if ("id".equals(fileItem.getFieldName())) {
                            book.setId(Integer.parseInt(fileItem.getString()));
                            book= bookService.queryBookById(book.getId());
                        } else if ("name".equals(fileItem.getFieldName())) {
                            book.setName(fileItem.getString("utf-8"));
                        } else if ("author".equals(fileItem.getFieldName())) {
                            book.setAuthor(fileItem.getString("utf-8"));
                        } else if ("price".equals(fileItem.getFieldName())) {
                            book.setPrice(new BigDecimal(fileItem.getString()));
                        } else if ("sales".equals(fileItem.getFieldName())) {
                            book.setSales(Integer.valueOf(fileItem.getString()));
                        } else if ("stock".equals(fileItem.getFieldName())) {
                            book.setStock(Integer.parseInt(fileItem.getString()));
                        }
//                        else if ("oldPath".equals(fileItem.getFieldName())){
//                            book.setImgPath(fileItem.getString());
//                        }
                    } else {
                        //处理文件类型(文件上传)
                        String filename = fileItem.getName();
                        if (!"".equals(filename)) {
                            String suffix = filename.substring(filename.lastIndexOf("."));
                            //通过时间毫秒 + 后缀 = 新文件名
                            String newfilename = System.currentTimeMillis() + suffix;
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String format = simpleDateFormat.format(new Date());
                            File file = new File("f:/bookimg/" + format + "/");
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            System.out.println(file.getAbsolutePath());
                            fileItem.write(new File(file, newfilename));
                            String path = "f:/" + book.getImgPath();
                            WebUtils.delete(path);
                            book.setImgPath("/bookimg/" + format + "/" + newfilename);
                        }
                    }
                }
                bookService.updateBook(book);
                response.sendRedirect("BookServlet?action=page&pageNo="+pageNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            out.println("不是多段数据..无法上传文件!");
        }
    }

    protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

       // String id = request.getParameter("id");
        Integer id = WebUtils.parseInt(request.getParameter("id"), 0);
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        try {
            Book book = bookService.queryBookById(Integer.valueOf(id));
            String path = "f:/" + book.getImgPath();
             WebUtils.delete(path);
            bookService.deleteBookById(Integer.valueOf(id));
            //&pageNo="+pageNo
            response.sendRedirect("BookServlet?action=page&pageNo="+pageNo);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    protected void page(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize
        int pageNo = WebUtils.parseInt(request.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(request.getParameter("pageSize"), Page.PAGE_SIZE);

        try {
            Page<Book> page = bookService.page(pageNo,pageSize);
            page.setUrl("BookServlet?action=page&");
            request.setAttribute("page", page);
            request.getRequestDispatcher("pages/manager/book_manager.jsp").forward(request, response);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    protected void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        Book book = new Book();

        if (ServletFileUpload.isMultipartContent(request)) {
            //创建FileItemFactory 工厂实现类
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            // 创建用于解析上传数据的工具类ServletFileUpload 类
            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
            try {
                // 解析上传的数据，得到每一个表单项FileItem
                List<FileItem> list = servletFileUpload.parseRequest(request);
                //循环这6段数据并处理它们
                for (FileItem fileItem : list) {
                    //判断那些是普通表单项,还是上传的文件类型
                    if (fileItem.isFormField()) {

                        if ("name".equals(fileItem.getFieldName())) {
                            book.setName(fileItem.getString("utf-8"));
                        } else if ("author".equals(fileItem.getFieldName())) {
                            book.setAuthor(fileItem.getString("utf-8"));
                        } else if ("price".equals(fileItem.getFieldName())) {
                            book.setPrice(new BigDecimal(fileItem.getString()));
                        } else if ("sales".equals(fileItem.getFieldName())) {
                            book.setSales(Integer.valueOf(fileItem.getString()));
                        } else if ("stock".equals(fileItem.getFieldName())) {
                            book.setStock(Integer.parseInt(fileItem.getString()));
                        }
                    } else {
                        //处理文件类型(文件上传)
                        String filename = fileItem.getName();
                        //文件名 = 123.jpg       suffix = .jpg
                        String suffix = filename.substring(filename.lastIndexOf("."));
                        //通过时间毫秒 + 后缀 = 新文件名
                        String newfilename = System.currentTimeMillis() + suffix;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String format = simpleDateFormat.format(new Date());
                        File file = new File("f:/bookimg/" + format + "/");
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        System.out.println(file.getAbsolutePath());
                        fileItem.write(new File(file, newfilename));
                        book.setImgPath("/bookimg/" + format + "/" + newfilename);
                    }
                }
                bookService.addBook(book);
                response.sendRedirect("BookServlet?action=page");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            out.println("不是多段数据..无法上传文件!");
        }


    }


}
