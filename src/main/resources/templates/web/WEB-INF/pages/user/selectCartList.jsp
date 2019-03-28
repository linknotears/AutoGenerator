<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
<script type="text/javascript">
$(function(){
	//加载data
	loadUrls =[
		{
			url:"orderForm/getCart.html",
			refNames:['existCart']
		}
	];
	//加载标签
	loadEls = ['#mainData'];
	//初始化数据
	init();

});
function remove(id){
	request({
		url:'orderForm/removeitem.html',
		data:{'roomId':id},
		successHandle:data =>{
			//重新获取数据
			loadData();
		}
	});
}
function createOrder(){
	request({
		url:'orderForm/createOrder.html',
		successHandle:data =>{
			alert("下单成功！");
			//重新获取数据
			window.location.href = "base/goto/user/selectOrderList.html";
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
			<table id="mainData"
				class="table table-striped table-bordered table-hover"
				style="width:70%">
				<tr>
					<th>房间号</th>
					<th>价格</th>
					<th>操作</th>
				</tr>
				<tr>
				<tr v-for="orderitem in data.existCart.orderitems">
					<th>{{orderitem.roomId}}</th>
					<th>{{orderitem.subtotalPrice}}元</th>
					<th><center>
					<button @click="remove(orderitem.roomId)">删除</button>
					</center></th>
				</tr>
				<tr bgcolor="gray">
					<th>总价格</th>
					<th>{{data.existCart.orderForm.totalPrice}}元</th>
					<th><center>
					<button v-if="data.existCart.orderForm.totalPrice!=0" onclick="createOrder()">下单</button>
					</center></th>
				</tr>
			</table>
		</center>
	</div>
</body>
</html>
