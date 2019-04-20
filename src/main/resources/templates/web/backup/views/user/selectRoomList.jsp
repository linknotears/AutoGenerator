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
			url:"room/findPage.html",
			refNames:['rooms','pageData']
		},
		{
			url:"orderForm/getCart.html",
			refNames:['existCart']
		},
	];
	//加载标签
	loadEls = ['#mainData',"#pageSelect"];
	//初始化数据
	init();
});
function addCart(id){
	request({
		url:'orderForm/addCart.html',
		data:{'roomId':id},
		successHandle:data =>{
			alert("添加成功！");
			//重新获取数据
			loadData();
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
					<th>房间名</th>
					<th>宠物容量</th>
					<th>已占用容量</th>
					<th>状态</th>
					<th>观赏金</th>
					<th>最大观看时间</th>
					<th>操作</th>
				</tr>
				<tr>
				<tr v-for="room in data.rooms">
					<th><a :href="'base/goto/user/selectPetList.html?roomId='+room.id" target="_blank">{{room.roomName}}</a></th>
					<th>{{room.maxSeat}}</th>
					<th>{{room.occupySeat}}</th>
					<th>{{room.rent}}</th>
					<th>{{room.state==1? '占用' : '空闲' }}</th>
					<th>{{room.maxTime}}</th>
					<th><center>
					<button @click="addCart(room.id)">加入购物车</button>&nbsp;
					</center></th>
				</tr>
			</table>
		</center>
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
</body>
</html>
