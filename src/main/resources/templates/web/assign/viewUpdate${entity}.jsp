<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

<title>更新</title>

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
<script type="text/javascript" src="common/util/request-tool-1.1.js"></script>
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
			url:"#tolowercase($entity)/findById.html",
			data:{
				'id':getParameter("id")
			},
			refNames:['#tolowercase($entity)']
		}
##判断select
#foreach($field in ${table.fields})
#if($cfg.propertyType.get($table.name).get($field.name) == 'select' || (${field.propertyName.contains('Id')} && !$cfg.propertyType.get($table.name).get($field.name)))
#set($propertyName = $field.propertyName.replace('Id',''))
		,
		{
			url:"${propertyName}/findList.html",
			refNames:['${propertyName}s']
		}
#end
#end
	];
	//加载标签
	loadEls = ['#mainData'];
	//初始化数据
	init();

});

function saveOrUpdate(form){
	return request({
		url: "#tolowercase($entity)/saveOrUpdate.html",
		form: form,
		success: function(data){
			window.location.href = "base/goto/admin/select${entity}List.html";
		},
		isMultipart: true,
		check: function(){return true;}
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
#set($temp = 1)
		<form onsubmit="return saveOrUpdate(this);" method="post" enctype="multipart/form-data">
			<table id="mainData"
					class="table table-striped table-bordered table-hover"
					style="width:70%">
					<tr>
					<input type="hidden" name="id" :value="data.#tolowercase($entity).${table.fields[0].propertyName}">
#foreach($field in ${table.fields})
##尽量不要在这里排除字段，显示会乱
#if(!$cfg.colExclude.get($table.name).get($field.name))
#if(!${field.keyFlag} || $cfg.propertyType.get($table.name).get($field.name))
##判断文件类型
#if($cfg.propertyType.get($table.name).get($field.name) == 'file' || ${field.propertyName.contains('image')})
						<th style="text-align: center;">${field.comment}</th>
						<th><input type="file" name="${field.propertyName}file"></th>
#elseif($cfg.propertyType.get($table.name).get($field.name) == 'select' || (${field.propertyName.contains('Id')} && !$cfg.propertyType.get($table.name).get($field.name)))
						<th style="text-align: center;">${field.comment}</th>
						<th>
							<select name="${field.propertyName}" :value="data.#tolowercase($entity).${field.propertyName}">
#set($propertyName = $field.propertyName.replace('Id',''))
								<option v-for="${propertyName} in data.${propertyName}s" :value="${propertyName}.id">{{ ${propertyName}.name }}</option>
							</select>
						</th>
#elseif($cfg.propertyType.get($table.name).get($field.name) == 'sex' || $field.propertyName == 'sex')
						<th style="text-align: center;">${field.comment}</th>
						<th>
							<select name="${field.propertyName}" :value="data.#tolowercase($entity).${field.propertyName}">
								<option value="true">男</option>
								<option value="false">女</option>
							</select>
						</th>
#elseif($field.type.contains('int') || $field.type.contains('double') || $field.type.contains('decimal'))
						<th style="text-align: center;">${field.comment}</th>
						<th>
							<input type="number" name="${field.propertyName}" :value="data.#tolowercase($entity).${field.propertyName}">
						</th>
#elseif($field.type == 'date')
						<th style="text-align: center;">${field.comment}</th>
						<th>
							<input type="date" name="${field.propertyName}" :value="formatDate(data.#tolowercase($entity).${field.propertyName})">
						</th>
#elseif($field.type == 'datetime')
						<th style="text-align: center;">${field.comment}</th>
						<th>
							<input type="datetime-local" name="${field.propertyName}" :value="dateFormat(data.#tolowercase($entity).${field.propertyName})">
						</th>
#else
						<th style="text-align: center;">${field.comment}</th>
						<th><input type="text" name="${field.propertyName}" :value="data.#tolowercase($entity).${field.propertyName}"></th>
#end
#if(($temp % 2) == 0)
					</tr>
					<tr>
#end
##自增
#set($temp = $temp + 1)
#end
#end
#end
					</tr>
				</table>
			<input class="btn btn-primary" type="submit" value="提交" />
			<input class="btn" type="reset" value="重置" />
			</form>
		</center>
	
		<script type="text/javascript">
			$("table input,select").css("width","185px");
		</script>
	</div>
</body>
</html>