<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<title>主页</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="${ctx}/static/js/jquery-3.1.1.min.js"></script>
<script src="${ctx}/static/js/bootstrap.min.js"></script>
<link href="${ctx}/static/css/bootstrap.min.css" rel="stylesheet">
<style type="text/css">
html, body {
	margin: 0px;
	width: 100%;
	height: 100%;
	width: 100%;
}

.login {
	position: absolute;
	top: 0px;
	width: 100%;
	height: 100%;
	background-color: gray;
	opacity: 0.5;
	display: none;
}
#datasource{
	margin-left: 20px;
}
</style>
</head>
<body onload="init()">
	<h2></h2>
	<nav class="navbar navbar-default" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">XXXX</a>
			</div>
			<div>
				<ul class="nav navbar-nav">
					<li class="active"><a href="#">tab1</a></li>
					<li><a href="#">tab2</a></li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">tab3 <b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<li><a href="#">tab3-1</a></li>
							<li><a href="#">tab3-2</a></li>
							<li><a href="#">tab3-3</a></li>

						</ul></li>
				</ul>
			</div>
			<div>
				<ul class="nav navbar-nav" style="float: right">
					<li><a href="javascript:void(0)" onclick="divShow()">登录</a></li>
					<li><a href="#">注册</a></li>
					<li><a href="${ctx}/login/doLogout">退出登录</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<table id="datasource" class="table table-bordered table-hover">
		<caption>用户列表</caption>
		<thead>
			<tr>
				<th>ID</th>
				<th>用户名</th>
				<th>创建时间</th>
			</tr>
		</thead>
		<tbody>
			
		</tbody>
	</table>
	<!--弹出层  -->
	<div class="login" onclick="divHide()"></div>
</body>
<script type="text/javascript">
//初始化数据
function init() {
	jQuery.ajax({
		type : 'post',
		url : "${ctx}/index/list",
		data : '',
		dataType : 'json',
		success : function(data) {
			var tbody = $("#datasource tbody");
			tbody.empty();
			for(var i=0;i<data.userList.length;i++){
				var tr ='<tr>'
				+'<td>'+data.userList[i].id+'</td>'
				+'<td>'+data.userList[i].userName+'</td>'
				+'<td>'+data.userList[i].createTime+'</td>'
				+'</tr>';
				tbody.append(tr);
			}
		},
		error : function() {

		}

	});

}
	function divShow() {
		$('.login').show();
	}
	function divHide() {
		$('.login').hide();
	}
</script>
</html>
