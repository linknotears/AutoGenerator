<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

<title>添加车辆</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="gdescription" content="This is my page">
<link href="assets/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="assets/css/font-awesome.min.css" />
<link rel="stylesheet" href="assets/css/ace.min.css" />
<link rel="stylesheet" href="assets/css/ace-rtl.min.css" />
<link rel="stylesheet" href="assets/css/ace-skins.min.css" />
<script src="assets/js/ace-extra.min.js"></script>
<script type="text/javascript" src="common/util/netutil-1.0.js"></script>
<style type="text/css">
select{
width: 160px;
}
input {
width: 160px;
}
</style>

<script type="text/javascript">

$(function(){
	//加载data
	loadUrls =[
		{
			url:"pet/findById.html",
			data:{
				'id':getParameter("id")
			},
			refNames:['pet']
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
			refNames:['foods']
		}
	];
	//加载标签
	loadEls = ['#mainData'];
	//初始化数据
	init();

});

function saveOrUpdate(form){
	return request({
		url: "pet/saveOrUpdate.html",
		form: form,
		successHandle:function(data){
			window.location.href = "base/goto/admin/selectPetList.html";
		},
		isMultipart:true,
		checkHandle:function(){return true;}
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
		<form onsubmit="return saveOrUpdate(this);" method="post" enctype="multipart/form-data">
			<table id="mainData"
					class="table table-striped table-bordered table-hover"
					style="width:70%">
					<tr>
						<th>宠物名</th>
						<th>
						<input type="hidden" name="id" :value="data.pet.id">
						<input type="text" name="name" :value="data.pet.name"></th>
						<th>宠物类型</th>
						<th>
							<select name="typeId" :value="data.pet.typeId">
								<option v-for="pettype in data.pettypes" :value="pettype.id">{{pettype.name}}</option>
							</select>
						</th>
					</tr>
					<tr>
						<th>房间名</th>
						<th>
							<select name="roomId" :value="data.pet.roomId">
								<option v-for="room in data.rooms" :value="room.id">{{room.roomName}}</option>
							</select>
						</th>
						<th>食物</th>
						<th>
							<select name="foodId" :value="data.pet.foodId">
								<option v-for="food in data.foods" :value="food.id">{{food.name}}</option>
							</select>
						</th>
					</tr>
					<tr>
						<th>饥饿状态</th>
						<th>
							<select name="state" :value="data.pet.state">
								<option value="1">饱和</option>
								<option value="0">饥饿</option>
							</select>
						</th>
						<th></th>
						<th></th>
					</tr>
				</table>
			<input class="btn btn-primary" type="submit" value="提交" />
			<input class="btn" type="reset" value="重置" />
			</form>
		</center>

	</div>
</body>
</html>