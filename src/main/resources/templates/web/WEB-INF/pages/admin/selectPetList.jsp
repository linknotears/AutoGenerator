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
			url:"pet/findPage.html",
			refNames:['pets','pageData']
		},
		{
			url:"pettype/findList.html",
			refNames:['pettypes']
		},
		{
			url:'room/findList.html',
			refNames:['rooms']
		},
		{
			url:'food/findList.html',
			refNames:['foods'],
			successHandle: data =>{				
				//改造pettypes
				globalData.data.pettypes = arrJsonToMapping(globalData.data.pettypes,"id","name");
				globalData.data.foods = arrJsonToMapping(globalData.data.foods,"id","name");
				globalData.data.rooms = arrJsonToMapping(globalData.data.rooms,"id","roomName");
			}
		}
	];
	//加载标签
	loadEls = ['#mainData',"#pageSelect"];
	//初始化数据
	init();
	
});

function viewUpdate(id){
	window.location.href = "base/goto/admin/viewUpdatePet.html?id="+id;
}

function feed(id){
	request({
		url:'pet/feed.html',
		data:{'id':id,'reduce':200},
		successHandle:data =>{
			alert("喂食成功!")
			//重新获取数据
			loadData();
		}
	});
}
function remove(id){
	request({
		url:'pet/remove.html',
		data:{'id':id},
		successHandle:data =>{
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
					<th style="text-align: center;">宠物名</th>
					<th style="text-align: center;">宠物类型</th>
					<th style="text-align: center;">房间名</th>
					<th style="text-align: center;">食物</th>
					<th style="text-align: center;">饥饿状态</th>
					<th style="text-align: center;">操作</th>
				</tr>
				<tr>
				<tr v-for="pet in data.pets">
					<th style="text-align: center;">{{pet.name}}</th>
					<th style="text-align: center;">{{data.pettypes[pet.typeId]}}</th>
					<th style="text-align: center;">{{data.rooms[pet.roomId]}}</th>
					<th style="text-align: center;">{{data.foods[pet.foodId]}}</th>
					<th style="text-align: center;">{{pet.state==1? '饱和' : '饥饿' }}</th>
					<th><center>
					<button @click="feed(pet.id)">喂食</button>&nbsp;
					<button @click="viewUpdate(pet.id)">修改</button>&nbsp;
					<button @click="remove(pet.id)">删除</button>
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
