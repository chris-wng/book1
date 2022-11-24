<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>我的订单</title>
  <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
  <link type="text/css" rel="stylesheet" href="static/css/style.css">
  <style type="text/css">
    h1 {
      text-align: center;
      margin-top: 200px;
    }
  </style>
</head>
<body>

<div id="header">
  <img class="logo_img" alt="" src="static/img/logo.gif" width="230px" height="80px">
  <span class="wel_word">我的订单</span>
  <div>
    <span>欢迎<span class="um_span">${sessionScope.user.username}</span>光临书城</span>
    <a href="OrderServlet?action=listOrder">我的订单</a>
    <a href="index.jsp">注销</a>&nbsp;&nbsp;
    <a href="index.jsp">返回</a>
  </div>
</div>

<div id="main">

  <table>
    <tr>

      <td>书名</td>
      <td>数量</td>
      <td>单价</td>
      <td>总价</td>
      <td>订单编号</td>
    </tr>

      <tr>
        <td>贪玩蓝月</td>
        <td>2</td>
        <td>999.00</td>
        <td>1998.00</td>
        <td>16599576904451</td>

      </tr>

  </table>
  <jsp:include page="/pages/common/page.jsp"></jsp:include>

</div>

<%@ include file="/pages/common/bottom.jsp" %>
</body>
</html>