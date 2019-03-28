<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>查看用户详细</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="assets/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="assets/css/font-awesome.min.css" />
<link rel="stylesheet" href="assets/css/ace.min.css" />
<link rel="stylesheet" href="assets/css/ace-rtl.min.css" />
<link rel="stylesheet" href="assets/css/ace-skins.min.css" />
<script src="assets/js/ace-extra.min.js"></script>
<script type="text/javascript" src="common/util/netutil-1.0.js"></script>
<style type="text/css">
select {
	width: 160px;
}

input {
	width: 160px;
}
</style>
<script type="text/javascript">
	
	function saveOrUpdateForm(form){
		return request({
			'url': 'user/saveOrUpdate.html',
			'form': form,
			'successHandle':function(data){
				window.location.href = "base/goto/common/selectInforList.html";
			}
		});
	}
</script>
</head>

<body>
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try {
				ace.settings.check('breadcrumbs', 'fixed')
			} catch (e) {
			}
		</script>

		<ul class="breadcrumb">
		</ul>
		<!-- .breadcrumb -->

	</div>

	<div class="row">
		<center>
			<form onsubmit="return saveOrUpdateForm(this)" method="post">
				<table id="mainData"
					class="table table-striped table-bordered table-hover"
					style="width:70%">
					<tr>
						<th>用户名</th>
						<th>
						<input type="hidden" name="root" value="2">
						<input type="text" name="username"></th>
						<th>密码</th>
						<th><input type="password" name="password"></th>
					</tr>
					<tr>
						<th>手机号</th>
						<th><input type="number" name="phoneNumber"></th>
						<th>真实姓名</th>
						<th><input type="text" name="name"></th>
					</tr>
					<tr>
						<th>性别</th>
						<th>
							<select name="sex">
								<option value="1">男</option>
								<option value="0">女</option>	
							</select>
						</th>
						<th>年龄</th>
						<th><input type="text" name="age"></th>
					</tr>
				</table>
				<input class="btn btn-primary" type="submit" value="提交" /> <input
					class="btn" type="button" value="返回" onclick="history.go(-1)" />
			</form>
		</center>

	</div>
</body>
</html>
