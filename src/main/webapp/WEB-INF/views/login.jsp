<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<title>主页</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	 <h1>login page</h1>  
    <form id="" action="${ctx}/login/doLogin" method="post">  
        <label>User Name</label> <input tyep="text" name="userName"  
            maxLength="40" /> <label>Password</label><input type="password"  
            name="userPass" /> <input type="submit" value="login" />  
    </form>  
    <%--用于输入后台返回的验证错误信息 --%>  
    <P><c:out value="${msg}" /></P>  
</body>
</html>