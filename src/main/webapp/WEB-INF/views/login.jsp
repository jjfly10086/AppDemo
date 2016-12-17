<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<script src="${ctx}/static/js/jquery-3.1.1.min.js"></script>
<script src="${ctx}/static/js/cryptico.js"></script>
<script>
    var publicKey = "${publicKey}";
    $(function () {
        $('#login_btn').click(function () {
            var userNameStr = cryptico.encrypt($('#userName1').val(), publicKey).cipher.toString();
            var userPassStr = cryptico.encrypt($('#userPass1').val(), publicKey).cipher.toString();
            $("#userName").val(userNameStr);
            $("#userPass").val(userPassStr);
            console.log(userNameStr);
            console.log(userPassStr);
            $("#loginForm").submit();
        });
    });
</script>
<head>
<title>主页</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	 <h1>login page</h1>
     <label>User Name</label>
     <input id="userName1" type="text" name="userName1" maxLength="40" />
     <label>Password</label>
     <input id="userPass1" type="password" name="userPass1" />
    <form id="loginForm" action="${ctx}/login/doLogin" method="post">
        <input type="hidden" id="userName" name="userName"/>
        <input type="hidden" id="userPass" name="userPass"/>
        <input id="login_btn" type="button" value="登录" />
    </form>  
    <%--用于输入后台返回的验证错误信息 --%>  
    <P><c:out value="${msg}" /></P>
    <p>${publicKey}</p>
</body>
</html>