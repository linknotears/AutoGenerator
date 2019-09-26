<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>查看记录</title>

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

<link href="${pageContext.request.contextPath}/css/common.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/cart.css"
	rel="stylesheet" type="text/css" />
	
<script type="text/javascript" src="common/util/netutil-1.0.js"></script>

<script type="text/javascript">
$(function(){
	//加载data
	loadUrls =[
		{
			url:"order/findPage.html",
			data:{
				page: getParameter('page',1)
			},
			refNames:['orders','pageData']
		}
	];
	//加载标签
	loadEls = ['#mainData',"#pageSelect"];
	//初始化数据
	init();

});

function handleOrder(id){
	request({
		url: "order/saveOrUpdate.html",
		data: {'state': 1, 'id': id},
		successHandle: function(data){
			loadData();
		}
	});
}

</script>
</head>

<body>

	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
							try{ace.settings.check('breadcrumbs' , 'fixed');}catch(e){}
						</script>

		<ul class="breadcrumb">
			<li><i class="icon-home home-icon"></i> <a href="#">审批管理</a></li>

			
			<li class="active">查看订单</li>
		</ul>
		<!-- .breadcrumb -->

		<div class="nav-search" id="nav-search">
			<form class="form-search" action="selectRecord" id="form1" method="post">
				<span class="input-icon"> <input type="text" name="search" style="width: 300px"
					placeholder="" class="nav-search-input"
					id="nav-search-input" autocomplete="off" /> <a
					class="icon-search nav-search-icon" onclick="formsubmit()"></a>
				</span>
			</form>
		</div>
		<!-- #nav-search -->
	</div>
		

		<div class="row">
			<div class="col-xs-12">
				<div class="table-header">查找</div>

				<div class="table-responsive">
				<div class="span24">
					<table width="100%" id="mainData"
					class="table table-striped table-bordered table-hover" >
				<tbody v-for="order in data.orders">
						<tr>
							<th>
							订单编号:{{order.id}}
							</th>
							<th>
								订单状态：
								<font v-if="order.state==0" color="red">
									未付款
								</font>
								<font v-if="order.state==1" color="green">
									已处理
								</font>
								<font v-if="order.state==2" color="orange">
									已付款,<button @click="handleOrder(order.id)">点击处理</button>
								</font>
							</th>
							<th>
								总价格:{{order.totalPrice}}
							</th>
							<td></td>
						</tr>
						<tr  style="background: gray;">
							<td width="30%">商品图片</td>
							<td width="20%">商品名</td>
							<td width="20%">数量/kg</td>
							<td width="50%">价格</td>
						</tr>

						<tr v-for="orderitem in order.orderitemList" style="background: white;">
							<td>
								<img width="80" height="80" alt="" :src="orderitem.goods.pic">
							</td>
							<td>{{orderitem.goods.name }}</td>
							<td class="quantity">{{orderitem.count }}</td>
							<td>{{orderitem.subtotalPrice}}
								</td>
						</tr>
				</tbody>
			</table>
			</div>
				<center id="pageSelect">
					<div v-if="page.totalPage != 0">
						当前{{page.page }}/{{page.totalPage }}页     &nbsp;&nbsp;
		 				<span v-if="page.page != 1">
		 					<a :href="page.pageUri+'?page=1'">首页</a>&nbsp;&nbsp;
		 					<a :href="page.pageUri+'?page='+(page.page-1)">上一页 </a>&nbsp;&nbsp;
		 				</span>
		 				<span v-if="page.page!=page.totalPage">
		 					<a :href="page.pageUri+'?page='+(page.page+1)">下一页 </a>&nbsp;&nbsp;
		 					<a :href="page.pageUri+'?page='+page.totalPage">末页</a>&nbsp;&nbsp;
		 				</span>
		 			</div>
		 		</center>
				</div>
			</div>
			</div>
</body>
</html>
