<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
## 首字母小写
#macro(tolowercase $str)$str.substring(0,1).toLowerCase()$str.substring(1)#end
## 后部分转换为小写
#macro(touppercase $str)$str.substring(0,1)$str.substring(1).toLowerCase()#end
## 首字母大写
#macro(toheaduppercase $str)$str.substring(0,1).toUpperCase()$str.substring(1)#end
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
			url:"#tolowercase($entity)/findPage.html",
			refNames:['#tolowercase($entity)s','pageData']
		}
##判断select
#foreach($field in ${table.fields})
#if($cfg.propertyType.get($table.name).get($field.name) == 'select' || ${field.propertyName.contains('Id')})
#set($propertyName = $field.propertyName.replace('Id',''))
		,
		{
			url:"${propertyName}/findList.html",
			refNames:['${propertyName}s'],
			successHandle: data => { 				
				//改造pettypes
				globalData.data.${propertyName}obj = arrJsonToMapping(globalData.data.${propertyName}s,"id","name");
			}
		}
#end
#end
	];
	//加载标签
	loadEls = ['#mainData',"#pageSelect"];
	//初始化数据
	init();
	
});

function viewUpdate(id){
	window.location.href = "base/goto/admin/viewUpdate${entity}.html?id="+id;
}

function remove(id){
	request({
		url:'#tolowercase($entity)/remove.html',
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
#foreach($field in ${table.fields})
##判断排除字段
#if(!$cfg.colExclude.get($table.name).get($field.name))
#if(!${field.keyFlag})
					<th style="text-align: center;">${field.comment}</th>
#end
#end
#end
				</tr>
				<tr>
				<tr v-for="#tolowercase($entity) in data.#tolowercase($entity)s">
#foreach($field in ${table.fields})
##判断排除字段
#if(!$cfg.colExclude.get($table.name).get($field.name))
#if(!${field.keyFlag})
##判断文件类型
#if($cfg.propertyType.get($table.name).get($field.name) == 'image' || ${field.propertyName.contains('image')})
					<th style="text-align: center;"><img width="80" height="80" alt="" :src="data.#tolowercase($entity).${field.propertyName}"></th>
#elseif($cfg.propertyType.get($table.name).get($field.name) == 'select' || ${field.propertyName.contains('Id')})
					<th>
#set($propertyName = $field.propertyName.replace('Id',''))
						{{$data.${propertyName}obj[#tolowercase($entity).${field.propertyName}]}}
					</th>
#else
					<th style="text-align: center;">{{ #tolowercase($entity).${field.propertyName} }}</th>
#end
#end
#end
#end
					<th><center>
					<button @click="viewUpdate(#tolowercase($entity).${table.fields[0].propertyName})">修改</button>&nbsp;
					<button @click="remove(#tolowercase($entity).${table.fields[0].propertyName})">删除</button>
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
